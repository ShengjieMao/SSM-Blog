package com.sj.controller;

import com.sj.domain.ResponseResult;
import com.sj.domain.entity.LoginUser;
import com.sj.domain.entity.Menu;
import com.sj.domain.entity.User;
import com.sj.domain.vo.AdminUserInfoVo;
import com.sj.domain.vo.RoutersVo;
import com.sj.domain.vo.UserInfoVo;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.exception.SystemException;
import com.sj.service.MenuService;
import com.sj.service.RoleService;
import com.sj.service.SystemLoginService;
import com.sj.utils.BeanCopyUtils;
import com.sj.utils.RedisCache;
import com.sj.utils.SecurityUtils;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
public class LoginController {

    @Autowired
    private SystemLoginService systemLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return systemLoginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return systemLoginService.logout();
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        List<String> roleKeyList =
                roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        User user = loginUser.getUser();

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList,
                userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);

        return ResponseResult.okResult(new RoutersVo(menus));
    }
}