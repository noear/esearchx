package features;

import features.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.esearchx.EsData;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */

@RunWith(SolonJUnit4ClassRunner.class)
public class Test2Select {

    final String indice = "water$water_log_api";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化

    @Test
    public void test_selectById() throws Exception {
        LogDo logDo = context.indice("water$water_log_api_202110").selectById(LogDo.class, "1");
        assert logDo != null;
        assert logDo.log_id == 1;


        logDo = context.indice("water$water_log_api_202106").selectById(LogDo.class,"1");
        assert logDo == null;
    }

    @Test
    public void test_selectByIds() throws Exception {
        List<LogDo> result = context.indice(indice)
                .selectByIds(LogDo.class, Arrays.asList("1","2"));

        System.out.println(result);

        assert result != null;
        assert result.size() == 2;
    }

    @Test
    public void test_term() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_term_score() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.useScore().term("tag", "list1"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_terms() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.terms("tag", "list1", "map1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_prefix() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.prefix("tag", "m"))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }


    @Test
    public void test_match() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.match("tag", "list1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_match2() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.match("content", "class_name"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhrase() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrase("tag", "list1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhraseSolp() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrase("tag", "list1", 2))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhrasePrefix() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrasePrefix("content", "class"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_matchPhrasePrefixSolp() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrasePrefix("content", "name tag", 2))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 0;


        result = context.indice(indice)
                .where(c -> c.matchPhrasePrefix("content", "key", 2))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_range() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.range("level", r -> r.gt(3)))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_wildcard() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.wildcard("tag", "l*"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_regexp() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.regexp("tag", "l.*?"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }
}
