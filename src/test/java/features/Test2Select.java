package features;

import features.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.esearchx.model.EsPage;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */

@RunWith(SolonJUnit4ClassRunner.class)
public class Test2Select {

    final String indice = "water$water_log_api";

    EsContext context = new EsContext("eshost:30480,eshost:30480");


    @Test
    public void test_selectById() throws Exception {
        LogDo logDo = context.table("water$water_log_api_202110").selectById("1", LogDo.class);
        assert logDo != null;
        assert logDo.log_id == 1;


        logDo = context.table("water$water_log_api_202106").selectById("1", LogDo.class);
        assert logDo == null;
    }

    @Test
    public void test_term() throws Exception {

        EsPage<LogDo> result = context.table(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_terms() throws Exception {

        EsPage<LogDo> result = context.table(indice)
                .where(c -> c.terms("tag", "list1", "map1"))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_prefix() throws Exception {

        EsPage<LogDo> result = context.table(indice)
                .where(c -> c.prefix("tag", "m"))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }



    @Test
    public void test_match() throws Exception {

        EsPage<LogDo> result = context.table(indice)
                .where(c -> c.match("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhrase() throws Exception {

        EsPage<LogDo> result = context.table(indice)
                .where(c -> c.matchPhrase("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhrasePrefix() throws Exception {

        EsPage<LogDo> result = context.table(indice)
                .where(c -> c.matchPhrasePrefix("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }
}
