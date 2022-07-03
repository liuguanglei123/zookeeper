package com.chucan.zookeeperStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @Author: Yeman
 * @CreatedDate: 2022-07-02-18:12
 * @Description:
 */
@SpringBootApplication
public class ZookeeperApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(ZookeeperApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(ZookeeperApplication.class);
    }

}
