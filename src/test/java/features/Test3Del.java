package features;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test3Del {

    final String indiceNoExit = "water$water_log_apix";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";
    final String alias = "water$water_log_api";

    EsContext context = new EsContext("eshost:30480,eshost:30480");

    @Test
    public void test0() throws Exception {
        if (context.table(indice).deleteById("5")) {
            assert true;
        } else {
            assert false;
        }
    }

    @Test
    public void test1() throws Exception {
        String tmp = context.table(indice).where(q->q.term("level", 1)).delete();

        System.out.println(tmp);
    }
}
