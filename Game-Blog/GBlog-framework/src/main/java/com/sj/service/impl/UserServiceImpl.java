package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.domain.ResponseResult;
import com.sj.domain.entity.LoginUser;
import com.sj.domain.entity.User;
import com.sj.domain.entity.UserRole;
import com.sj.domain.vo.BlogUserLoginVo;
import com.sj.domain.vo.PageVo;
import com.sj.domain.vo.UserInfoVo;
import com.sj.domain.vo.UserVo;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.exception.SystemException;
import com.sj.mapper.UserMapper;
import com.sj.service.UserRoleService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Resource
    private UserRoleService userRoleService;

    /**
     *
     */
    @Override
    public ResponseResult userRegister(User user) {
        if (!StringUtils.hasText(user.getUserName())
                || !StringUtils.hasText(user.getPassword())
                || !StringUtils.hasText(user.getEmail())
                || !StringUtils.hasText(user.getNickName())
                || !StringUtils.hasText(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.REGISTER_NOT_NULL);
        }

        checkFieldExists(User::getUserName, user.getUserName(), AppHttpCodeEnum.USERNAME_EXIST);
        checkFieldExists(User::getEmail, user.getEmail(), AppHttpCodeEnum.EMAIL_EXIST);
//        checkFieldExists(User::getNickName, user.getNickName(), AppHttpCodeEnum.NICKNAME_EXIST);

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName,
                user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()), User::getStatus,
                user.getStatus());
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()),
                User::getPhonenumber,user.getPhonenumber());

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        List<User> users = page.getRecords();
        List<UserVo> userVoList = users.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(userVoList);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName)) == 0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,
                user.getPhonenumber())) == 0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail())) == 0;
    }

    @Override
    @Transactional
    public ResponseResult addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if(user.getId() != null && user.getId().length() > 0){
            addUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        addUserRole(user);
        updateById(user);
    }

    private void addUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getId())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
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

    private void checkFieldExists(Function<User, ?> fieldGetter, String value, AppHttpCodeEnum exceptionCode) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq((SFunction<User, ?>) fieldGetter, value);
        User user = getOne(queryWrapper);
        if (Objects.nonNull(user)) {
            throw new SystemException(exceptionCode);
        }
    }
//    public boolean judgeUsername(String username){
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(User::getUserName, username);
//        User user = getOne(queryWrapper);
//        return Objects.isNull(user);
//    }
}