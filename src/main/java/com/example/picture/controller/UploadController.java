package com.example.picture.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.picture.mapper.ImageMapper;
import com.example.picture.pojo.Image;
import com.example.picture.service.ImageService;
import com.example.picture.util.FileUtil;
import com.example.picture.util.Helper;
import com.example.picture.util.ResultResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private ImageService imageService;

    @Resource
    private Environment environment;

    @GetMapping("")
    public String upload(Model model) {
        model.addAttribute("upload");
        return "upload";
    }

    @ResponseBody
    @PostMapping("/files")
    public Object test(MultipartFile[] files) throws IOException, NoSuchAlgorithmException {
        System.out.println("文件的个数:" + files.length);
        ArrayList<String> list = new ArrayList<>();
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            String s = ".jpg";
            if (filename != null) {
                s = filename.substring(filename.lastIndexOf('.'));
            }
            String user = StpUtil.getLoginIdAsString();
            String sha256 = Helper.getSHA256(file);

            String[] image = imageService.saveImage(user, s, sha256, "");
            list.add(image[1]);
            if(image[0].equals("1")){
                FileUtil.save(file, image[1], environment.getProperty("image.save.file"));
            }
        }
        return ResultResponse.success(list);
    }
}
