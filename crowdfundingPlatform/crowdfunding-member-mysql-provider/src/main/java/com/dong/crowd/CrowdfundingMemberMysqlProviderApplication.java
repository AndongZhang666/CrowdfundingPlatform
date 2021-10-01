package com.dong.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.dong.crowd.mapper")
@SpringBootApplication
public class CrowdfundingMemberMysqlProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdfundingMemberMysqlProviderApplication.class, args);
    }

}
