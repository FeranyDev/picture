package com.example.picture.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID uuid;
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

}
