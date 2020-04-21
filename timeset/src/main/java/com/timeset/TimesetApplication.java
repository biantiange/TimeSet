package com.timeset;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.timeset")
public class TimesetApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimesetApplication.class, args);
    }

}
