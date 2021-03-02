package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.User;

import java.util.List;

public interface UserService {
    /**
     * 查询用户名--》通过用户id查角色--》通过角色id查权限
     * 用户授予权限SpringSecurityUserService.java
     */
    User findByUsername(String username);

    /**
     * 分页查询
     */
    PageResult findByPage(QueryPageBean queryPageBean);

    /**
     * 暂时导出用户数据的时候使用
     */
    List<User> userShowAll();
    List<User>selByIds(Integer[] ids);

    void userAdd(User user,Integer[] roleIds);

    /**
     * 编辑
     */
    User findUserById(int id);//编辑时用于数据回显
    Integer[] findRoleCheckList(int id);//通过用户id获得用户包含的角色列表，用于复选框回显
    void userEdit(User user,Integer[] roleIds);

    /**
     * 删除
     */
    void userDelete(int id);
}
