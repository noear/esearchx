package features;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * @author noear 2022/1/18 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test10Stream {
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
        String policy_dsl = Utils.getResourceAsString("demo10/log-policy_dsl.json");

        String policy_dsl_rst = context.policyCreate(policy, policy_dsl);
        System.out.println(policy_dsl_rst);


        String index_dsl = Utils.getResourceAsString("demo10/log-index.json");
        String index_dsl_rst = context.templateCreate(template, index_dsl);
        System.out.println(index_dsl_rst);
    }

    @Test
    public void test3() throws Exception {

    }
}
