package org.noear.esearchx.model;

import java.util.Arrays;
import java.util.function.Consumer;
import org.noear.snack.ONode;

/**
 * @author noear
 * @since 1.0.3
 */
public class EsAggs {
  private final ONode oNode;
  private ONode lastLevel2Node;

  private String oNodeAsField;

  public EsAggs(ONode oNode) {
    this.oNode = oNode;
  }

  public EsAggs asField(String asField) {
    oNode.rename(oNodeAsField, asField);
    return this;
  }

  /** sum，求合 */
  public EsAggs sum(String field) {
    funSet(field + "_sum", field, "sum");
    return this;
  }

  /** sum，求合（支持别名） */
  public EsAggs sum(String field, String asFiled) {
    funSet(asFiled, field, "sum");
    return this;
  }

  //
  // ============ Metrics =============
  //

  /** avg，求平均值 */
  public EsAggs avg(String field) {
    funSet(field + "_avg", field, "avg");
    return this;
  }

  /** avg，求平均值（支持别名） */
  public EsAggs avg(String field, String asField) {
    funSet(asField, field, "avg");
    return this;
  }

  /** max，求最大值 */
  public EsAggs max(String field) {
    funSet(field + "_max", field, "max");
    return this;
  }

  /** max，求最大值（支持别名） */
  public EsAggs max(String field, String asField) {
    funSet(asField, field, "max");
    return this;
  }

  /** min，求最小值 */
  public EsAggs min(String field) {
    funSet(field + "_min", field, "min");
    return this;
  }

  /** min，求最小值（支持别名） */
  public EsAggs min(String field, String asField) {
    funSet(asField, field, "min");
    return this;
  }

  /** count，值计数 */
  public EsAggs count(String field) {
    funSet(field + "_count", field, "value_count");
    return this;
  }

  /** count，值计数（支持别名） */
  public EsAggs count(String field, String asField) {
    funSet(asField, field, "value_count");
    return this;
  }

  /** top_hits，每一个聚合Bucket里面仅返回指定顺序的前N条数据。 */
  public EsAggs topHits(int size) {
    return topHits(size, null);
  }

  public EsAggs topHits(int size, Consumer<EsSort> sort) {
    ONode top_hits = oNode.getOrNew("$topHits").getOrNew("top_hits");

    top_hits.set("size", size);

    if (sort != null) {
      EsSort s = new EsSort(top_hits.getOrNew("sort").asArray());
      sort.accept(s);
    }

    return this;
  }

  /** cardinality，先去重再计数 */
  public EsAggs cardinality(String field) {
    funSet(field + "_cardinality", field, "cardinality");
    return this;
  }

  /** cardinality，先去重再计数（支持别名） */
  public EsAggs cardinality(String field, String asField) {
    funSet(asField, field, "cardinality");
    return this;
  }

  /** percentiles，多值聚合求百分比 */
  public EsAggs percentiles(String field, Number[] percents) {
    ONode oNode1 = getLevel2Node(field + "_percentiles").getOrNew("percentiles");
    oNode1.set("field", field);
    oNode1.getOrNew("percents").addAll(Arrays.asList(percents));
    return this;
  }

  /** percentiles，多值聚合求百分比（支持别名） */
  public EsAggs percentiles(String field, String asField, Number[] percents) {
    ONode oNode1 = getLevel2Node(asField).getOrNew("percentiles");
    oNode1.set("field", field);
    oNode1.getOrNew("percents").addAll(Arrays.asList(percents));
    return this;
  }

  /** percentiles rank */
  public EsAggs percentilesRank(String field, Number[] values) {
    ONode oNode1 = getLevel2Node(field + "_percentilesRank").getOrNew("percentile_ranks");
    oNode1.set("field", field);
    oNode1.getOrNew("values").addAll(Arrays.asList(values));
    return this;
  }

  /** percentiles rank（支持别名） */
  public EsAggs percentilesRank(String field, String asField, Number[] values) {
    ONode oNode1 = getLevel2Node(asField).getOrNew("percentile_ranks");
    oNode1.set("field", field);
    oNode1.getOrNew("values").addAll(Arrays.asList(values));
    return this;
  }

  /** extended_stats */
  public EsAggs extendedStats(String field) {
    funSet(field + "_extendedStats", field, "extended_stats");
    return this;
  }

