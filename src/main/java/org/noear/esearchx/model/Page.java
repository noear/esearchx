package org.noear.esearchx.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author noear 2021/10/22 created
 */
public class Page<T> implements Serializable {
    final long total;
    final List<T> list;
    final double maxScore;

    public long getTotal() {
        return total;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public List<T> getList() {
        return list;
    }

    public int getListSize() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public Page(long total, double maxScore, List<T> list) {
        this.total = total;
        this.maxScore = maxScore;
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", list=" + list +
                ", maxScore=" + maxScore +
                '}';
    }
}
