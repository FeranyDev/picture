package com.example.picture.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class Helper {
    /**
     * 获取ip包括v4,v6
     *
     * @param request
     * @return IP
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String s : ips) {
                if (!("unknown".equalsIgnoreCase(s))) {
                    ip = s;
                    break;
                }
            }
        }
        if (ip.equals("127.0.0.1")) {
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (inetAddress != null) {
                ip = inetAddress.getHostAddress();
            }
        }
        return ip;
    }

    /**
     * 传入文本内容，返回 SHA-256 串
     *
     * @param strText 明文文本
     * @return 加密文本
     */
    public static String SHA256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    /**
     * 传入文本内容，返回 SHA-512 串
     *
     * @param strText 明文文本
     * @return 加密文本
     */
    public static String SHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }

    /**
     * md5加密
     *
     * @param strText 明文文本
     * @return 加密文本
     */
    public static String MD5(String strText) {
        return SHA(strText, "MD5");
    }

    /**
     * 字符串 SHA 加密
     *
     * @param strText
     * @param strType
     * @return
     */
    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte[] byteBuffer = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (byte b : byteBuffer) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    /**
     * 产生随机字符串
     *
     * @param length 长度
     * @return
     */

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

    /**
     * UUID
     *
     * @return
     */

    public static UUID getUUID() {
        return UUID.fromString(UUID.randomUUID().toString());
    }

    public static String myKey() {
        return "%DW<D!CL#NW8>k3-2N7C}gF9{-vr^(@Q@:dq|z/_bX=,fy>L1KcS7hJOIvC-lPw*Ou0|4!F}p@%/<t{?QNL!lK[=?f,UR1j4}6xAzV*(Lo$); 0FM}W<+_+|^^CU9m|L";
    }

    public static String getSHA256(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = file.getInputStream();
        String hash = getHash(inputStream, "SHA-256");
        inputStream.close();
        return hash;
    }

    private static String getHash(InputStream inputStream, String hashType) throws NoSuchAlgorithmException, IOException {
        byte[] bytes = new byte[1024];
        MessageDigest instance = MessageDigest.getInstance(hashType);
        int i;
        while ((i = inputStream.read(bytes)) > 0){
            instance.update(bytes, 0, i);
        }
        return toHexString(instance.digest());
    }

    private static String toHexString(byte[] bytes){
        StringBuilder stringBuffer = new StringBuilder();
        for (byte b : bytes) {
            stringBuffer.append(Integer.toHexString(b & 0xFF));
        }
        return stringBuffer.toString();
    }


}