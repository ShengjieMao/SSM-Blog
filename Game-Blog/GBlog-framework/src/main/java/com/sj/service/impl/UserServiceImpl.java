package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.ResponseResult;
import com.sj.domain.entity.LoginUser;
import com.sj.domain.entity.User;
import com.sj.domain.vo.BlogUserLoginVo;
import com.sj.domain.vo.UserInfoVo;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.exception.SystemException;
import com.sj.mapper.UserMapper;
import com.sj.service.UserService;
import com.sj.utils.BeanCopyUtils;
import com.sj.utils.JwtUtils;
import com.sj.utils.RedisCache;
import com.sj.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

import static com.sj.constants.RedisConstants.BLOG_USER_LOGIN;


/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     *
     */
    @Override
    public ResponseResult userRegister(User user) {
        if (!StringUtils.hasText(user.getUserName())
                && !StringUtils.hasText(user.getPassword())
                && !StringUtils.hasText(user.getEmail())
                && !StringUtils.hasText(user.getNickName())
                && !StringUtils.hasText(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.REGISTER_NOT_NULL);
        }

        if (!judgeUsername(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!judgeNickname(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (!judgeEmail(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        return ResponseResult.okResult();
    }

    /**
     *
     */
    @Override
    public ResponseResult userLogin(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("Incorrect Username or Password");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtils.createJWT(userId);

        redisCache.setCacheObject("bloglogin:" + userId, loginUser);

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);

        return ResponseResult.okResult(blogUserLoginVo);
    }


    /**
     *
     */
    public ResponseResult userLogout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();

        redisCache.deleteObject("bloglogin:" + userId);
        return ResponseResult.okResult();
    }

    /**
     *
     */
    @Override
    public ResponseResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);

        UserInfoVo userInfo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfo);
    }

    /**
     *
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        boolean result = updateById(user);
        if (Objects.isNull(result)) {
            return ResponseResult.errorResult(408, "Failed to update");
        }
        redisCache.deleteObject("bloglogin:" + user.getId());

        User newUser = getById(user.getId());
        LoginUser loginUser = new LoginUser(newUser,null);

        redisCache.setCacheObject("bloglogin:" + user.getId(), loginUser);
        return ResponseResult.okResult();
    }

    /**
     *
     */
    public boolean judgeUsername(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName, username);
        User user = getOne(queryWrapper);
        return Objects.isNull(user);
    }

    /**
     *
     */
    public boolean judgeEmail(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        return Objects.isNull(user);
    }

    /**
     *
     */
    public boolean judgeNickname(String nickname){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getNickName, nickname);
        User user = getOne(queryWrapper);
        return Objects.isNull(user);
    }
}