package com.cryptostruct;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.SocketAddress;

public class DomainSocketClient extends AbstractVerticle {

  private static final String DELIMITER = "\u0000";

  private final SocketAddress socketAddress;

  public DomainSocketClient(String filePath) {
    this.socketAddress = SocketAddress.domainSocketAddress(filePath);
  }

  @Override
  public void start(final Promise<Void> startPromise) {
    vertx
        .createNetClient()
        .connect(socketAddress)
        .onSuccess(
            netSocket -> {
              System.out.printf("connected to %s%n", netSocket.remoteAddress());

              vertx.setPeriodic(
                  1,
                  timerId -> {
                    netSocket.write(System.nanoTime() + DELIMITER);
                  });
            })
        .onFailure(
            throwable -> {
              System.out.printf("failed to connect to %s%n", throwable.getMessage());
            });
  }
}
