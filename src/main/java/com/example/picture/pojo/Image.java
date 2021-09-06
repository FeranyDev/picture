package com.example.picture.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String id;
    private String name;
    private String sha256;
    private String status;
    private Timestamp createdAt;
    private Timestamp deletedAt;

    public Image(String id, String name, String sha256) {
        this.id = id;
        this.name = name;
        this.sha256 = sha256;
    }
}
