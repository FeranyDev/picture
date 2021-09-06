package com.example.picture.util;


import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FileUtil {


    public static String save(MultipartFile file, String filename, String filePath) {
        return saveFile(file, filename, filePath);
    }

    private static String saveFile(MultipartFile file, String filename, String filePath) {
        if (file.isEmpty()) {
            return "false";
        }
        if (filename == null || filename.equals("")) {
            filename = file.getOriginalFilename(); //获取上传文件原来的名称
        }
        File temp = new File(filePath);
        if (!temp.exists()) {
            if (!temp.mkdirs()) {
                return "false";
            }
        }
        File localFile = new File(filePath + filename);
        if (localFile.exists()) {
            localFile = new File(filePath + Helper.getRandomString(6) + filename);
        }
        try {
            file.transferTo(localFile); //把上传的文件保存至本地
            System.out.println(file.getOriginalFilename() + " 上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public static Object downloadFile(String realPath, String fileName, HttpServletResponse response) {
        // 如果文件名不为空，则进行下载
        if (fileName != null) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            //设置文件路径
            File file = new File(realPath, fileName);
            // 如果文件名存在，则进行下载
            if (file.exists()) {

                // 实现文件下载
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    byte[] buffer = new byte[1024];
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download the song successfully!");
                } catch (Exception e) {
                    System.out.println("Download the song failed!");
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}
