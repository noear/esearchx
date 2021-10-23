package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.test.SolonJUnit4ClassRunner;

import java.io.IOException;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test0 {
    final String indiceNoExit = "water$water_log_apix";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";
    final String alias = "water$water_log_api";

    EsContext context = new EsContext("eshost:30480,eshost:30480");

    @Test
    public void test0() throws Exception {
        assert context.tableExist(indiceNoExit) == false;
    }

    @Test
    public void test1() throws Exception {
        if (context.tableExist(indiceNew)) {
            context.tableDrop(indiceNew);
        }

        assert context.tableExist(indiceNew) == false;

        String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
        context.tableCreate(indiceNew, dsl);


        assert context.tableExist(indiceNew) == true;
    }


    private void test2Create(String name) throws IOException {
        if (context.tableExist(name) == false) {
            String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
            context.tableCreate(name, dsl);
        }

        assert context.tableExist(name) == true;
    }

    @Test
    public void test2() throws Exception {
        test2Create(indice);
    }

    @Test
    public void test3() throws Exception {
        test2Create("water$water_log_api_202110");
        test2Create("water$water_log_api_202109");
        test2Create("water$water_log_api_202108");
        test2Create("water$water_log_api_202107");
        test2Create("water$water_log_api_202106");
        test2Create("water$water_log_api_202105");
    }

    @Test
    public void test4() throws Exception {
        context.tableAliases(a -> a
                .add("water$water_log_api_202110", alias)
                .add("water$water_log_api_202109", alias)
                .add("water$water_log_api_202108", alias)
                .add("water$water_log_api_202107", alias)
                .add("water$water_log_api_202106", alias)
                .add("water$water_log_api_202105", alias));
    }

    @Test
    public void test5() throws Exception {
        String json = context.tableShow(indice);
        System.out.println(json);
        ONode oNode = ONode.loadStr(json);

        assert oNode.get(indice).contains("mappings");
        assert oNode.get(indice).contains("settings");

        ////
        try {
            context.tableShow(indiceNoExit); //不存在会 404 错误
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
