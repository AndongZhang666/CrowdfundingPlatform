package com.dong.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Andong Zhang
 * @create 2021-06-19-0:25
 */

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class CrowdfundingMemberAuthenticationServerConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdfundingMemberAuthenticationServerConsumerApplication.class, args);
    }

}
