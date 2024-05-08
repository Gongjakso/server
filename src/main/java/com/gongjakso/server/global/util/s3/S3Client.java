package com.gongjakso.server.global.util.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Client {

    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.baseUrl}")
    private String baseUrl;

    public String upload(MultipartFile multipartFile, String dirName) {
        // Validation
        if(multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }

        // Business Logic
        String uuid = UUID.randomUUID().toString();
        String imageUrl = dirName + "/" + uuid + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        // Check File upload
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, imageUrl, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Response
        return baseUrl + imageUrl;
    }

    public void delete(String imageUrl) {
        // Validation
        String fileName = imageUrl.substring(baseUrl.length()); // baseUrl 제거
        if(!amazonS3Client.doesObjectExist(bucket, fileName)) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }

        // Business Logic
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
