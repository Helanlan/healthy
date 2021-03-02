package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Role;

import java.util.List;

public interface RoleService {
    PageResult findPage(QueryPageBean queryPageBean);//分页查询
    void add(Role role,Integer[] permissions,Integer[] menus);
    void edit(Role role,Integer[] permissions,Integer[] menus);
    Role findRoleById(int id);

    void deleteRole(int id);

    Integer[] findPermissionCheckedList(int id);//这个角色下已添加的权限，用于复选框回显
    Integer[] findMenuCheckedList(int id);//这个角色下已添加的菜单，用于复选框回显

    List<Role> findAll();//用户添加/编辑页面要显示的角色信息
}
