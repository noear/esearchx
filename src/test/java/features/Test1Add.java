package features;

import features.model.LogDo;
import org.junit.jupiter.api.Test;
import org.noear.esearchx.EsCommand;
import org.noear.esearchx.EsContext;
import org.noear.esearchx.PriWw;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.solon.test.SolonTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */
@SolonTest
public class Test1Add {
    final String indice = "test-user_log_202110";
    final String indice2 = "test-user_log_202109";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化


    Random random = new Random();

    public void test1() throws IOException {
        //demo
        EsCommand command = new EsCommand();
        command.path = "/demo/_doc/";
        command.method = "POST";
        command.dsl = "";
        command.dslType = PriWw.mime_json;

        String rst = context.execAsBody(command);
        System.out.println(rst);
    }

    @Test
    public void test2() throws Exception {
        String json = ResourceUtil.getResourceAsString("demo/log.json", "utf-8");

        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = SnowflakeUtils.genId();
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test1";
        logDo.level = 2;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();

        String rst = context.indice(indice).upsert(Utils.guid(), logDo);
        System.out.println(rst);
    }

    private void test3_do(long id, String json) throws Exception {
        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = id;
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test1";
        logDo.level = 2;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();

        context.indice(indice).upsert(String.valueOf(id), logDo);
    }

    @Test
    public void test3() throws Exception {
        String json = ResourceUtil.getResourceAsString("demo/log.json", "utf-8");

        test3_do(1, json);
        test3_do(2, json);
        test3_do(3, json);
        test3_do(4, json);
        test3_do(5, json);
    }

    @Test
    public void test4() throws Exception {
        String json = ResourceUtil.getResourceAsString("demo/log.json", "utf-8");

        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = SnowflakeUtils.genId();
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test2";
        logDo.level = (random.nextInt() % 5) + 1;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();

        String rst = context.indice(indice).insert(logDo);
        System.out.println(rst);
        assert rst.contains("\"errors\":") == false;
    }

    @Test
    public void test5() throws Exception {
        String json = ResourceUtil.getResourceAsString("demo/log.json", "utf-8");

        Map<String, LogDo> docs = new LinkedHashMap<>();

        for (int i = 0; i < 200; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "map1";
            logDo.level = (random.nextInt() % 5) + 1;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.put(Utils.guid(), logDo);
        }

        String rst = context.indice(indice2).upsertList(docs);
        System.out.println(rst);
        assert rst.contains("\"errors\":false");
    }

    @Test
    public void test6() throws Exception {
        String json = ResourceUtil.getResourceAsString("demo/log.json", "utf-8");

        List<LogDo> docs = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "list1";
            logDo.level = (random.nextInt() % 5) + 1;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.add(logDo);
        }

        String rst = context.indice(indice2).insertList(docs);
        System.out.println(rst);
        assert rst.contains("\"errors\":false");
    }
}
