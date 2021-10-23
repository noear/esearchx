package org.noear.esearchx;

import org.noear.snack.ONode;

/**
 * @author noear
 * @since 1.2
 */
public class EsAliases {
    protected ONode oNode = new ONode();

    public EsAliases add(String indiceName, String alias) {
        oNode.addNew().getOrNew("add").set("index", indiceName).set("alias", alias);
        return this;
    }

    public EsAliases remove(String indiceName, String alias) {
        oNode.addNew().getOrNew("remove").set("index", indiceName).set("alias", alias);
        return this;
    }
}
