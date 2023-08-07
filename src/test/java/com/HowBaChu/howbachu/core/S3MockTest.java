package com.HowBaChu.howbachu.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.HowBaChu.howbachu.core.manager.AWSFileManager;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

@Import(S3MockConfig.class)
@ActiveProfiles("test")
@SpringBootTest
public class S3MockTest {

    @Autowired
    private S3Mock s3Mock;
    @Autowired
    private AWSFileManager awsFileManager;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    void upload(){
        // given
        String path = "test.png";
        String contentType = "image/png";
        String dirName = "test";

        MockMultipartFile file = new MockMultipartFile("test", path, contentType, "test".getBytes());

        // when
        String urlPath = awsFileManager.upload(dirName,file);

        // then
        assertThat(urlPath).contains(path);
        assertThat(urlPath).contains(dirName);
    }
}
