package com.example.picture.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.picture.dto.CreateUserDto;
import com.example.picture.dto.JwtDto;
import com.example.picture.dto.LoginDto;
import com.example.picture.service.MailService;
import com.example.picture.service.UserService;
import com.example.picture.util.Helper;
import com.example.picture.util.JwtUtils;
import com.example.picture.util.ResultResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;


    /**
     * 注册账户
     */

    @ResponseBody
    @PostMapping("/create")
    public ResultResponse createUser(@Valid CreateUserDto user, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jwt = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                jwt = cookie.getValue();
            }
        }
        if (jwt != null) {
            JwtDto data = JwtUtils.getData(jwt);
            String s = data.getSubject();
            if (!user.getToken().equals(s)) {
                return ResultResponse.error("验证码错误");
            }
            System.out.println(user);
            if (userService.createUser(user)) {
                return ResultResponse.success("注册成功");
            }
        }
        return ResultResponse.error("注册失败");
    }

    /**
     * 获取验证码
     */

    @ResponseBody
    @PostMapping("/verification")
    public ResultResponse verification(HttpServletResponse response, String email) {
        if (StpUtil.isLogin()) {
            String uuid = StpUtil.getLoginIdAsString();
            email = userService.getInfoByUUID(uuid, "email");
            if (uuid.equals("")) {
                return ResultResponse.error("认证失败请重新登录");
            } else {
                return sendCode(email, response);
            }
        }
        if (email == null || email.equals("")) {
            return ResultResponse.error("邮箱为空");
        }
        if (userService.checkInfo("email", email)) {
            return ResultResponse.error("此邮箱不可用于注册");
        }
        return sendCode(email, response);
    }

    /**
     * 修改信息
     */
    @ResponseBody
    @PostMapping("/change")
    public ResultResponse change(CreateUserDto user, HttpServletRequest request) {
        String uuid = StpUtil.getLoginIdAsString();
        if (uuid == null || uuid.equals("")) {
            return ResultResponse.error("请重新登录");
        }
        String tmp = "";
        if (user.getEmail() != null && !user.getEmail().equals("")) {
            JwtDto jwtDto = getToken(request);
            String s = jwtDto.getSubject();
            String email = jwtDto.getIssuer();
            if (!user.getToken().equals(s) && !user.getEmail().equals(email)) {
                return ResultResponse.error("验证码错误");
            }
            if (userService.updateByUUID(uuid, user.getEmail(), "email")) {
                tmp += "邮箱 ";
            }
        }
        if (user.getUsername() != null && !user.getUsername().equals("")) {
            if (userService.updateByUUID(uuid, user.getUsername(), "username")) {
                tmp += "用户名 ";
            }
        }
        if (user.getNickname() != null && !user.getNickname().equals("")) {
            if (userService.updateByUUID(uuid, user.getNickname(), "nickname")) {
                tmp += "昵称 ";
            }
        }
        return ResultResponse.success(tmp.trim() + "修改成功");
    }

    /**
     * 修改密码
     */

    @ResponseBody
    @PostMapping("/changePassword")
    public ResultResponse changePassword(String oldPassword, String newPassword) {
        String uuid = StpUtil.getLoginIdAsString();
        if (userService.login(new LoginDto(uuid, oldPassword, null)) == null) {
            return ResultResponse.error("旧密码错误");
        }
        if (userService.updateByUUID(uuid, Helper.SHA256(newPassword), "password")) {
            return ResultResponse.success();
        } else {
            return ResultResponse.error("修改密码失败");
        }
    }

    /**
     * 忘记密码
     */

    @ResponseBody
    @PostMapping("/forgather")
    public ResultResponse forgather(String token, HttpServletRequest request, String password, String email) {
        String s = getToken(request).getSubject();
        if (!token.equals(s)) {
            return ResultResponse.error("验证码错误");
        }
        if (userService.resetPasswordByEmail(Helper.SHA256(password), email)) {
            return ResultResponse.success();
        } else {
            return ResultResponse.error("重置密码失败");
        }
    }

    /**
     * 数据校验
     *
     * @param key   键：username,nickname,email
     * @param value 值
     * @return Yes, No
     */
    @ResponseBody
    @PostMapping("/check")
    public ResultResponse check(@NotBlank String key, @NotBlank String value) {
        if (key.equals("username") || key.equals("nickname") || key.equals("email")) {
            return ResultResponse.error("key不合法");
        }
        if (userService.checkInfo(key, value)) {
            return ResultResponse.success("可用");
        } else {
            return ResultResponse.error("不可用");
        }
    }

    private ResultResponse sendCode(String email, HttpServletResponse response) {
        String s = Helper.getRandomString(6).toLowerCase();
        String jwt = JwtUtils.getJWT(Helper.getUUID().toString(), email, s, 30 * 60 * 1000);
        Cookie cookie = new Cookie("jwt", jwt);
        response.addCookie(cookie);
        String body = "你好！\n" +
                "\n" +
                "您的验证码为：\n" +
                s + "\n" +
                "有效时间30分钟!\n" +
                "祝您使用愉快，使用过程中您有任何问题请及时联系我们。";
        mailService.sendSimpleMail(email, "YogurtCloud", body);
        HashMap<String, String> map = new HashMap<>();
        map.put("jwt", jwt);
        return ResultResponse.success(map);
    }

    private JwtDto getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jwt = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                jwt = cookie.getValue();
            }
        }
        return JwtUtils.getData(jwt);
    }

}
