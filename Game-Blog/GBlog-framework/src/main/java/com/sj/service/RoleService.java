package com.sj.service;

import com.sj.domain.ResponseResult;
import com.sj.domain.dto.RoleDto;
import com.sj.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> getAllRoleList();

    ResponseResult getListAllRole();

    ResponseResult getAllRoleByPage(Integer pageNum, Integer pageSize, RoleDto roleDto);

    ResponseResult changeRoleStatus(Integer roleId, Integer status);

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult getRoleInfoById(Long id);

    ResponseResult deleteRole(Long id);

    ResponseResult updateRoleInfo(RoleDto roleDto);

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

    void addRole(Role role);

    void updateRole(Role role);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);
}