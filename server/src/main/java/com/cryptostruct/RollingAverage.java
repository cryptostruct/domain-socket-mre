package com.cryptostruct;

import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class RollingAverage {

  private final Queue<Long> data = new CircularFifoQueue<>(2000);

  public void add(long l) {
    data.add(l);
  }

  public double average() {
    int size = data.size();
    if (size == 0) {
      return 0.0;
    }
    long acc = 0L;
    for (long datum : data) {
      acc = acc + datum;
    }
    return acc / (double) size;
  }

  public long size() {
    return data.size();
  }
}
