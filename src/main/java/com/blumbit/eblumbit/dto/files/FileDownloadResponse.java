package com.blumbit.eblumbit.dto.files;

import java.io.File;

import org.springframework.core.io.Resource;

import lombok.*;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class FileDownloadResponse {
    private Resource resource;
    private String contentType;
    private String fileName;
}
