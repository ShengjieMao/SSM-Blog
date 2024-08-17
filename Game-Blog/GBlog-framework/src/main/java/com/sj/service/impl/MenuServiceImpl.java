package com.sj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sj.constants.CommonConstants;
import com.sj.domain.ResponseResult;
import com.sj.domain.dto.MenuDto;
import com.sj.domain.entity.Menu;
import com.sj.domain.vo.MenuTreeVo;
import com.sj.domain.vo.MenuVo;
import com.sj.domain.vo.RoleMenuTreeSelectVo;
import com.sj.domain.vo.RoutersVo;
import com.sj.mapper.MenuMapper;
import com.sj.service.MenuService;
import com.sj.utils.BeanCopyUtils;
import com.sj.utils.SecurityUtils;
import com.sj.utils.SystemConverter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sj.constants.CommonConstants.MENU_PARENT_ID;
import static com.sj.enums.AppHttpCodeEnum.*;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Lazy
    @Resource
    private MenuService menuService;

    @Override
    public ResponseResult selectRouterMenu() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = null;

        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectAllRouterMenu();
        } else {
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        List<Menu> menuTree = buildMenuTree(menus, MENU_PARENT_ID);
        return ResponseResult.okResult(new RoutersVo(menuTree));
    }

    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream().
        filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildrenTree(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildrenTree(Menu menu, List<Menu> menus) {
        List<Menu> childrenTree = menus.stream()
                .filter(menu1 -> menu1.getParentId().equals(menu.getId()))
                .map(menu1 -> menu1.setChildren(getChildrenTree(menu1, menus)))
                .collect(Collectors.toList());
        return childrenTree;
    }

    @Override
    public ResponseResult getMenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status)
                .like(StringUtils.hasText(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);

        List<Menu> menus = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);

        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(MenuDto menuDto) {
        Menu menu = BeanCopyUtils.copyBean(menuDto, Menu.class);
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName, menu.getMenuName());
        Menu oneMenu = getOne(queryWrapper);
        if (!Objects.isNull(oneMenu)) {
            return ResponseResult.errorResult(ADD_MENU_FAIL);
        }
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, id);
        Menu menu = getOne(queryWrapper);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(MenuDto menuDto) {
        if (!StringUtils.hasText(menuDto.getMenuName()) ||
                !StringUtils.hasText(menuDto.getMenuType()) ||
                !StringUtils.hasText(String.valueOf(menuDto.getStatus())) ||
                !StringUtils.hasText(menuDto.getPath()) ||
                !StringUtils.hasText(String.valueOf(menuDto.getOrderNum())) ||
                !StringUtils.hasText(menuDto.getIcon())) {
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }
        Menu menu = BeanCopyUtils.copyBean(menuDto, Menu.class);
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        List<Menu> menus = menuMapper.selectList(queryWrapper);

        if (!Objects.isNull(menus) && menus.size() != 0) {
            return ResponseResult.errorResult(DELETE_MENU_REFUSE);
        }

        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName,
                menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;

        return menus;
    }

    @Override
    public ResponseResult getMenuTree() {
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> menuTreeVos =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(menuTreeVos);
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = this.selectMenuListByRoleId(id);

        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);

        return ResponseResult.okResult(vo);
    }

    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (SecurityUtils.isAdmin()) { // admin
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, CommonConstants.MENU, CommonConstants.BUTTON);
            wrapper.eq(Menu::getStatus, CommonConstants.MENU_STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            return menus.stream().map(Menu::getPerms).toList();
        }
        // roles with limited permissions
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        if (SecurityUtils.isAdmin()) {
            // admin - return all menu
            menus = menuMapper.selectAllRouterMenu();
        }
        else {
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        // Create tree
        List<Menu> menuTree = buildMenuTree(menus, 0L);

        return menuTree;
    }

    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }

    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }
}