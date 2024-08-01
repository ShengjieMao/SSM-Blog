package com.sj.service;

import com.sj.domain.ResponseResult;
import com.sj.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
