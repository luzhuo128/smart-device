package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *
 * </p>
 *
 * @author lz
 * @date 2023/7/10 11:06
 */
@SpringBootApplication
@MapperScan("com.v1.dao")
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
