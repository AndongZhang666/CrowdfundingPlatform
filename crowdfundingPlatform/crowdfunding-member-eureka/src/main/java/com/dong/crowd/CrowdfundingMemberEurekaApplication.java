package com.dong.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class CrowdfundingMemberEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdfundingMemberEurekaApplication.class, args);
    }

}
