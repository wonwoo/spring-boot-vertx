package me.wonwoo;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import me.wonwoo.vertx.PushTcpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class SpringBootVertxApplication {

  @Autowired
  private PushTcpServer pushTcpServer;

  public static void main(String[] args) {
    SpringApplication.run(SpringBootVertxApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> Vertx.vertx().deployVerticle(pushTcpServer);
  }

  @Bean
  public PushTcpServer pushTcpServer() {
    return new PushTcpServer();
  }

  @GetMapping("/push/{sendMessage}")
  public String message(@PathVariable String sendMessage) {
    pushTcpServer.getEventBus().send("tcp.push.message", sendMessage, message ->{
      log.info("succeeded : {} , body : {}", message.succeeded(), message.result().body());
    });
    return "ok";
  }
}
