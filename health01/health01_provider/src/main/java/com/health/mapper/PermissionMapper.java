package com.health.mapper;

import com.github.pagehelper.Page;
import com.health.pojo.Menu;
import com.health.pojo.Permission;

import java.util.List;

public interface PermissionMapper {
    Page<Permission> showAll(String queryString);
    void add(Permission permission);

    /**
     * 修改
     */
    Permission findPermissionById(int id);
    void edit(Permission permission);

    /**
     * 删除
     */
    long selCountRolePermissionByPermissionId(int id);
    void deletePermission(int id);

    /**
     * 角色中显示权限列表时使用
     */
    List<Permission> findAllList();
}
