package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import web.config.BotConfig;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties({BotConfig.class})
@EnableFeignClients
public class BotApplication {

    public static void main(String[] args) throws IOException {

        ApplicationContext context = SpringApplication.run(BotApplication.class, args);
    }
}