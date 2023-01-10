package com.cryptostruct;

import io.vertx.core.buffer.Buffer;

public class LatencyMeasure {

  public final RollingAverage rollingAverage = new RollingAverage();

  public void onInterval() {
    System.out.printf(
        "average %f micros over %d %n", rollingAverage.average() * 0.001, rollingAverage.size());
  }

  public void onTextMessageReceived(Buffer buffer) {
    String text = buffer.toString();
    long currentTime = System.nanoTime();
    long sendTime = Long.valueOf(text);
    long delay = currentTime - sendTime;
    rollingAverage.add(delay);
  }
}
