package com.naftal.gmao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GmaoApplication {

    public static void main(String[] args) {

//        SpringApplication.run(GmaoApplication.class, args);

        SpringApplicationBuilder builder = new SpringApplicationBuilder(GmaoApplication.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
    }

}
