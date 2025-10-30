package org.noear.esearchx.model;

import java.util.function.Consumer;
import org.noear.snack4.ONode;

/**
 * @author airhead
 * @since 1.0.25
 */
public class EsHistogram {
  private final ONode oNode;

  public EsHistogram(ONode oNode) {
    this.oNode = oNode;
  }

  /**
   * 间隔
   *
   * @param interval
   * @return
   */
  public EsHistogram interval(int interval) {
    oNode.set("interval", interval);
    return this;
  }

  public EsHistogram offset(int offset) {
    oNode.set("offset", offset);
    return this;
  }

  public EsHistogram keyed(boolean keyed) {
    oNode.set("keyed", keyed);
    return this;
  }

  public EsHistogram missing(int missing) {
    oNode.set("missing", missing);
    return this;
  }

  public EsHistogram minDocCount(int minDocCount) {
    oNode.set("min_doc_count", minDocCount);
    return this;
  }

  public EsHistogram extendedBounds(int min, int max) {
    oNode.getOrNew("extended_bounds").set("min", min).set("max", max);
    return this;
  }

  public EsHistogram hardBounds(int min, int max) {
    oNode.getOrNew("hard_bounds").set("min", min).set("max", max);
    return this;
  }

  /** 排序 */
  public EsHistogram sort(Consumer<EsSort> sort) {
    EsSort s = new EsSort(oNode.getOrNew("sort").asArray());
    sort.accept(s);
    return this;
  }
}
