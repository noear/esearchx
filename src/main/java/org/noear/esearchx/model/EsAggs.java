package org.noear.esearchx.model;

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


    //
    //============ Metrics =============
    //

    /**
     * sum，求合
     */
    public EsAggs sum(String field) {
        funSet(field + "_sum", field, "sum");
        return this;
    }

    /**
     * avg，求平均值
     */
    public EsAggs avg(String field) {
        funSet(field + "_avg", field, "avg");
        return this;
    }

    /**
     * max，求最大值
     */
    public EsAggs max(String field) {
        funSet(field + "_max", field, "max");
        return this;
    }

    /**
     * min，求最小值
     */
    public EsAggs min(String field) {
        funSet(field + "_min", field, "min");
        return this;
    }

    /**
     * count，值计数
     */
    public EsAggs count(String field) {
        funSet(field + "_count", field, "value_count");
        return this;
    }


    /**
     * top_hits，每一个聚合Bucket里面仅返回指定顺序的前N条数据。
     */
    public EsAggs top(int size) {
        return top(size, null);
    }

    public EsAggs top(int size, Consumer<EsSort> sort) {
        ONode top_hits = oNode.getOrNew("$top").getOrNew("top_hits");

        top_hits.set("size", size);

        if (sort != null) {
            EsSort s = new EsSort(top_hits.getOrNew("sort").asArray());
            sort.accept(s);
        }

        return this;
    }

    /**
     * cardinality，求基数（先去重再求和）
     */
    public EsAggs cardinality(String field) {
        funSet(field + "_cardinality", field, "cardinality");
        return this;
    }

    /**
     * percentiles，多值聚合求百分比
     */
    public EsAggs percentiles(String field, int[] percents) {
        ONode oNode1 = oNode.getOrNew(field + "_percentiles").getOrNew("percentiles");
        oNode1.set("field", field);
        oNode1.getOrNew("percents").addAll(Arrays.asList(percents));
        return this;
    }

    /**
     * percentiles rank
     */
    public EsAggs percentilesRank(String field, int[] values) {
        ONode oNode1 = oNode.getOrNew(field + "_percentilesRank").getOrNew("percentile_ranks");
        oNode1.set("field", field);
        oNode1.getOrNew("values").addAll(Arrays.asList(values));
        return this;
    }

    /**
     * extended_stats
     */
    public EsAggs extendedStats(String field) {
        funSet(field + "_extendedStats", field, "extended_stats");
        return this;
    }

    /**
     * stats
     */
    public EsAggs stats(String field) {
        funSet(field + "_stats", field, "stats");
        return this;
    }

    //
    //============ Bucket =============
    //

    /**
     * filter，聚合
     */
    public EsAggs filter(Consumer<EsCondition> condition) {
        EsCondition c = new EsCondition(oNode.getOrNew("$filter"));
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
     */
    public EsAggs terms(String field) {
        terms(field, null);
        return this;
    }

    public EsAggs terms(String field,  Consumer<EsTerms> terms) {
        ONode oNode1 = oNode.getOrNew(field + "_terms").getOrNew("terms");

        if (field.startsWith("$")) {
            oNode1.set("script", field.substring(1));
        } else {
            oNode1.set("field", field);
        }

        if (terms != null) {
            EsTerms t = new EsTerms(oNode);
            terms.accept(t);
        }

        return this;
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
