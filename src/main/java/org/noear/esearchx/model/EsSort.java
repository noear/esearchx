package org.noear.esearchx.model;

import org.noear.snack.ONode;

/**
 * @author noear
 * @since 1.0.3
 */
public class EsSort {
    private final ONode oNode;

    public EsSort(ONode oNode) {
        this.oNode = oNode;
    }

    @Deprecated
    public EsSort addByAes(String field) {
        oNode.addNew().getOrNew(field).set("order", "asc");

        return this;
    }

    public EsSort addByAsc(String field) {
        oNode.addNew().getOrNew(field).set("order", "asc");

        return this;
    }

    public EsSort addByDesc(String field) {
        oNode.addNew().getOrNew(field).set("order", "desc");

        return this;
    }
}
