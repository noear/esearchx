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
        ONode oNode1 = oNode.addNew();

        if (from != null) {
            oNode1.set("from", from);
        }

        if (to != null) {
            oNode1.set("to", to);
        }

        return this;
    }
}
