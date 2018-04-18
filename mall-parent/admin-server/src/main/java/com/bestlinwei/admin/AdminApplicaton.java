package com.bestlinwei.admin;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class,DataSourceAutoConfiguration.class})
@EnableAdminServer
public class AdminApplicaton {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplicaton.class, args);
    }
}































































