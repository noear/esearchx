package org.noear.esearchx;

import org.noear.snack.ONode;

/**
 * @author noear
 * @since 1.0
 */
public class EsSetting {
    protected ONode oNode = new ONode();

    public EsSetting set(String name, Object value) {
        oNode.set(name, value);
        return this;
    }
//这个不参修改
//    public EsSetting numberOfShards(int value) {
//        return set("number_of_shards", value);
//    }

    public EsSetting numberOfReplicas(int value) {
        return set("number_of_replicas", value);
    }

    public EsSetting refreshInterval(Object value) {
        return set("refresh_interval", value);
    }
}
