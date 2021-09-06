package com.example.picture.mapper;

import com.example.picture.dto.CreateUserDto;
import com.example.picture.dto.LoginDto;
import com.example.picture.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface UserMapper {

    String login(LoginDto user);

    User getUserInfoByUUID(String uuid);

    Boolean createUser(CreateUserDto user);

    String getUserStatusByUUID(String uuid);

    boolean checkUsername(String username);

    boolean checkNickname(String nickname);

    boolean checkEmail(String Email);

    boolean checkUsernameAndEmail(String username, String email);

    boolean checkEmailAndPassword(String email, String password);

    boolean updatePassword(String uuid, String password);

    boolean updateUsername(String uuid, String username);

    boolean updateNickname(String uuid, String nickname);

    boolean updateEmail(String uuid, String email);

    String gerEmailByUUID(String uuid);

    boolean resetPasswordByEmail(String email, String password);
}
