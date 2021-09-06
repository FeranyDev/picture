package com.example.picture.mapper;

import com.example.picture.pojo.Session;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface SessionMapper {

    boolean login(Session session);

    boolean logout(String token);

}
