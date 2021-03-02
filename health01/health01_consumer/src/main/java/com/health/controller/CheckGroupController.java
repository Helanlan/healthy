package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckGroup;
import com.health.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 检查组
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/addCheckGroup")
    public Result addCheckGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.addGroupItem(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageContant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){//检查组分页查询
        PageResult page = checkGroupService.findByPage(queryPageBean);
        return page;
    }

    @RequestMapping("/findCheckGroupById")
    public Result findCheckGroupById(int id){//编辑检查组时数据回显
        try {
            CheckGroup checkGroup = checkGroupService.findCheckGroupById(id);
            return new Result(true, MessageContant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.QUERY_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/findById")
    public Integer[] findById(int id){//获取检查组中包含的检查项的列表
        return checkGroupService.findById(id);
    }

    @RequestMapping("/updateCheckGroup")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_EDIT')")
    public Result updateCheckGroup(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.updateCheckGroup(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageContant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/deleteCheckGroup")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_DEL')")
    public Result deleteCheckGroup(int id){
        try {
            checkGroupService.deleteCheckGroup(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageContant.DELETE_CHECKGROUP_SUCCESS);
    }


    /**
     * 体检套餐中要显示的检查组的信息列表
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){//体检套餐中要显示的检查组的信息列表
        try {
            List<CheckGroup> all = checkGroupService.findAll();
            return new Result(true, MessageContant.ADD_SETMEAL_SUCCESS,all);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageContant.ADD_SETMEAL_FAIL);
        }
    }
}
