package com.example.dm.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.dm.dto.commons.PreSignedUrlDto;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    @Value("${aws-s3.bucket-name}")
    private String bucketName;

    private final AmazonS3 amazonS3Client;

    public List<PreSignedUrlDto.OutputDto> getPreSignedUrls(PreSignedUrlDto.InputDto inputDto) {
        String objectPrefix = inputDto.getDirectory();
        List<String> mimeTypes = inputDto.getExtensions();

        return mimeTypes.stream().map(mimeType -> {
            // 1. 확장자 유효성 검사 & 확장자 반환 (ex- .jpeg)
            String extension = getExtension(mimeType);

            // 2. UUID + 확장자 형태로 랜덤한 파일이름 설정
            String fileName = getRandomFileName(extension);

            // 3. objectKey 지정
            String objectKey = getObjectKey(objectPrefix, fileName);

            // 4. pre-signed-url 생성
            URL preSignedUrl = createPreSignedUrl(objectKey);

            return new PreSignedUrlDto.OutputDto(extension, preSignedUrl.toString());
        }).collect(Collectors.toList());
    }

    public URL createPreSignedUrl(String objectKey) {
        URL preSignedUrl = null;
        try {
            // Set the pre-signed URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the pre-signed URL.
            log.debug("Generating pre-signed URL.");

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey).withMethod(HttpMethod.PUT).withExpiration(expiration);

            preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();

        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

        return preSignedUrl;
    }

    private String getRandomFileName(String extension) {
        UUID uuid = UUID.randomUUID();
        return uuid + extension;
    }

    private String getExtension(String mimeType) {
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        String ext = "";

        try {
            MimeType type = allTypes.forName("image/" + mimeType);

            if (type.getExtension().isEmpty()) { // 일치하는 확장자가 없으면 throw
                throw new MimeTypeException(mimeType);

            } else {
                ext = type.getExtension(); // ex: .png
            }

        } catch (MimeTypeException e) {
            e.printStackTrace();
            throw new CommonException(ApiResultStatus.INVALID_EXTENSIONS, mimeType);
        }

        return ext;
    }

    private String getObjectKey(String objectPrefix, String extension) {
        return objectPrefix + '/' + extension;
    }
}
