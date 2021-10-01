package com.dong.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Andong Zhang
 * @create 2021-06-19-0:25
 */

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aws.sms")
public class AWSSMSProperties {
    String AWSAccessKeyId;
    String AWSSecretKeyId;
}
