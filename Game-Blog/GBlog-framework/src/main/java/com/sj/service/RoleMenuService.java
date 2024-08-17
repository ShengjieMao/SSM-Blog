package com.sj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sj.domain.entity.RoleMenu;

public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);

}