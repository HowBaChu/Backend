package com.HowBaChu.howbachu.core.manager;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {
    String upload(String url, MultipartFile multipartFile) ;

    String getFileLocation(String avatar);
}
