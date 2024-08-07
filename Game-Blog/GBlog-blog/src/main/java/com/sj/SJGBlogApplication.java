package com.sj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// ----- Create main class -----
@SpringBootApplication
@MapperScan("/com.sj.mapper")
@EnableScheduling
@EnableSwagger2
// @ComponentScan
public class SJGBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SJGBlogApplication.class, args);
    }
}
