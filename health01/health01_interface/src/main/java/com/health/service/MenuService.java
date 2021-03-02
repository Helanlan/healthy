package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Menu;
import com.health.pojo.Permission;

import java.util.List;

public interface MenuService {
    PageResult findAllMenu(QueryPageBean queryPageBean);
    void add(Menu menu);
    List showMenuByName();
    List<Menu> showParentMenu();
    Menu findById(Integer id);
    void editMenu(Menu menu);

    int deleteMenu(int id);

    /**
     * 角色中显示权限列表时使用
     */
    List<Menu> findAllList();
}
