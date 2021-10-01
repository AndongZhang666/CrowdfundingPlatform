package com.dong.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class AWSS3Properties {
        String AWSAccessKeyId;
        String AWSSecretKeyId;
        String bucketName;
}
