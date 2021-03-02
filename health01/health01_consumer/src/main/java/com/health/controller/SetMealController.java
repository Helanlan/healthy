package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.constant.RedisContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.SetMeal;
import com.health.service.SetMealService;
import com.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐管理：
 * 1、图片存储:先由elementui的upload组件将图片提交到自己写的controller，
 * 再由controller来调用七牛云的工具类，然后将图片上传到七牛云上，
 * 再拿到七牛云上图片的网络地址，然后将imageUrl的值设置成在这个网络地址
 * 2、新增套餐 :将存到数据库的图片名称保存到redis
 * 3、体检套餐的分页查询
 * 4、定时任务（任务调度），定时清理垃圾图片
 * 5、需求分析：新增（包含多个检查组
 */
@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Autowired
    private JedisPool jedisPool;//使用redis存储图片名称


    /**
     * 添加套餐信息
     */
    @Reference
    private SetMealService setMealService;

    @RequestMapping("/add")
    public Result add(@RequestBody SetMeal setMeal, Integer[] checkgroupIds) {
        try {
            setMealService.add(setMeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageContant.ADD_SETMEAL_SUCCESS);
    }


    @RequestMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        String suffix = imgFile.getOriginalFilename().substring(imgFile.getOriginalFilename().lastIndexOf("."));//后缀名
        String fileName = UUID.randomUUID().toString() + suffix;//生成的随机文件名加上后缀
        try {
            System.out.println("pic_fileName::" + fileName);
            QiNiuUtils.upLoad2QiNiu(imgFile.getBytes(), fileName);
            //将上传的图片存储到redis中
            jedisPool.getResource().sadd(RedisContant.SETMEAL_PIC_RESOURCE, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageContant.PIC_UPLOAD_SUCCESS, fileName);
    }

    /**
     * 分页查询
     */
    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        return setMealService.findByPage(queryPageBean);
    }

    /**
     * 编辑套餐项信息
     */
    //基本信息回显
    @RequestMapping("/findById")
    public Result findById(int id) {
        try {
            SetMeal setMeal = setMealService.findById(id);
            return new Result(true, MessageContant.QUERY_SETMEAL_SUCCESS, setMeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.QUERY_SETMEAL_FAIL);
        }
    }

    //复选列表选择回显
    @RequestMapping("/findCheckGroupById")
    public Integer[] findCheckGroupById(int id) {
        return setMealService.findCheckGroupById(id);
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAnyAuthority('SETMEAL_EDIT')")
    public Result update(@RequestBody SetMeal setMeal, Integer[] checkgroupIds) {
        try {
            setMealService.update(setMeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageContant.EDIT_SETMEAL_SUCCESS);
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('SETMEAL_DEL')")
    public Result delete(int id) {
        try {
            setMealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.DEL_SETMEAL_FAIL);
        }
        return new Result(true, MessageContant.DEL_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findAll")
//    @PreAuthorize("hasAnyAuthority('SETMEAL_QUERY')")
    public Result findAll() {
        try {
            List<SetMeal> all = setMealService.findAll();
            return new Result(true, MessageContant.QUERY_SETMEAL_SUCCESS,all);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.QUERY_SETMEAL_SUCCESS);
        }
    }
}
