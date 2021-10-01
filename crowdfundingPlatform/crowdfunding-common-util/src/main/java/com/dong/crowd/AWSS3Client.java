package com.dong.crowd;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Andong Zhang
 * @create 2021-06-19-0:25
 */
public class AWSS3Client {

    private AmazonS3 s3Client;
    private String location = "us-east-2";

    public AWSS3Client(String AWSAccessKeyId, String AWSSecretKeyId) {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(AWSAccessKeyId, AWSSecretKeyId);

        s3Client = AmazonS3Client.builder()
                .withRegion(location)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        System.out.println("s3 Client built");
    }

    public ResultEntity<String> upload(File tempFile, String remoteFileName, String bucketName) {

        try {
            //Create stored path in S3 for example: /bucketName/20201119
            String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());

            String bucketPath = bucketName + "/" + folderName;
            //send file to s3
            s3Client.putObject(new PutObjectRequest(bucketPath, remoteFileName, tempFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, remoteFileName);

            URL url = s3Client.generatePresignedUrl(urlRequest);

            return ResultEntity.successWithData(url.toString());

        } catch (SdkClientException e) {

            return ResultEntity.fail(e.getMessage());
        }
    }

    public ResultEntity<String> upload(InputStream tempFile, String contentType, long contentLength, String originalFileName, String bucketName) {

        try {
            //Create stored path in S3 for example: /bucketName/20201119
            String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
            // Generate the file name of the uploaded file when it is saved on the OSS server
            // Use UUID to generate file subject name
            String fileMainName = UUID.randomUUID().toString().replace("-", "");
            // Get the file extension from the original file name
            String extensionName = folderName + "/" + fileMainName + originalFileName.substring(originalFileName.lastIndexOf("."));

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(contentType);

            metadata.setContentLength(contentLength);

            //send file to s3
            s3Client.putObject(new PutObjectRequest(bucketName, extensionName, tempFile, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            //design url
            String url = "http://" + bucketName + ".s3." + location + ".amazonaws.com/" + extensionName;

            System.out.println(url);

            return ResultEntity.successWithData(url);

        } catch (SdkClientException e) {

            return ResultEntity.fail(e.getMessage());
        }
    }

}
