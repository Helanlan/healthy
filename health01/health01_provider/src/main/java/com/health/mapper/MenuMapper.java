package com.health.mapper;

import com.github.pagehelper.Page;
import com.health.pojo.Menu;

import java.util.List;

public interface MenuMapper {
    Page<Menu> findMenu(String selectString);
    void add(Menu menu);
    List<Menu> findByParentMenuID();
    List<Menu> showParentMenu();
    List<Menu> showMenuByName(Integer id);//菜单栏菜单的显示
    Menu findById(Integer id);
    void editMenu(Menu menu);

    List<Menu> findChildsById(Integer id);
    void deleteMenu(int id);

    /**
     * 角色中显示权限列表时使用
     */
    List<Menu> findAllList();
}
