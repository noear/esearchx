package features;

import org.noear.esearchx.EsContext;
import org.noear.solon.annotation.Inject;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/26 created
 */
//@RunWith(SolonJUnit4ClassRunner.class)
public class Test5AggsAdd {
    final String indice = "test-order";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化
}
