package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Menu;
import com.health.pojo.Permission;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface PermissionService {
    PageResult showAll(QueryPageBean queryPageBean);
    void add(Permission permission);

    /**
     * 修改权限信息
     */
    Permission findPermissionById(int id);
    void edit(Permission permission);

    /**
     * 删除
     */
    void deletePermission(int id);


    /**
     * 角色中显示权限列表时使用
     */
    List<Permission> findAllList();
}
