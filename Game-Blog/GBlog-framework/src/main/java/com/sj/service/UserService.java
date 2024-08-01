package com.sj.service;

import com.sj.domain.ResponseResult;
import com.sj.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
*
*/

public interface UserService extends IService<User> {
    ResponseResult userLogin(User user);

    ResponseResult userLogout();

    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult userRegister(User user);
}