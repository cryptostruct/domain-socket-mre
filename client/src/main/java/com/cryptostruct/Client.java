package com.cryptostruct;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Client {

  public static final String ENDPOINT = "/api";
  public static final int PORT = 3000;

  public static final String DOMAIN_SOCKET_FILE = "/tmp/test.sock";
  public static final String WS_OPTION = "-ws";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));
    if (args.length >= 1 && WS_OPTION.equals(args[0])) {
      System.out.println("websocket");
      vertx.deployVerticle(new WebsocketClient(PORT, ENDPOINT));
    } else {
      System.out.println("native transport: " + vertx.isNativeTransportEnabled());
      vertx.deployVerticle(new DomainSocketClient(DOMAIN_SOCKET_FILE));
    }
  }
}
