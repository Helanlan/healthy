package com.health.mapper;

import com.github.pagehelper.Page;
import com.health.entity.QueryPageBean;
import com.health.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleMapper {
    /**
     * 添加角色，同时添加角色拥有的权限和菜单
     * @param role
     */
    void add(Role role);
    void setRoleMenu(Map map);
    void setRolePermission(Map map);
    /**
     * 分页展示
     */
    Page<Role> findPage(String queryString);
    /**
     * 修改角色信息
     */
    Role findRoleById(int id);//数据回显
    Integer[] findPermissionCheckedList(int id);//这个角色下已添加的权限，用于复选框回显
    Integer[] findMenuCheckedList(int id);//这个角色下已添加的菜单，用于复选框回显
    void editRolePermissionByRoleId(Map map);
    void editRoleMenuByRoleId(Map map);
    void delRolePermissionByRoleId(int roleId);
    void delRoleMenuByRoleId(int roleId);
    void edit(Role role);


    /**
     * 删除
     * 先删除用户角色关系表中role_id=这个角色id的数据
     * 删除前先删除关联表中的关于此角色数据
     */
    void deleteUserRoleByRoleId(int roleId);//删除用户角色关系表中role_id=这个角色id的数据
    void deleteRole(int roleId);


    List<Role> findAll();//用户添加/编辑页面要显示的角色信息

}
