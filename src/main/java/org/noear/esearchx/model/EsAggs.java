package org.noear.esearchx.model;

import org.noear.esearchx.PriUtils;
import org.noear.snack.ONode;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author noear
 * @since 1.0.3
 */
public class EsAggs {
    private final ONode oNode;

    public EsAggs(ONode oNode) {
        this.oNode = oNode;
    }

    private void funSet(String asField, String field, String funName) {
        oNode.getOrNew(asField).getOrNew(funName).set("field", field);
    }

    /**
     * sum，求合
     */
    public EsAggs sum(String asField, String field) {
        funSet(asField, field, "sum");
        return this;
    }

    /**
     * avg，求平均值
     */
    public EsAggs avg(String asField, String field) {
        funSet(asField, field, "avg");
        return this;
    }

    /**
     * percentiles，百分比
     * */
    public EsAggs percentiles(String asField, String field, int[] percents) {
        ONode oNode1 = oNode.getOrNew(asField).getOrNew("percentiles");
        oNode1.set("field", field);
        oNode1.getOrNew("percents").addAll(Arrays.asList(percents));
        return this;
    }

    /**
     * percentiles rank
     * */
    public EsAggs percentilesRank(String asField, String field, int[] values) {
        ONode oNode1 = oNode.getOrNew(asField).getOrNew("percentile_ranks");
        oNode1.set("field", field);
        oNode1.getOrNew("values").addAll(Arrays.asList(values));
        return this;
    }

    /**
     * cardinality，去重
     * */
    public EsAggs cardinality(String asField, String field) {
        funSet(asField, field, "cardinality");
        return this;
    }

    /**
     * filter，聚合
     * */
    public EsAggs filter(String asField, Consumer<EsCondition> condition) {
        EsCondition c = new EsCondition(oNode.getOrNew(asField));
        c.filter();
        condition.accept(c);

        return this;
    }

//    /**
//     * filter，聚合
//     * */
//    public EsAggs range(String asField) {
//
//        return this;
//    }

    /**
     * terms，聚合
     * */
    public EsAggs terms(String asField, String field) {
        terms(asField, field, 0, null);
        return this;
    }

    public EsAggs terms(String asField, String field, int size) {
        terms(asField, field, size, null);
        return this;
    }

    public EsAggs terms(String asField, String field, int size, Consumer<EsSort> sort) {
        ONode oNode1 = oNode.getOrNew(asField).getOrNew("terms");

        oNode1.set("field", field);

        if (size > 0) {
            oNode1.set("size", size);
        }

        if (sort != null) {
            EsSort s = new EsSort(oNode1.getOrNew("sort").asArray());
            sort.accept(s);
        }

        return this;
    }

    public void topHits(String asField, int size, Consumer<EsSort> sort) {
        ONode top_hits = oNode.getOrNew(asField).getOrNew("top_hits");

        top_hits.set("size", size);

        EsSort s = new EsSort(top_hits.getOrNew("sort").asArray());
        sort.accept(s);
    }

    /**
     * 添加下级条件
     */
    public EsAggs add(Consumer<EsAggs> aggs) {
        EsAggs c = new EsAggs(oNode.getOrNew("aggs"));
        aggs.accept(c);

        return this;
    }
}

//https://blog.csdn.net/ifenggege/article/details/86103918
//https://www.cnblogs.com/duanxz/p/6528161.html
