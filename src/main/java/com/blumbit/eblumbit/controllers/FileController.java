package com.blumbit.eblumbit.controllers;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.services.spec.IFileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    @GetMapping
    public ResponseEntity<Resource> retrieveFile(@RequestParam(required = false) String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new com.blumbit.eblumbit.exception.ResourceNotFoundException("Ruta de archivo no válida");
        }
        File file = fileService.retrieveFile(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new com.blumbit.eblumbit.exception.ResourceNotFoundException("Archivo no encontrado");
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
}
