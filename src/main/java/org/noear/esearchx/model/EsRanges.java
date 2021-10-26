package org.noear.esearchx.model;

import org.noear.snack.ONode;

/**
 * @author noear
 * @since 1.0.3
 */
public class EsRanges {
    private final ONode oNode;

    public EsRanges(ONode oNode) {
        this.oNode = oNode;
    }

    public EsRanges add(Object from, Object to) {
        oNode.addNew().set("from", from).set("to", to);
        return this;
    }
}
