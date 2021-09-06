package com.example.picture.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
}
