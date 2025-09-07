package com.fss.core.fssCalculation.persistance;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.io.SdkFilterInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Paths;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "fssexcelsheets";

    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1) // same as your bucket region
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    // Upload Excel file
    public void uploadFile(String keyName, File file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
        System.out.println("✅ File uploaded: " + keyName);
    }

    // Download Excel file
    public SdkFilterInputStream downloadFile(String keyName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        return s3Client.getObject(getObjectRequest);

    }
}
