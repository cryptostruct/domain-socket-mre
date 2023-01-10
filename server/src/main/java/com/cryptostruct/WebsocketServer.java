package com.cryptostruct;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import java.time.Duration;

public class WebsocketServer extends AbstractVerticle {
  public static final Duration LATENCY_LOG_INTERVAL = Duration.ofSeconds(1);
  public final String endpoint;
  public final int port;
  private final LatencyMeasure latencyMeasure = new LatencyMeasure();

  public WebsocketServer(int port, String endpoint) {
    this.port = port;
    this.endpoint = endpoint;
  }

  @Override
  public void start(final Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    router
        .route(endpoint)
        .handler(
            routingContext -> {
              System.out.println("connect request " + routingContext.mountPoint());
              routingContext
                  .request()
                  .toWebSocket()
                  .onSuccess(
                      serverWebSocket -> {
                        serverWebSocket.handler(latencyMeasure::onTextMessageReceived);
                      })
                  .onFailure(
                      throwable ->
                          System.out.printf("failed to listen %s%n", throwable.getMessage()));
            });

    final var httpOptions =
        new HttpServerOptions()
            .setMaxWebSocketMessageSize(Integer.MAX_VALUE)
            .setMaxWebSocketFrameSize(Integer.MAX_VALUE)
            .setPort(port);

    vertx.setPeriodic(LATENCY_LOG_INTERVAL.toMillis(), timerId -> latencyMeasure.onInterval());

    vertx
        .createHttpServer(httpOptions)
        .requestHandler(router)
        .listen(port)
        .<Void>mapEmpty()
        .onComplete(startPromise);
  }
}
