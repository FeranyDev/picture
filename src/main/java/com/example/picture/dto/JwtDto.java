package com.example.picture.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    private String id;
    private String issuer;
    private String subject;
    private Long ttlMillis;
}
