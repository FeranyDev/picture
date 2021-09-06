package com.example.picture.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private UUID id;
    private String user_id;
    private String value;
    private String status;
    private String ip;
    private Date expiredAt;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
