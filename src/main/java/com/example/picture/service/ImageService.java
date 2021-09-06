package com.example.picture.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {

    List<String> getAllImages();

    int checkImageByUserAndSHA256(String userId, String SHA256);

    String getImage();

    String[] saveImage(String user, String expandedName, String sha256, String tags);
}
