package com.sj.service;

import com.sj.domain.ResponseResult;
import com.sj.domain.dto.MenuDto;
import com.sj.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {
    ResponseResult selectRouterMenu();

    List<Menu> selectMenuList(Menu menu);

    ResponseResult getMenuList(String status, String menuName);

    ResponseResult addMenu(MenuDto menuDto);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(MenuDto menuDto);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult getMenuTree();

    ResponseResult roleMenuTreeselect(Long id);

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    // For delete - safe check for sub-menus
    boolean hasChild(Long menuId);

    List<Long> selectMenuListByRoleId(Long roleId);
}