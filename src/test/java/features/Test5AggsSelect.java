package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/26 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test5AggsSelect {
    final String indice = "test-user_log";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化

    @Test
    public void test0() throws Exception {
        ONode oNode = context.indice(indice)
                .where(c -> c.range("level", r -> r.gte(3)))
                .limit(0)
                .aggs(a -> a.sum("level"))
                .selectAggs();

        System.out.println(oNode.toJson());

        System.out.println(oNode.get("$sum").get("value").getDouble());
    }

    @Test
    public void test1() throws Exception {
        String tmp = context.indice(indice)
                .limit(0)
                .aggs(a -> a.terms("level"))
                .selectJson();

        System.out.println(tmp);
    }

    @Test
    public void test2() throws Exception {
        String tmp = context.indice(indice)
                .limit(0)
                .aggs(a -> a.terms("level").top(1))
                .selectJson();

        System.out.println(tmp);
    }

    @Test
    public void test3() throws Exception {
        String tmp = context.indice(indice)
                .limit(0)
                .aggs(a -> a.cardinality("level"))
                .selectJson();

        System.out.println(tmp);
    }
}
