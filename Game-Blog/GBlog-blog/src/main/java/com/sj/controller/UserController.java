package com.sj.controller;

import com.sj.domain.ResponseResult;
import com.sj.domain.entity.User;
import com.sj.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * Sign up
     */
    @PostMapping("/register")
    public ResponseResult userRegister(@RequestBody User user) {
        return userService.userRegister(user);
    }

    /**
     * Log in
     */
    @PostMapping("/login")
    public ResponseResult userLogin(@RequestBody User user) {
        return userService.userLogin(user);
    }

    /**
     * Log out
     */
    @PostMapping("/logout")
    public ResponseResult userLogout() {
        return userService.userLogout();
    }

    /**
     *
     */
    @GetMapping("/userInfo")
    public ResponseResult getUserInfo() {
        return userService.getUserInfo();
    }

    /**
     *
     */
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }
}