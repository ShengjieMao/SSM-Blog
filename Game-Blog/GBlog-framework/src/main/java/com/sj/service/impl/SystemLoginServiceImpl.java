package com.sj.service.impl;

import com.sj.domain.entity.LoginUser;
import com.sj.domain.ResponseResult;
import com.sj.domain.entity.User;
import com.sj.service.SystemLoginService;
import com.sj.utils.JwtUtils;
import com.sj.utils.RedisCache;
import com.sj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
@Service
public class SystemLoginServiceImpl implements SystemLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("Incorrect Username or Password.");
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();

        String jwt = JwtUtils.createJWT(userId);

        redisCache.setCacheObject("login: "+userId,loginUser);

        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login: " + userId);
        return ResponseResult.okResult();
    }
}