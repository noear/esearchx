package org.noear.esearchx.model;

import org.noear.snack.ONode;

/**
 * @author noear 2021/10/26 created
 */
public class EsAggs {
    private final ONode oNode;
    public EsAggs(ONode oNode){
        this.oNode = oNode;
    }

    public EsAggs cardinality(String field){
        return this;
    }

    public EsAggs terms(String field){
        return this;
    }

}
