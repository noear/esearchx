package org.noear.esearchx.model;

import org.noear.esearchx.PriUtils;
import org.noear.snack.ONode;

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

    public EsAggs cardinality(String asField, String field) {
        oNode.getOrNew(asField).getOrNew("cardinality").set("field", field);
        return this;
    }

    public EsAggs terms(String asField, String field) {
        oNode.getOrNew(asField).getOrNew("terms").set("field", field);
        return this;
    }

    public void topHits(String asField,int size, Consumer<EsSort> sort) {
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
