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

    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    // Add user:
    //  - unique name
    //  - unique phone number
    //  - unique email address
    boolean checkUserNameUnique(String userName);
    boolean checkPhoneUnique(User user);
    boolean checkEmailUnique(User user);
    ResponseResult addUser(User user);

    void updateUser(User user);
}