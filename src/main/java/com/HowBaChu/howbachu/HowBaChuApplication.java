package com.HowBaChu.howbachu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class HowBaChuApplication {

    public static void main(String[] args) {
        SpringApplication.run(HowBaChuApplication.class, args);
    }

}
