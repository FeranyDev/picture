package com.example.picture.service;

import com.example.picture.dto.CreateUserDto;
import com.example.picture.dto.LoginDto;
import com.example.picture.pojo.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    String login(LoginDto user);

    User getUserInfoByUUID(String uuid);

    Boolean createUser(CreateUserDto user);

    String getUserStatusByUUID(String uuid);

    Boolean checkInfo(String key, String value);

    Boolean checkUsernameAndEmail(String username, String email);

    Boolean checkEmailAndPassword(String email, String password);

    Boolean updateByUUID(String uuid, String value, String token);

    String getInfoByUUID(String value, String token);

    Boolean resetPasswordByEmail(String password, String email);
}
