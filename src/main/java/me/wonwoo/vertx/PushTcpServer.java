package me.wonwoo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetServer;

/**
 * Created by wonwoo on 2016. 4. 23..
 */
public class PushTcpServer extends AbstractVerticle {

  private NetServer server;
  private EventBus eventBus;

  public EventBus getEventBus() {
    return vertx.eventBus();
  }

  @Override
  public void start() throws Exception {
    server = vertx.createNetServer();
    eventBus = vertx.eventBus();

    server.connectHandler(socket ->
      eventBus.consumer("tcp.push.message", message -> {
        String body = (String) message.body();
        socket.write(body);
        message.reply(body);
      })
    );
    server.listen(8888);
  }
}
