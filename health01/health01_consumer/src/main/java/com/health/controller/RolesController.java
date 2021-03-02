package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Role;
import com.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Reference
    private RoleService roleService;

    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissions, Integer[] menus) {
        try {
            roleService.add(role, permissions, menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ROLE_ADD_FAIL);
        }
        return new Result(true, MessageContant.ROLE_ADD_SUCCESS);
    }

    /**
     * 分页显示
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pages = roleService.findPage(queryPageBean);
        return pages;
    }

    /**
     * 弹出编辑窗口时需要数据回显
     */
    @RequestMapping("/findRoleById")
    public Result findRoleById(int id) {
        try {
            Role role = roleService.findRoleById(id);
            System.out.println(role);
            return new Result(true, MessageContant.ROLE_FIND_SUCCESS, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ROLE_FIND_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role, Integer[] permissions, Integer[] menus) {
        try {
            roleService.edit(role, permissions, menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ROLE_EDIT_FAIL);
        }
        return new Result(true, MessageContant.ROLE_EDIT_SUCCESS);
    }

    @RequestMapping("/deleteRole")
    public Result deleteRole(int id) {
        try {
            roleService.deleteRole(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ROLE_DEL_SUCCESS);
        }
        return new Result(true, MessageContant.ROLE_DEL_FAIL);
    }


    /**
     * 这个角色下已添加的权限，用于复选框回显
     * @param id
     * @return
     */
    @RequestMapping("/findPermissionCheckedList")
    public Integer[] findPermissionCheckedList(int id) {
        return roleService.findPermissionCheckedList(id);
    }

    /**
     * 这个角色下已添加的菜单，用于复选框回显
     * @param id
     * @return
     */
    @RequestMapping("/findMenuCheckedList")
    public Integer[] findMenuCheckedList(int id) {
        return roleService.findMenuCheckedList(id);
    }


    /**
     * 暂时在添加用户时使用
     * 用户添加/编辑页面要显示的角色信息
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Role> all = roleService.findAll();
            return new Result(true,MessageContant.ROLE_FIND_SUCCESS,all);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.ROLE_FIND_FAIL);
        }
    }
}
