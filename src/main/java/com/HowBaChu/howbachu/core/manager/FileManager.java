package com.HowBaChu.howbachu.core.manager;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {
    String upload(String url, MultipartFile multipartFile) ;
    boolean delete(String fileName);
    void download(String filePath, String fileName);
    String getFileLocation(String avatar);
}
