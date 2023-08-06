package com.HowBaChu.howbachu.core.manager;

import com.HowBaChu.howbachu.core.factory.YamlLoadFactory;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@PropertySource(value = {"classpath:application-s3.yml"}, factory = YamlLoadFactory.class)
@Slf4j
@Component
public class AWSFileManager implements FileManager {

    private final AmazonS3 amazonS3;

    @Value(value = "${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(String path, MultipartFile multipartFile) {

        ObjectMetadata objectMetadata = metaDataInstance(multipartFile);
        String fileUrl = makeFileUrl(path, multipartFile);

        try {
            amazonS3.putObject(new PutObjectRequest(
                bucket, fileUrl, multipartFile.getInputStream(), objectMetadata));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_NOT_EXIST);
        }
        return getFileLocation(multipartFile.getOriginalFilename()) + fileUrl;
    }

    @Override
    public boolean delete(String fileName) {
        /*if (!amazonS3.doesObjectExist(bucket, fileName)) {
            throw new AmazonS3Exception(fileName + " 은(는) 존재하지 않는 이미지 입니다");
        }
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));*/
        return true;
    }

    @Override
    public void download(String filePath, String fileName) {

    }

    @Override
    public String getFileLocation(String key) {
        return amazonS3.getUrl(bucket, key).toString().split("com")[0] + "com" + File.separator;
    }

    private static String makeFileUrl(String path, MultipartFile multipartFile) {
        String extension = getExtension(multipartFile);
        String fileName = UUID.randomUUID() + "." + extension;
        return path + File.separator + fileName;
    }

    private static String getExtension(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    private static ObjectMetadata metaDataInstance(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        return objectMetadata;
    }


}
