package org.noear.esearchx.model;

import java.util.function.Consumer;
import org.noear.snack.ONode;

/**
 * 1.参数合法性，由使用者自己判断 2.暂不支持 Scripts，需要 runtime_mappings 的支持
 *
 * @author airhead
 * @since 1.0.25
 */
public class EsDateHistogram {
  private final ONode oNode;

  public EsDateHistogram(ONode oNode) {
    this.oNode = oNode;
  }

  /**
   * 日历间隔
   *
   * @param calendarInterval minute，1m; hour, 1h; week, 1w; month, 1M; quarter, 1q; year, 1y
   * @return
   */
  public EsDateHistogram calendarInterval(String calendarInterval) {
    oNode.set("calendar_interval", calendarInterval);
    return this;
  }

  /**
   * 固定间隔
   *
   * @param fixedInterval milliseconds (ms); seconds (s); minutes (m); hours (h); days (d)
   * @return
   */
  public EsDateHistogram fixedInterval(String fixedInterval) {
    oNode.set("fixed_interval", fixedInterval);
    return this;
  }

  public EsDateHistogram format(String format) {
    oNode.set("format", format);
    return this;
  }

  public EsDateHistogram timeZone(String timeZone) {
    oNode.set("time_zone", timeZone);
    return this;
  }

  public EsDateHistogram offset(String offset) {
    oNode.set("offset", offset);
    return this;
  }

  public EsDateHistogram keyed(boolean keyed) {
    oNode.set("keyed", keyed);
    return this;
  }

  public EsDateHistogram missing(String missing) {
    oNode.set("missing", missing);
    return this;
  }

  public EsDateHistogram minDocCount(int minDocCount) {
    oNode.set("min_doc_count", minDocCount);
    return this;
  }

  public EsDateHistogram extendedBounds(int min, int max) {
    oNode.getOrNew("extended_bounds").set("min", min).set("max", max);
    return this;
  }

  public EsDateHistogram hardBounds(int min, int max) {
    oNode.getOrNew("hard_bounds").set("min", min).set("max", max);
    return this;
  }

  /** 排序 */
  public EsDateHistogram sort(Consumer<EsSort> sort) {
    EsSort s = new EsSort(oNode.getOrNew("sort").asArray());
    sort.accept(s);
    return this;
  }
}
