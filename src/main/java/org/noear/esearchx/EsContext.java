package org.noear.esearchx;


import org.noear.snack.ONode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * ElasticSearch 上下文（只支持 7.x +）
 *
 * @author noear
 * @since 1.0
 */
public class EsContext {
    private final String[] urls;
    private int urlIndex;
    private final String username;
    private final String paasword;

    @Deprecated
    public EsCommand lastCommand;

    public EsContext(Properties prop) {
        this(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("paasword"));
    }

    public EsContext(String url) {
        this(url, null, null);
    }

    public EsContext(String url, String username, String paasword) {
        this.username = username;
        this.paasword = paasword;

        List<String> urlAry = new ArrayList<>();
        for (String ser : url.split(",")) {
            if (ser.contains("://")) {
                urlAry.add(ser);
            } else {
                urlAry.add("http://" + ser);
            }
        }
        this.urls = urlAry.toArray(new String[urlAry.size()]);
    }

    private String getUrl() {
        if (urls.length == 0) {
            return urls[0];
        } else {
            if (urlIndex > 10000000) {
                urlIndex = 0;
            }

            return urls[urlIndex % urls.length];
        }
    }

    protected PriHttpUtils getHttp(String path) {
        PriHttpUtils http = PriHttpUtils.http(getUrl() + path);

        if (PriUtils.isNotEmpty(username)) {
            String token = PriUtils.b64Encode(username + ":" + paasword);
            String auth = "Basic " + token;

            http.header("Authorization", auth);
        }

        return http;
    }

    public String execAsBody(EsCommand cmd) throws IOException {
        if (PriUtils.isEmpty(cmd.dsl)) {
            return getHttp(cmd.path).execAsBody(cmd.method);
        } else {
            return getHttp(cmd.path).bodyTxt(cmd.dsl, cmd.dslType).execAsBody(cmd.method);
        }
    }

    public int execAsCode(EsCommand cmd) throws IOException {
        if (PriUtils.isEmpty(cmd.dsl)) {
            return getHttp(cmd.path).execAsCode(cmd.method);
        } else {
            return getHttp(cmd.path).bodyTxt(cmd.dsl, cmd.dslType).execAsCode(cmd.method);
        }
    }

    /**
     * 获取表操作
     */
    public EsTableQuery table(String table) {
        return new EsTableQuery(this, table);
    }

    /**
     * 表结构创建
     *
     * @param indiceName 索引名字
     */
    public String tableCreate(String indiceName, String dsl) throws IOException {
        PriHttpUtils http = getHttp(String.format("/%s", indiceName));

        String tmp = http.bodyTxt(dsl, PriWw.mime_json).put();
        //return: {"acknowledged":true,"shards_acknowledged":true,"index":"water$water_log_api_202110"}

        return tmp;
    }

    /**
     * 表结构是否存在
     *
     * @param indiceName 索引名字
     */
    public boolean tableExist(String indiceName) throws IOException {
        int tmp = getHttp(String.format("/%s", indiceName)).head();

        return tmp == 200; //404不存在
    }

    /**
     * 表结构删除
     *
     * @param indiceName 索引名字
     */
    public String tableDrop(String indiceName) throws IOException {
        String tmp = getHttp(String.format("/%s", indiceName)).delete();

        return tmp;
    }

    /**
     * 表结构显示
     *
     * @param indiceName 索引名字
     */
    public String tableShow(String indiceName) throws IOException {
        String tmp = getHttp(String.format("/%s", indiceName)).get();

        return tmp;
    }

    /////////////////

    /**
     * 表别名处理
     */
    public String tableAliases(Consumer<EsAliases> aliases) throws IOException {
        EsAliases e = new EsAliases();
        aliases.accept(e);

        PriHttpUtils http = getHttp("/_aliases");

        ONode oNode = new ONode().build(n -> n.set("actions", e.oNode));

        String dsl = oNode.toJson();
        String tmp = http.bodyTxt(dsl, PriWw.mime_json).post();

        return tmp;
    }
}