  /** extended_stats（支持别名） */
  public EsAggs extendedStats(String field, String asField) {
    funSet(asField, field, "extended_stats");
    return this;
  }

  /** stats */
  public EsAggs stats(String field) {
    funSet(field + "_stats", field, "stats");
    return this;
  }

  /** stats（支持别名） */
  public EsAggs stats(String field, String asField) {
    funSet(asField, field, "stats");
    return this;
  }

  /** filter，聚合 */
  public EsAggs filter(Consumer<EsCondition> condition) {
    EsCondition c = new EsCondition(getLevel2Node("$filter").getOrNew("filter"));
    condition.accept(c);
    return this;
  }

  /** range，聚合 */
  public EsAggs range(String field, Consumer<EsRanges> ranges) {
    ONode oNode1 = getLevel2Node(field + "_range").getOrNew("range");
    oNode1.set("field", field);
    EsRanges t = new EsRanges(oNode1.getOrNew("ranges").asArray());
    ranges.accept(t);
    return this;
  }

  //
  // ============ Bucket =============
  //

  /** range，聚合 */
  public EsAggs range(String field, String asField, Consumer<EsRanges> ranges) {
    ONode oNode1 = getLevel2Node(asField).getOrNew("range");
    oNode1.set("field", field);
    EsRanges t = new EsRanges(oNode1.getOrNew("ranges").asArray());
    ranges.accept(t);
    return this;
  }

  /** terms，聚合 */
  public EsAggs terms(String field) {
    terms(field, field + "_terms");
    return this;
  }

  /** terms，聚合 */
  public EsAggs terms(String field, String asField) {
    terms(field, asField, null);
    return this;
  }

  public EsAggs terms(String field, Consumer<EsTerms> terms) {
    ONode oNode1 = getLevel2Node(field + "_terms").getOrNew("terms");
    if (field.startsWith("$")) {
      oNode1.set("script", field.substring(1));
    } else {
      oNode1.set("field", field);
    }
    if (terms != null) {
      EsTerms t = new EsTerms(oNode1);
      terms.accept(t);
    }
    return this;
  }

  public EsAggs terms(String field, String asField, Consumer<EsTerms> terms) {
    ONode oNode1 = getLevel2Node(asField).getOrNew("terms");
    if (field.startsWith("$")) {
      oNode1.set("script", field.substring(1));
    } else {
      oNode1.set("field", field);
    }
    if (terms != null) {
      EsTerms t = new EsTerms(oNode1);
      terms.accept(t);
    }
    return this;
  }

  public EsAggs dateHistogram(String field, Consumer<EsDateHistogram> dateHistogram) {
    return dateHistogram(field, field + "_date_histogram", dateHistogram);
  }

  public EsAggs dateHistogram(
      String field, String asField, Consumer<EsDateHistogram> dateHistogram) {
    ONode oNode1 = getLevel2Node(asField).getOrNew("date_histogram");
    oNode1.set("field", field);

    if (dateHistogram != null) {
      EsDateHistogram t = new EsDateHistogram(oNode1);
      dateHistogram.accept(t);
    }

    return this;
  }

  public EsAggs histogram(String field, Consumer<EsHistogram> histogram) {
    return histogram(field, field + "_histogram", histogram);
  }

  public EsAggs histogram(
      String field, String asField, Consumer<EsHistogram> histogram) {
    ONode oNode1 = getLevel2Node(asField).getOrNew("histogram");
    oNode1.set("field", field);

    if (histogram != null) {
      EsHistogram t = new EsHistogram(oNode1);
      histogram.accept(t);
    }

    return this;
  }

  /** 添加下级条件 */
  public EsAggs aggs(Consumer<EsAggs> aggs) {
    if (lastLevel2Node == null) {
      throw new IllegalArgumentException("There are no secondary nodes");
    }
    EsAggs c = new EsAggs(lastLevel2Node.getOrNew("aggs"));
    aggs.accept(c);
    return this;
  }

  private ONode getLevel2Node(String name) {
    oNodeAsField = name;
    lastLevel2Node = oNode.getOrNew(name);
    return lastLevel2Node;
  }

  private void funSet(String asField, String field, String funName) {
    getLevel2Node(asField).getOrNew(funName).set("field", field);
  }
}
