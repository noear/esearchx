package features;

import features.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.esearchx.EsContext;
import org.noear.esearchx.model.EsPage;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */

@RunWith(SolonJUnit4ClassRunner.class)
public class Test2SelectPlus {

    final String indice = "water$water_log_api";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化


    @Test
    public void test1() throws Exception {

        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.must().term("tag", "list1").term("level", 3))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test1_filter() throws Exception {

        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.filter().term("tag", "list1").term("level", 3))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test1_score() throws Exception {

        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.useScore().must().term("tag", "list1").term("level", 3))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test2() throws Exception {

        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.match("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test3() throws Exception {

        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.must().match("tag", "list1").term("level", 3))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test4() throws Exception {

        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.must()
                        .match("tag", "list1")
                        .term("level", 3)
                        .add(c1 -> c1.mustNot()
                                .matchPhrasePrefix("summary", "${")
                                .matchPhrasePrefix("summary", "#{")))
                .limit(0, 10)
                .orderByDesc("log_id")
                .select(LogDo.class);

        System.out.println(result);
        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test5() throws Exception {
        //输出字段控制（选择模式）
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class, "log_id,trace_id");

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
        assert result.getList().get(0).tag == null;
    }

    @Test
    public void test6() throws Exception {
        //输出字段控制（排除模式）
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class, "!log_id,trace_id");

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id == 0;
        assert result.getList().get(0).tag != null;
    }

    @Test
    public void test7() throws Exception {
        //输出字段控制（选择模式）
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(5)
                .orderByAsc("log_id")
                .onAfter(239467464128819200l)
                .select(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 5;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test8() throws Exception {
        //输出字段控制（选择模式）
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .orderByDesc("log_id")
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > result.getList().get(1).log_id;
    }

    @Test
    public void test9() throws Exception {
        //输出字段控制（选择模式）
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .orderByDesc("level")
                .andByAsc("log_id")
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test10() throws Exception {
        //输出字段控制（选择模式）
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.useScore().must()
                        .term("tag", "list1")
                        .range("level", r -> r.gt(3)))
                .limit(0, 10)
                .orderByAsc("level")
                .andByAsc("log_id")
                .minScore(1)
                .select(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).level > 3;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test11() throws Exception {
        //输出字段控制（选择模式）
        EsPage<LogDo> result = context.indice(indice)
                .where(c -> c.filter()
                        .term("tag", "list1")
                        .range("level", r -> r.gt(3)))
                .limit(0, 10)
                .orderByAsc("level")
                .andByAsc("log_id")
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).level > 3;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }
}
