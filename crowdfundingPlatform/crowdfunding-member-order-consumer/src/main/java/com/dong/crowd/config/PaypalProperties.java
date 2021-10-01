package com.dong.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author
 * @create 2020-11-22-6:04
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@ConfigurationProperties(prefix = "paypal")
public class PaypalProperties {

    private String clientId;
    private String clientSecret;
    private String mode;

}
