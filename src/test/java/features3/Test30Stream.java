package features3;

import features.SnowflakeUtils;
import features.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author noear 2022/1/18 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test30Stream {
    final String streamNoExit = "test-order_notexit";
    final String templateNoExit = "test-order-tml_notexit";
    final String policyNoExit = "test-order-policy_notexit";

    final String stream = "test-order";
    final String template = "test-order-tml";
    final String policy = "test-order-policy";


    @Inject("${test.esx}")
    EsContext context;

    @Test
    public void test1() throws Exception {
        assert context.templateExist(templateNoExit) == false;
        assert context.policyExist(policyNoExit) == false;
    }

    @Test
    public void test2() throws Exception {
        //创建或者更新微略
        String policy_dsl = Utils.getResourceAsString("demo30/log-policy_dsl.json");

        String policy_dsl_rst = context.policyCreate(policy, policy_dsl);
        System.out.println(policy_dsl_rst);


        //创建或者更新模板
        String tml_dsl = Utils.getResourceAsString("demo30/log-index.json");

        ONode tmlDslNode = ONode.loadStr(tml_dsl);
        //设定匹配模式
        tmlDslNode.getOrNew("index_patterns").val(stream+"*");
        //设定别名
        //tmlDslNode.getOrNew("aliases").getOrNew(stream).asObject(); //stream 不需要别名
        //设定策略
        tmlDslNode.get("settings").get("index.lifecycle.name").val(policy);
        //设定翻转别名
        tmlDslNode.get("settings").get("index.lifecycle.rollover_alias").val(stream);

        String index_dsl_rst = context.templateCreate(template, tmlDslNode.toJson());
        System.out.println(index_dsl_rst);


//        String stream_rst = context.streamCreate(stream);
//        System.out.println(stream_rst);
    }


    Random random = new Random();

    @Test
    public void test3() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        Map<String, ONode> docs = new LinkedHashMap<>();

        for (int i = 0; i < 4; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "map1";
            logDo.level =  (random.nextInt() % 5) + 1;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.put(Utils.guid(), ONode.loadObj(logDo).build(n->{
                n.set("@timestamp", logDo.log_fulltime);
            }));
        }

        String rst = context.stream(stream).upsertList(docs);
        System.out.println(rst);
    }
}
