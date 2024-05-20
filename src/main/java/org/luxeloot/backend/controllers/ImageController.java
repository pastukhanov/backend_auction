package org.luxeloot.backend.controllers;


import org.luxeloot.backend.dto.ImageUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Value("${uploaded.folder}")
    private String uploadedFolder;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadedFolder + file.getOriginalFilename());
            Files.write(path, bytes);

            ImageUploadResponse imageUploadResponse =  ImageUploadResponse.builder()
                    .url( file.getOriginalFilename())
                    .message("You successfully uploaded '" + file.getOriginalFilename() + "'")
                    .build();

            return ResponseEntity.ok(imageUploadResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload '" + file.getOriginalFilename() + "' due to " + e.getMessage());
        }
    }
}