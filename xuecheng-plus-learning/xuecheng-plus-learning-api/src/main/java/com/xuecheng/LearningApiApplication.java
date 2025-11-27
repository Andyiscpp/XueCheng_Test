package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.spring4all.swagger.EnableSwagger2Doc;

@EnableSwagger2Doc
@EnableFeignClients(basePackages={"com.xuecheng.*.feignclient"})
@SpringBootApplication
public class LearningApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningApiApplication.class, args);
    }

}
