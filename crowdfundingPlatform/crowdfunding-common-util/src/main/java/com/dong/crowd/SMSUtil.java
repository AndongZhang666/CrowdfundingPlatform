package com.dong.crowd;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;

public class SMSUtil {
    public static ResultEntity<String> sendSMS(
                                        // Phone number to receive the code
                                        String phoneNum,

                                        // App code
                                        String key,

                                        // String sign
                                        String secreteKey) {


        String defaultSenderID = "Andong";
        String defaultSMSType = "Promotional";
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i< 4; i++){
            int random = (int) (Math.random()*10);
            builder.append(random);
        }
        String code = builder.toString();
        String message = "[Andong] This is your Verification code: " + code;


        BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(key, secreteKey);
        Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue(defaultSenderID) //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.10") //Sets the max price to 0.50 USD.
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue(defaultSMSType) //Sets the type to promotional.
                .withDataType("String"));


        try {
            AmazonSNS snsClient = AmazonSNSClient.builder()
                    .withRegion(Regions.US_EAST_2)
                    .withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials))
                    .build();

            PublishResult result = snsClient.publish(new PublishRequest()
                    .withMessage(message)
                    .withPhoneNumber(phoneNum)
                    .withMessageAttributes(smsAttributes));

            return ResultEntity.successWithData(code);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());

        }


    }
}
