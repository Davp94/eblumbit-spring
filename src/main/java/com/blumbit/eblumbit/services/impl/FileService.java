package com.blumbit.eblumbit.services.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blumbit.eblumbit.dto.files.FileDownloadResponse;
import com.blumbit.eblumbit.services.spec.IFileService;

import jakarta.annotation.PostConstruct;

@Service 
public class FileService implements IFileService {

    @Value("${storage.location}")
    private String filePath;

    private Path fileStorageLocation;

    @PostConstruct 
    public void init() {
        try {
            this.fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
            Files.createDirectories(fileStorageLocation);
        } catch (Exception e) {
            throw new RuntimeException("No se puede crear el directorio");   
        }
    }
    
    @Override
    public File retrieveFile(String filePath) {
       try {
            Path fileSavedPath = fileStorageLocation.resolve(filePath).normalize();
            if(!fileSavedPath.startsWith(fileStorageLocation)) {
                throw new RuntimeException("Ruta de archivo no válida");
            }
            File file = fileSavedPath.toFile();
            return file;
       } catch (Exception e) {
            throw new RuntimeException("Error recuperando el archivo");
       }
    }

    @Override
    public String createFile(MultipartFile file) {
       String fileName = generateUniqueFileName(file.getOriginalFilename());
       try {
          Path targetLocation = fileStorageLocation.resolve(fileName);
          Files.copy(file.getInputStream(), targetLocation);
          return fileName;
       } catch (Exception e) {
            throw new RuntimeException("Error al crear archivo", e);
       }
    }

    @Override
    public FileDownloadResponse fileDownload(String filePath) {
        try {
            File file = retrieveFile(filePath);
            Path path = Paths.get(file.getAbsolutePath());
            Resource resource = new UrlResource(path.toUri());
            if(!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Archivo no encontrado o no legible");
            }
            String contentType = "";
            try {
                contentType = Files.probeContentType(path);
            } catch (Exception e) {
                contentType = "application/octet-stream";
            }
            return FileDownloadResponse.builder()
            .fileName(file.getName())
            .contentType(contentType)
            .resource(resource)
            .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        if(originalFileName == null){
            originalFileName = "file";
        }
        String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String uuid = UUID.randomUUID().toString().substring(0,8);

        return timestamp + "_"+uuid+"_"+originalFileName;
    }

}
