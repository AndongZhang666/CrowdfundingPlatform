package com.dong.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableEurekaClient
@EnableRedisHttpSession
@EnableFeignClients
@SpringBootApplication
public class CrowdfundingMemberOrderConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdfundingMemberOrderConsumerApplication.class, args);
    }

}
