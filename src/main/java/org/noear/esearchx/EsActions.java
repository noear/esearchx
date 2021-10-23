package org.noear.esearchx;

import org.noear.snack.ONode;

/**
 * @author noear
 * @since 1.2
 */
public class EsActions {
    protected ONode oNode = new ONode();

    public EsActions add(String indiceName, String alias) {
        oNode.addNew().getOrNew("add").set("index", indiceName).set("alias", alias);
        return this;
    }

    public EsActions remove(String indiceName, String alias) {
        oNode.addNew().getOrNew("remove").set("index", indiceName).set("alias", alias);
        return this;
    }
}
