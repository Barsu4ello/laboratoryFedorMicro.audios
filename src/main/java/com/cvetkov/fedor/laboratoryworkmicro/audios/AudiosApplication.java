package com.cvetkov.fedor.laboratoryworkmicro.audios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.cvetkov.fedor.laboratoryworkmicro"})
@EnableR2dbcRepositories(basePackages = {"com.cvetkov.fedor.laboratoryworkmicro.audios"})
@EnableFeignClients
public class AudiosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AudiosApplication.class, args);
    }

}
