package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;
import com.health.service.CheckItemService;
import com.health.constant.MessageContant;
import com.health.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**   nhb查项管理
 * @RequestBody：解析前端提交的数据，封装成指定的CheckItem对象，即将json格式的数据转换成javabean
 * @RequestBody：解析提交过来的json数据，解析成Javabean
 * @Reference：去zookeeper服务中心查找CheckItemService的服务
 *
 * 检查项业务：添加删除查询更新，删除的时候要求先检查是否已添加到检查组，是则不能删除，不是则直接删除，跟新需要数据回显
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    @Reference//查找服务
    private CheckItemService checkItemService;


    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageContant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageContant.ADD_CHECKITEM_SUCCESS,checkItem);
    }

    @PostMapping("/findByPage")
    public PageResult selAll(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.selAll(queryPageBean);
        return pageResult;
    }
    @RequestMapping("/deleteCheckItem")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DEL')")//有这个权限才能删除
    public Result deleteCheckItem(int id){
        /*先看检查组有没有，有就不能删除，没有则直接删除*/
        try {
            checkItemService.deleteCheckItem(id);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof RuntimeException) {
                return new Result(false, "请先在检查组中删除吧好吗！");
            }
            return new Result(false,MessageContant.DELETE_CHECKITEM_FAIL);
        }
    return new Result(true,MessageContant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){//编辑的时候的数据回显，
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageContant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.QUERY_CHECKITEM_FAIL);
        }
    }
    @RequestMapping("/updateCheckItem")
    public Result updateChickItem(@RequestBody CheckItem checkItem){
        /*先数据回显旧数据*/
        try {
            checkItemService.updateCheckItem(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageContant.EDIT_CHECKITEM_SUCCESS);
    }




    @RequestMapping("/findAll")
    public Result findAll(){//checkgroup添加检查项是要显示的
        try {
            List<CheckItem> all = checkItemService.findAll();
            return new Result(true,MessageContant.QUERY_CHECKITEM_SUCCESS,all);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.QUERY_CHECKITEM_FAIL);
        }
    }
}
