package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.health.constant.MessageContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Menu;
import com.health.pojo.Permission;
import com.health.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;


    @RequestMapping("/showAllByPage")
    public PageResult showAll(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = permissionService.showAll(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            return new Result(true, MessageContant.PERMISSION_ADD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.PERMISSION_ADD_FAIL);
        }
    }


    /**
     * 修改时数据回显
     */
    @RequestMapping("/findPermissionById")
    public Result findPermissionById(int id){
        try {
            Permission permission = permissionService.findPermissionById(id);
            System.out.println(permission);
            return new Result(true, MessageContant.PERMISSION_FIND_SUCCESS,permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.PERMISSION_FIND_FAIL);
        }
    }
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
            return new Result(true, MessageContant.PERMISSION_EDIT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.PERMISSION_EDIT_FAIL);
        }
    }

    @RequestMapping("/deletePermission")
    public Result deletePermission(int id){
        try {
            permissionService.deletePermission(id);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof RuntimeException){
                return new Result(false, "请先在角色中删除吧好吗！");
            }
            return new Result(false, MessageContant.PERMISSION_DEL_FAIL);
        }
        return new Result(true, MessageContant.PERMISSION_DEL_SUCCESS);
    }



    /**
     * 角色中显示权限列表时使用
     */
    @RequestMapping("/findAllList")
    public Result findAllList(){
        try {
            List<Permission> permissions = permissionService.findAllList();
//            System.out.println(permissions);
            return new Result(true, MessageContant.PERMISSION_FIND_SUCCESS,permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.PERMISSION_FIND_FAIL);
        }
    }
}
