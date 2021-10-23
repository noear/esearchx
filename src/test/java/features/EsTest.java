package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.solon.Utils;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class EsTest {
    final String indiceNoExit = "water$water_log_api";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";

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

    @Test
    public void test2() throws Exception {
        if (context.tableExist(indice) == false) {
            String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
            context.tableCreate(indice, dsl);
        }

        assert context.tableExist(indice) == true;
    }

}
