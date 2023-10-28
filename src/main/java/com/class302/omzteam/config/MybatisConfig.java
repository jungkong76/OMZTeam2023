package com.class302.omzteam.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.class302.omzteam.mybatis")
public class MybatisConfig {
    public static void main(String[] args) {
        SpringApplication.run(MybatisConfig.class, args);
    }
}
