package com.example.picture.mapper;

import com.example.picture.pojo.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ImageMapper {

    void addImage(@Param("image") Image image, @Param("user") String user);

    String getIdBySHA256(String SHA256);

    List<String> getAllImages();

    int checkImageByUserAndSHA256(String userId, String SHA256);

    void createContact(String imageId, String user_id, String tags);

    int checkContact(String imageId, String user_id);

    String getFilenameById(String id);

    String getImage();
}
