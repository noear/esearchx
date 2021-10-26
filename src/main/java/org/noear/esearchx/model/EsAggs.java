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

    /**
     * sum，求合
     */
    public EsAggs sum(String field) {
        funSet("$sum", field, "sum");
        return this;
    }

    /**
     * avg，求平均值
     */
    public EsAggs avg(String field) {
        funSet("$avg", field, "avg");
        return this;
    }

    /**
     * count，值计数
     */
    public EsAggs count(String field) {
        funSet("$count", field, "value_count");
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
     * percentiles，百分比
     */
    public EsAggs percentiles(String field, int[] percents) {
        ONode oNode1 = oNode.getOrNew("$percentiles").getOrNew("percentiles");
        oNode1.set("field", field);
        oNode1.getOrNew("percents").addAll(Arrays.asList(percents));
        return this;
    }

    /**
     * percentiles rank
     */
    public EsAggs percentilesRank(String field, int[] values) {
        ONode oNode1 = oNode.getOrNew("$percentilesRank").getOrNew("percentile_ranks");
        oNode1.set("field", field);
        oNode1.getOrNew("values").addAll(Arrays.asList(values));
        return this;
    }

    /**
     * cardinality，去重
     */
    public EsAggs cardinality(String field) {
        funSet("$cardinality", field, "cardinality");
        return this;
    }

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
        terms(field, 0, null);
        return this;
    }

    public EsAggs terms(String field, int size) {
        terms(field, size, null);
        return this;
    }

    public EsAggs terms(String field, int size, Consumer<EsSort> sort) {
        ONode oNode1 = oNode.getOrNew("$terms").getOrNew("terms");

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
