package com.cryptostruct;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.WebSocketConnectOptions;

public class WebsocketClient extends AbstractVerticle {
  public static final String LOCALHOST = "localhost";
  public final String endpoint;
  public final int port;

  public WebsocketClient(int port, String endpoint) {
    this.port = port;
    this.endpoint = endpoint;
  }

  @Override
  public void start(final Promise<Void> startPromise) {
    vertx
        .createHttpClient()
        .webSocket(new WebSocketConnectOptions().setHost(LOCALHOST).setPort(port).setURI(endpoint))
        .onSuccess(
            webSocket -> {
              System.out.printf("connected to %s%n", webSocket.remoteAddress());
              vertx.setPeriodic(
                  1, timerId -> webSocket.writeTextMessage(Long.toString(System.nanoTime())));
            })
        .onFailure(
            throwable -> {
              System.out.printf("failed to connect to %s%n", throwable.getMessage());
            });
  }
}
