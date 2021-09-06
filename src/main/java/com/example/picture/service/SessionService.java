package com.example.picture.service;

import com.example.picture.pojo.Session;
import org.springframework.stereotype.Service;


@Service
public interface SessionService {

    boolean login(Session session);

}
