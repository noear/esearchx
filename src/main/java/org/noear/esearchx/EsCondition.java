package org.noear.esearchx;

import org.noear.snack.ONode;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * ElasticSearch 条件构建器
 *
 * @author noear
 * @since 1.0
 */
public class EsCondition {
    protected final ONode oNode = new ONode();
    ONode oNodeArray = null;
    String score_mode = null;


    /**
     * 设置过滤风格
     * */
    private void filterStyleSet(String name) {
        if (score_mode == null) {
            oNodeArray = oNode.getOrNew("bool").getOrNew(name).asArray();
        } else {
            //使用评分模式
            oNodeArray = oNode.getOrNew("function_score").getOrNew("query").getOrNew("bool").getOrNew(name).asArray();

            if (score_mode.length() > 0) {
                oNode.getOrNew("function_score").set("score_mode", score_mode);
            }
        }
    }

    /**
     * 设置过滤风格
     * */
    private void filterSet(String type, String field, Object value) {
        if(value == null){
            throw new IllegalArgumentException("value");
        }

        if (oNodeArray == null) {
            if (score_mode == null) {
                oNode.getOrNew(type).set(field, value);
            }else{
                //使用评分模式
                oNode.getOrNew("function_score").getOrNew("query").getOrNew(type).set(field, value);
                if (score_mode.length() > 0) {
                    oNode.getOrNew("function_score").set("score_mode", score_mode);
                }
            }
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew(type).set(field, value)));
        }
    }

    /**
     * 启用评分定制
     *
     * function_score/..
     */
    public EsCondition useScore() {
        return useScore(null);
    }

    /**
     * 启用评分定制
     *
     * function_score/..
     */
    public EsCondition useScore(String mode) {
        if (mode == null) {
            score_mode = "";
        } else {
            score_mode = null;
        }

        return this;
    }



    /**
     * 只过滤，不参与打分
     *
     * bool/filter
     */
    public EsCondition filter() {
        filterStyleSet("filter");
        return this;
    }

    /**
     * 如果有多个条件，这些条件都必须满足 and与
     *
     * bool/must
     */
    public EsCondition must() {
        filterStyleSet("must");
        return this;
    }

    /**
     * 如果有多个条件，满足一个或多个即可 or或
     *
     * bool/should
     */
    public EsCondition should() {
        filterStyleSet("should");
        return this;
    }

    /**
     * 和must相反，必须都不满足条件才可以匹配到 ！非
     *
     * bool/mustNot
     */
    public EsCondition mustNot() {
        filterStyleSet("must_not");
        return this;
    }



    /**
     * match
     */
    public EsCondition match(String field, Object value) {
        filterSet("match", field, value);
        return this;
    }

    /**
     * match_phrase
     */
    public EsCondition matchPhrase(String field, Object value) {
        filterSet("match_phrase", field, value);
        return this;
    }


    /**
     * match_phrase_prefix
     */
    public EsCondition matchPhrasePrefix(String field, Object value) {
        filterSet("match_phrase_prefix", field, value);
        return this;
    }

    /**
     * term
     */
    public EsCondition term(String field, Object value) {
        filterSet("term", field, value);
        return this;
    }


    /**
     * terms
     */
    public EsCondition terms(String field, Object... values) {
        filterSet("terms", field, new ONode().addAll(Arrays.asList(values)));
        return this;
    }

    /**
     * range
     */
    public EsCondition range(String field, Consumer<EsRange> range) {
        EsRange r = new EsRange();
        range.accept(r);

        filterSet("range", field, r.oNode);
        return this;
    }


    /**
     * prefix
     */
    public EsCondition prefix(String field, String value) {
        filterSet("prefix", field, value);
        return this;
    }

    /**
     * wildcard
     *
     * @param value *表示任意字符，?表示任意单个字符(
     */
    public EsCondition wildcard(String field, String value) {
        filterSet("wildcard", field, value);
        return this;
    }

    /**
     * regexp
     */
    public EsCondition regexp(String field, String value) {
        filterSet("regexp", field, value);
        return this;
    }


    /**
     * 添加下级条件
     * */
    public EsCondition add(Consumer<EsCondition> condition) {
        EsCondition c = new EsCondition();
        condition.accept(c);

        if (oNodeArray != null) {
            oNodeArray.add(c.oNode);
        } else {
            throw new IllegalArgumentException("Conditions lack combination types");
        }

        return this;
    }
}