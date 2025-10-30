package org.noear.esearchx.model;

import org.noear.snack4.ONode;

import java.util.function.Consumer;

/**
 * @author noear
 * @since 1.0.14
 */
public class EsHighlight {
    private final ONode oNode;

    public EsHighlight(ONode oNode) {
        this.oNode = oNode;
    }

    public EsHighlight addField(String field, Consumer<EsHighlightField> consumer) {
        ONode oNode1 = oNode.getOrNew("fields").addNew().getOrNew(field);
        consumer.accept(new EsHighlightField(oNode1));
        return this;
    }
}
