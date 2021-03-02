package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.mapper.MenuMapper;
import com.health.pojo.Menu;
import com.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public PageResult findAllMenu(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Menu> menus = menuMapper.findMenu(queryPageBean.getQueryString());
        long total = menus.getTotal();
        List<Menu> result = menus.getResult();
        return new PageResult(total, result);
    }

    @Override
    public void add(Menu menu) {
        menuMapper.add(menu);
    }

    @Override
    public List showMenuByName() {
        List<Menu> menus1 = menuMapper.showParentMenu();
        List list = new ArrayList();
        for (Menu menu : menus1) {
            List<Menu> menus = menuMapper.showMenuByName(menu.getId());
            list.add(menus);
        }
        return list;
    }

    @Override
    public List<Menu> showParentMenu() {//得到全部的一级菜单
        return menuMapper.showParentMenu();
    }

    @Override
    public Menu findById(Integer id) {
        return menuMapper.findById(id);
    }

    @Override
    public void editMenu(Menu menu) {
        menuMapper.editMenu(menu);
    }

    @Override
    public int deleteMenu(int id) throws RuntimeException{
        List<Menu> childs = menuMapper.findChildsById(id);
            System.out.println(childs);
            System.out.println(childs.size());
            if (childs.size() <= 0) {
                menuMapper.deleteMenu(id);
                return 1;
            }else {
                return 0;
            }
    }

    @Override
    public List<Menu> findAllList() {
        return menuMapper.findAllList();
    }
}
