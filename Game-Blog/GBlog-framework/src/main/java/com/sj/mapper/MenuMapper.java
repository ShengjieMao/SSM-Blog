package com.sj.mapper;

import com.sj.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     *
     */
    List<String> selectPermsByUserId(Long userId);


    /**
     *
     */
    List<Menu> selectAllRouterMenu();

    /**
     *
     */
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Long> selectMenuListByRoleId(Long roleId);
}