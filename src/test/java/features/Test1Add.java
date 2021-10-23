package features;

import features.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.solon.Utils;
import org.noear.solon.test.SolonJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.*;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test1Add {
    final String indiceNoExit = "water$water_log_api";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";
    final String indice2 = "water$water_log_api_202109";

    EsContext context = new EsContext("eshost:30480,eshost:30480");


    @Test
    public void test2() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

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


        context.table(indice).upsert(Utils.guid(), logDo);
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

        context.table(indice).upsert(String.valueOf(id), logDo);
    }

    @Test
    public void test3() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        test3_do(1, json);
        test3_do(2, json);
        test3_do(3, json);
        test3_do(4, json);
        test3_do(5, json);
    }

    @Test
    public void test4() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = SnowflakeUtils.genId();
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test2";
        logDo.level = 3;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();

        context.table(indice).insert(logDo);
    }

    @Test
    public void test5() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        Map<String, LogDo> docs = new LinkedHashMap<>();

        for (int i = 0; i < 200; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "map1";
            logDo.level = 4;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.put(Utils.guid(), logDo);
        }

        context.table(indice2).upsertList(docs);
    }

    @Test
    public void test6() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        List<LogDo> docs = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "list1";
            logDo.level = 5;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.add(logDo);
        }

        context.table(indice2).insertList(docs);
    }
}
