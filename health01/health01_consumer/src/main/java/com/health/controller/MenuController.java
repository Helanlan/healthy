package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Menu;
import com.health.service.MenuService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    @RequestMapping("/findAllMenu")
    public PageResult findAllMenu(@RequestBody QueryPageBean queryPageBean) {
        PageResult allMenu = menuService.findAllMenu(queryPageBean);
        return allMenu;
    }
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true,MessageContant.MENU_ADD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.MENU_ADD_FAIL);
        }
    }

    @RequestMapping("/showMenuByName")
    public Result showMenuByName(Model model){
        try {
            List menus = menuService.showMenuByName();
            System.out.println(menus);
            List list=new ArrayList<>();
            for (int i=0;i< menus.size();i++){
                list.add(menus.get(i));
                model.addAttribute("menus",menus.get(i));
            }
            return new Result(true,"菜单显示成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "菜单显示失败");
        }
    }
    @RequestMapping("/showParentMenu")
    public Result showParentMenu(){
        try {
            List<Menu> menus = menuService.showParentMenu();
            System.out.println(menus);
            return new Result(true,"菜单显示成功",menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "菜单显示失败");
        }
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Menu menu = menuService.findById(id);
            return new Result(true,MessageContant.MENU_FIND_SUCCESS,menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.MENU_FIND_FAIL);
        }
    }
    @RequestMapping("/editMenu")
    public Result editMenu(@RequestBody Menu menu){
        try {
            menuService.editMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.MENU_EDIT_FAIL);
        }
        return new Result(true,MessageContant.MENU_EDIT_SUCCESS);
    }
    @RequestMapping("/deleteMenu")
    public Result deleteMenu(int id){
        try {
            int deleteMenu = menuService.deleteMenu(id);
            if (deleteMenu==0){
                return new Result(false, "请先删除子菜单");
            }else {
                return new Result(true,MessageContant.MENU_DEL_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.MENU_DEL_FAIL);
        }
    }


    /**
     * 角色中显示菜单列表时使用
     */
    @RequestMapping("/findAllList")
    public Result findAllList(){
        try {
            List<Menu> menus = menuService.findAllList();
            return new Result(true,MessageContant.MENU_FIND_SUCCESS,menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.MENU_FIND_FAIL);
        }
    }
}
