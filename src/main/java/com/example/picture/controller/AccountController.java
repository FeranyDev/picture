package com.example.picture.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.example.picture.dto.LoginDto;
import com.example.picture.exception.ExceptionEnum;
import com.example.picture.pojo.Session;
import com.example.picture.service.SessionService;
import com.example.picture.service.UserService;
import com.example.picture.util.Helper;
import com.example.picture.util.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@Controller
public class AccountController {

    @Resource
    private UserService userService;
    @Resource
    private SessionService sessionService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }


    /**
     * 对于无cookie的客户端：
     * 无论是app还是小程序，其传递方式都大同小异
     * 那就是，将 token 塞到请求header里 ，格式为：{tokenName: tokenValue}
     * 以经典跨端框架 uni-app 为例：
     * http://sa-token.dev33.cn/doc/index.html#/up/not-cookie
     * 只要按照如此方法将token值传递到后端，Sa-Token 就能像传统PC端一样自动读取到 token 值，进行鉴权
     * 你可能会有疑问，难道我每个ajax都要写这么一坨？岂不是麻烦死了
     * 你当然不能每个 ajax 都写这么一坨，因为这种重复性代码都是要封装在一个函数里统一调用的
     */
    @ResponseBody
    @PostMapping("/login/login")
    public ResultResponse login(@Valid LoginDto user, HttpServletRequest request) {
        user.setPassword(Helper.SHA256(user.getPassword()));
        System.out.println(user);
        UUID uuid = null;
        try {
            uuid = UUID.fromString(userService.login(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uuid != null) {
            String status = userService.getUserStatusByUUID(String.valueOf(uuid));
            if (status.equals("unverified")) {
                return ResultResponse.error(ExceptionEnum.USER_UNVERIFIED);
            } else if (status.equals("blocked")) {
                return ResultResponse.error(ExceptionEnum.USER_BLOCKED);
            }
            Session session = new Session();
            session.setIp(Helper.getIp(request));
            session.setUser_id(String.valueOf(uuid));
            String randomString = Helper.getUUID().toString();
            session.setValue(randomString);
            if (sessionService.login(session)) {
                StpUtil.login(uuid);
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                return ResultResponse.success(tokenInfo);
            }
        }
        return ResultResponse.error("账户或密码错误");
    }

    @ResponseBody
    @RequestMapping("/login/status")
    public ResultResponse isLogin() {
        if (StpUtil.isLogin()) {
            return ResultResponse.success(StpUtil.getTokenInfo());
        }
        return ResultResponse.error("未登录");
    }

    @ResponseBody
    @RequestMapping("/logout")
    public ResultResponse logout() {
        try {
            StpUtil.logout();
            return ResultResponse.success("退出成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultResponse.error(ExceptionEnum.UNAUTHORIZED);
    }

}
