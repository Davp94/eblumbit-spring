package com.blumbit.eblumbit.services.spec;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.blumbit.eblumbit.dto.files.FileDownloadResponse;

public interface IFileService {

    File retrieveFile(String filePath);
    String createFile(MultipartFile file);
    FileDownloadResponse fileDownload(String filePath);
}
