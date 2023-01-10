package com.cryptostruct;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Server {
  public static final String WS_ENDPOINT = "/api";
  public static final int WS_PORT = 3000;
  public static final String DOMAIN_SOCKET_FILE = "/tmp/test.sock";

  /** Command-line option to enable websocket server. */
  public static final String WS_OPTION = "-ws";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));

    if (args.length >= 1 && WS_OPTION.equals(args[0])) {
      System.out.println("server mode: websocket");
      vertx.deployVerticle(new WebsocketServer(WS_PORT, WS_ENDPOINT));
    } else {
      System.out.println("server mode: domain socket");
      vertx.deployVerticle(new DomainSocketServer(DOMAIN_SOCKET_FILE));
    }
  }
}
