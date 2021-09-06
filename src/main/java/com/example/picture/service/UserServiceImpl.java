package com.example.picture.service;

import com.example.picture.dto.CreateUserDto;
import com.example.picture.dto.LoginDto;
import com.example.picture.mapper.UserMapper;
import com.example.picture.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String login(LoginDto user) {
        return userMapper.login(user);
    }

    @Override
    public User getUserInfoByUUID(String uuid) {
        return userMapper.getUserInfoByUUID(uuid);
    }

    @Override
    public Boolean createUser(CreateUserDto user) {
        return userMapper.createUser(user);
    }

    @Override
    public String getUserStatusByUUID(String uuid) {
        return userMapper.getUserStatusByUUID(uuid);
    }

    @Override
    public Boolean checkInfo(String key, String value) {
        switch (key) {
            case "username":
                return userMapper.checkUsername(value);
            case "nickname":
                return userMapper.checkNickname(value);
            case "email":
                return userMapper.checkEmail(value);
            default:
                return false;
        }
    }

    @Override
    public Boolean checkUsernameAndEmail(String username, String email) {
        return userMapper.checkUsernameAndEmail(username, email);
    }

    @Override
    public Boolean checkEmailAndPassword(String email, String password) {
        return userMapper.checkEmailAndPassword(email, password);
    }

    @Override
    public Boolean updateByUUID(String uuid, String value, String token) {
        switch (token) {
            case "username":
                return userMapper.updateUsername(uuid, value);
            case "nickname":
                return userMapper.updateNickname(uuid, value);
            case "email":
                return userMapper.updateEmail(uuid, value);
            case "password":
                return userMapper.updatePassword(uuid, value);
            default:
                return null;
        }
    }

    @Override
    public String getInfoByUUID(String uuid, String token) {
        switch (token) {
            case "email":
                return userMapper.gerEmailByUUID(uuid);
            default:
                return null;
        }
    }

    @Override
    public Boolean resetPasswordByEmail(String password, String email) {
        return userMapper.resetPasswordByEmail(email, password);
    }
}
