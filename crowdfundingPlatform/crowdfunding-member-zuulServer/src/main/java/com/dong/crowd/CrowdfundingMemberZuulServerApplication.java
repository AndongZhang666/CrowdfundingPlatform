package com.dong.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class CrowdfundingMemberZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdfundingMemberZuulServerApplication.class, args);
    }

}
