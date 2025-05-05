package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import web.config.StorageConfig;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({StorageConfig.class})
public class Application {

    public static void main(String[] args) throws IOException {

        ApplicationContext context = SpringApplication.run(Application.class, args);
    }
}