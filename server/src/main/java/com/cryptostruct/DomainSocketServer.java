package com.cryptostruct;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.parsetools.RecordParser;
import java.time.Duration;

public class DomainSocketServer extends AbstractVerticle {

  private static final String DELIMITER = "\u0000";
  public static final Duration LATENCY_LOG_INTERVAL = Duration.ofSeconds(1);

  private final SocketAddress socketAddress;

  private final LatencyMeasure latencyMeasure = new LatencyMeasure();

  public DomainSocketServer(String filePath) {
    this.socketAddress = SocketAddress.domainSocketAddress(filePath);
  }

  @Override
  public void start(final Promise<Void> startPromise) {
    vertx
        .createNetServer()
        .connectHandler(
            netSocket -> {
              System.out.printf("serving domain socket at %s%n", socketAddress);

              vertx.setPeriodic(1, timerId -> netSocket.write(System.nanoTime() + DELIMITER));

              final var recordParser = RecordParser.newDelimited(DELIMITER);
              netSocket.closeHandler(ignore -> recordParser.handler(null));
              netSocket.handler(recordParser.handler(latencyMeasure::onTextMessageReceived));
              System.out.printf("client connected %s%n", netSocket.remoteAddress());
            })
        .listen(socketAddress)
        .<Void>mapEmpty()
        .onFailure(
            throwable -> {
              throwable.printStackTrace();
              System.out.printf(
                  "failed to serve domain socket %s:%s%n",
                  socketAddress, throwable.getCause().getMessage());
            })
        .onComplete(startPromise);

    vertx.setPeriodic(LATENCY_LOG_INTERVAL.toMillis(), aLong -> latencyMeasure.onInterval());
  }
}
