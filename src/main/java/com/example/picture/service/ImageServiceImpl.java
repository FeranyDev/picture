package com.example.picture.service;

import com.example.picture.mapper.ImageMapper;
import com.example.picture.pojo.Image;
import com.example.picture.util.Helper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService{

    @Resource
    private ImageMapper imageMapper;

    @Override
    public List<String> getAllImages() {
        return imageMapper.getAllImages();
    }

    @Override
    public int checkImageByUserAndSHA256(String userId, String SHA256) {
        return imageMapper.checkImageByUserAndSHA256(userId, SHA256);
    }

    @Override
    public String getImage() {
        return imageMapper.getImage();
    }


    @Transactional
    public String[] saveImage(String user, String expandedName, String sha256, String tags){
        String id = null;
        try{
            id = imageMapper.getIdBySHA256(sha256);
        }catch (Exception ignored) {}
        if(id != null && !id.equals("")){
            if(imageMapper.checkContact(id, user) > 0){
                    return new String[]{"0", "已存在"};
                }else {
                    imageMapper.createContact(id, user, tags);
                    return new String[]{"0", imageMapper.getFilenameById(id)};
                }
        }else {
            id = Helper.getUUID().toString();
            String s = Helper.getUUID().toString();
            imageMapper.addImage(new Image(id, s + expandedName, sha256), user);
            imageMapper.createContact(id, user, tags);
            return new String[]{"1", s + expandedName};
        }
    }
}
