package com.example.picture.service;

import com.example.picture.mapper.SessionMapper;
import com.example.picture.pojo.Session;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SessionServiceImpl implements SessionService {

    @Resource
    private SessionMapper sessionMapper;


    @Override
//    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public boolean login(Session session) {
        return sessionMapper.login(session);
    }

}
