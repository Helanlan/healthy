package com.health.mapper;

import com.github.pagehelper.Page;
import com.health.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * 查询用户名--》通过用户id查角色--》通过角色id查权限
     * 用户授予权限SpringSecurityUserService.java
     */
    User findByUsername(String username);


    /**
     * 分页查询
     */
    Page<User> findByPage(String queryString);

    /**
     * 暂时导出用户数据的时候使用
     */
    List<User> userShowAll();
    List<User>selByIds(Integer[] ids);

    /**
     * 添加
     */
    void userAdd(User user);
    void addRoleListToUserRole(Map map);


    /**
     * 编辑
     */
    User findUserById(int id);//编辑时用于数据回显
    Integer[] findRoleCheckList(int id);//通过用户id获得用户包含的角色列表，用于复选框回显
    void deleteUserRoleByUserId(int userid);//编辑时先删除旧的角色列表
    void userEdit(User user);

    /**
     * 删除
     */
    void userDelete(int id);
}
