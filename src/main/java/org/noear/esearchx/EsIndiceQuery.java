package org.noear.esearchx;

import org.noear.esearchx.exception.NoExistException;
import org.noear.esearchx.model.EsPage;
import org.noear.snack.ONode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * ElasticSearch 查询构建器
 *
 * @author noear
 * @since 1.0
 */
public class EsIndiceQuery {
    private final EsContext context;
    private final String indiceName;

    private ONode dslq;
    private ONode queryMatch;
    private ONode item;

    protected EsIndiceQuery(EsContext context, String indiceName) {
        this.context = context;
        this.indiceName = indiceName;
    }

    private PriHttpUtils getHttp(String path) {
        return context.getHttp(path);
    }

    private ONode getDslq() {
        if (dslq == null) {
            dslq = new ONode().asObject();
        }

        return dslq;
    }

    private ONode getQueryMatch() {
        if (queryMatch == null) {
            queryMatch = new ONode().asObject();
        }

        return queryMatch;
    }


    public EsIndiceQuery set(String field, Object value) {
        if (item == null) {
            item = new ONode();
        }

        item.set(field, value);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //
    // insert
    //

    private String insertDo(ONode doc) throws IOException {
        EsCommand cmd = new EsCommand();
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = doc.toJson();
        cmd.path = String.format("/%s/_doc/", indiceName);

        String tmp = context.execAsBody(cmd); //需要 post
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    private String upsertDo(String docId, ONode doc) throws IOException {
        EsCommand cmd = new EsCommand();
        cmd.method = PriWw.method_put;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = doc.toJson();
        cmd.path = String.format("/%s/_doc/%s", indiceName, docId);

        String tmp = context.execAsBody(cmd);; //需要 put
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }

    /**
     * 插入
     */
    public String insert() throws IOException {
        return insertDo(item);
    }

    public <T> String insert(T doc) throws IOException {
        return insertDo(ONode.loadObj(doc));
    }


    public <T> String insertList(List<T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        docs.forEach((doc) -> {
            docJson.append(new ONode().build(n -> n.getOrNew("index").asObject()).toJson()).append("\n");
            docJson.append(ONode.loadObj(doc).toJson()).append("\n");
        });

        EsCommand cmd = new EsCommand();
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_ndjson;
        cmd.dsl = docJson.toString();
        cmd.path = String.format("/%s/_doc/_bulk", indiceName);

        String tmp = context.execAsBody(cmd); //需要 post
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }


    public String upsert(String docId) throws IOException {
        return upsertDo(docId, item);
    }

    public <T> String upsert(String docId, T doc) throws IOException {
        return upsertDo(docId, ONode.loadObj(doc));
    }

    public <T> String upsertList(Map<String, T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        docs.forEach((docId, doc) -> {
            docJson.append(new ONode().build(n -> n.getOrNew("index").set("_id", docId)).toJson()).append("\n");
            docJson.append(ONode.loadObj(doc).toJson()).append("\n");
        });

        EsCommand cmd = new EsCommand();
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_ndjson;
        cmd.dsl = docJson.toString();
        cmd.path = String.format("/%s/_doc/_bulk", indiceName);

        String tmp = context.execAsBody(cmd); //需要 post
        //return: {"_index":"water$water_log_api_202110","_type":"_doc","_id":"eaeb3a43674a45ee8abf7cca379e4834","_version":1,"result":"created","_shards":{"total":2,"successful":1,"failed":0},"_seq_no":0,"_primary_term":1}

        return tmp;
    }


    //
    // select
    //
    public EsIndiceQuery where(Consumer<EsCondition> condition) {
        EsCondition c = new EsCondition();
        condition.accept(c);
        getDslq().set("query", c.oNode);
        return this;
    }

    public EsIndiceQuery limit(int start, int size) {
        getDslq().set("from", start);
        getDslq().set("size", size);
        return this;
    }

    public EsIndiceQuery limit(int size) {
        getDslq().set("size", size);
        return this;
    }

    //
    //排序
    //

    public EsIndiceQuery orderByAsc(String field) {
        getDslq().getOrNew("sort").getOrNew(field).set("order", "asc");
        return this;
    }

    public EsIndiceQuery orderByDesc(String field) {
        getDslq().getOrNew("sort").getOrNew(field).set("order", "desc");
        return this;
    }

    public EsIndiceQuery andByAsc(String field) {
        getDslq().getOrNew("sort").getOrNew(field).set("order", "asc");
        return this;
    }

    public EsIndiceQuery andByDesc(String field) {
        getDslq().getOrNew("sort").getOrNew(field).set("order", "desc");
        return this;
    }

    /**
     * search_after
     */
    public EsIndiceQuery onAfter(Object... values) {
        getDslq().getOrNew("search_after").addAll(Arrays.asList(values));
        return this;
    }

    /**
     * min_score
     * */
    public EsIndiceQuery minScore(Object value){
        getDslq().getOrNew("min_score").val(value);
        return this;
    }

    //
    // select
    //
    public <T> EsPage<T> select(Class<T> clz) throws IOException {
        return select(clz, null);
    }

    public <T> EsPage<T> select(Class<T> clz, String fields) throws IOException {
        if (queryMatch != null) {
            if (queryMatch.count() > 1) {
                getDslq().getOrNew("query").set("multi_match", queryMatch);
            } else {
                getDslq().getOrNew("query").set("match", queryMatch);
            }
        }

        if (PriUtils.isNotEmpty(fields)) {
            EsSource s = new EsSource();
            if (fields.startsWith("!")) {
                s.excludes(fields.substring(1).split(","));
            } else {
                s.includes(fields.split(","));
            }
            getDslq().set("_source", s.oNode);
        }

        EsCommand cmd = new EsCommand();
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = getDslq().toJson();
        cmd.path = String.format("/%s/_search", indiceName);


        String json = context.execAsBody(cmd);

        ONode oHits = ONode.loadStr(json).get("hits");

        long total = oHits.get("total").get("value").getLong();
        double max_score = oHits.get("oHits").getDouble();

        oHits.get("hits").forEach(n -> {
            n.setAll(n.get("_source"));
        });

        List<T> list = oHits.get("hits").toObjectList(clz);

        return new EsPage<>(total, max_score, list);
    }


    //
    // selectByIds
    //
    public <T> List<T> selectByIds(Class<T> clz, List<String> docIds) throws IOException {
        try {

            ONode oNode = new ONode();
            oNode.getOrNew("query").getOrNew("ids").getOrNew("values").addAll(docIds);

            EsCommand cmd = new EsCommand();
            cmd.method = PriWw.method_post;
            cmd.dslType = PriWw.mime_json;
            cmd.dsl = oNode.toJson();
            cmd.path = String.format("/%s/_search", indiceName);

            String json = context.execAsBody(cmd);

            ONode oHits = ONode.loadStr(json).get("hits");

            oHits.get("hits").forEach(n -> {
                n.setAll(n.get("_source"));
            });

            return oHits.get("hits").toObjectList(clz);
        } catch (NoExistException e) {
            return null;
        }
    }

    //
    // selectById
    //
    public <T> T selectById(Class<T> clz, String docId) throws IOException {
        try {
            EsCommand cmd = new EsCommand();
            cmd.method = PriWw.method_get;
            cmd.path = String.format("/%s/_doc/%s", indiceName, docId);

            String tmp = context.execAsBody(cmd);

            ONode oItem = ONode.loadStr(tmp);
            oItem.setAll(oItem.get("_source"));

            return oItem.toObject(clz);
        } catch (NoExistException e) {
            return null;
        }
    }


    //
    // delete
    //

    public String delete() throws IOException {
        if (queryMatch != null) {
            if (queryMatch.count() > 1) {
                getDslq().getOrNew("query").set("multi_match", queryMatch);
            } else {
                getDslq().getOrNew("query").set("match", queryMatch);
            }
        }

        String dsl = getDslq().toJson();

        EsCommand cmd = new EsCommand();
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = getDslq().toJson();;
        cmd.path = String.format("/%s/_delete_by_query", indiceName);

        String tmp = context.execAsBody(cmd);

        return tmp;
    }


    public boolean deleteById(String docId) throws IOException {
        EsCommand cmd = new EsCommand();
        cmd.method = PriWw.method_delete;
        cmd.path = String.format("/%s/_doc/%s", indiceName, docId);


        try {
            context.execAsBody(cmd);
            return true;
        } catch (NoExistException e) {
            return true;
        }
    }
}