package features;

import org.junit.jupiter.api.Test;
import org.noear.esearchx.EsContext;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonTest;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/26 created
 */
@SolonTest
public class Test5AggsSelect {
  final String indice = "test-user_log";

  @Inject("${test.esx}")
  EsContext context;

  // EsContext context = new EsContext("eshost:30480"); //直接实例化

  @Test
  public void test_sum() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.sum("level"))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.get("level_sum").get("value").getDouble()) > 0;
  }

  @Test
  public void test_avg() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.avg("level"))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.get("level_avg").get("value").getDouble()) > 0l;
  }

  @Test
  public void test_avg2() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.avg("level", "level"))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.get("level").get("value").getDouble()) > 0l;
  }

  @Test
  public void test_min() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.min("level"))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.get("level_min").get("value").getDouble()) > 0l;
  }

  @Test
  public void test_min2() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.min("level", "min"))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.get("min").get("value").getDouble()) > 0l;
  }

  @Test
  public void test_max() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.max("level"))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.get("level_max").get("value").getDouble()) > 0l;
  }

  @Test
  public void test_count() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.count("log_id"))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.get("log_id_count").get("value").getLong()) > 0;
  }

  @Test
  public void test_percentiles() throws Exception {
    ONode oNode =
        context
            .indice(indice)
            .where(c -> c.range("level", r -> r.gte(3)))
            .limit(0)
            .aggs(a -> a.percentiles("log_id", new Integer[] {50, 90}))
            .selectAggs();

    System.out.println(oNode.toJson());

    assert (oNode.select("log_id_percentiles.values['50.0']").getLong()) > 0;
  }

  @Test
  public void test_terms() throws Exception {
    String tmp = context.indice(indice).limit(0).aggs(a -> a.terms("level")).selectJson();

    System.out.println(tmp);
  }

  @Test
  public void test_stats() throws Exception {
    String tmp = context.indice(indice).limit(0).aggs(a -> a.stats("level")).selectAggs().toJson();

    System.out.println(tmp);
  }

  @Test
  public void test_extendedStats() throws Exception {
    String tmp =
        context.indice(indice).limit(0).aggs(a -> a.extendedStats("level")).selectAggs().toJson();

    System.out.println(tmp);
  }

  @Test
  public void test_terms_topHits() throws Exception {
    String tmp =
        context.indice(indice).limit(0).aggs(a -> a.terms("level").topHits(1)).selectJson();

    System.out.println(tmp);
  }

  @Test
  public void test_terms_aggs_topHits() throws Exception {
    String tmp =
        context
            .indice(indice)
            .limit(0)
            .aggs(
                a ->
                    a.terms("level", t -> t.size(20))
                        .aggs(a1 -> a1.topHits(2, s -> s.addByAes("log_fulltime"))))
            .selectJson();

    System.out.println(tmp);
  }

  @Test
  public void test_terms_aggs_percentiles_age() throws Exception {
    String tmp =
        context
            .indice(indice)
            .limit(0)
            .aggs(
                a ->
                    a.terms("tag")
                        .aggs(a1 -> a1.percentiles("level", new Integer[] {50, 90}).avg("level")))
            .selectAggs()
            .toJson();

    System.out.println(tmp);
  }

  @Test
  public void test_terms_aggs_percentilesRank_age() throws Exception {
    String tmp =
        context
            .indice(indice)
            .limit(0)
            .aggs(
                a ->
                    a.terms("tag")
                        .aggs(
                            a1 -> a1.percentilesRank("level", new Integer[] {50, 90}).avg("level")))
            .selectAggs()
            .toJson();

    System.out.println(tmp);
  }

  @Test
  public void test_cardinality() throws Exception {
    String tmp = context.indice(indice).limit(0).aggs(a -> a.cardinality("level")).selectJson();

    System.out.println(tmp);
  }

  @Test
  public void test_range() throws Exception {
    String tmp =
        context
            .indice(indice)
            .limit(0)
            .aggs(a -> a.range("level", r -> r.add(null, 3).add(3, 4).add(4, null)))
            .selectJson();

    System.out.println(tmp);
  }

  @Test
  public void test_filter_term() throws Exception {
    String tmp =
        context
            .indice(indice)
            .where(w -> w.term("tag", "list1"))
            .limit(0)
            .aggs(a -> a.filter(f -> f.term("level", 3)))
            .selectJson();

    System.out.println(tmp);
  }

  @Test
  public void test_histogram() throws Exception {
    String tmp =
        context
            .indice(indice)
            .limit(0)
            .aggs(
                a ->
                    a.histogram(
                        "level",
                        h -> {
                          h.interval(2).offset(1);
                        }))
            .selectJson();

    System.out.println(tmp);
  }

  @Test
  public void test_date_histogram() throws Exception {
    String tmp =
        context
            .indice(indice)
            .limit(0)
            .aggs(
                a ->
                    a.dateHistogram(
                        "log_fulltime",
                        d -> {
                          d.calendarInterval("1s").format("yyyy-MM-dd HH:mm:ss");
                        }))
            .selectJson();

    System.out.println(tmp);
  }
}
