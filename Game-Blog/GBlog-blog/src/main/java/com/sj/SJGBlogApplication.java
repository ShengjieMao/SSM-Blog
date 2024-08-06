package com.sj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// ----- Create main class -----
@SpringBootApplication
@MapperScan("/com.sj.mapper")
@EnableScheduling
// @ComponentScan
public class SJGBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SJGBlogApplication.class, args);
    }
}
