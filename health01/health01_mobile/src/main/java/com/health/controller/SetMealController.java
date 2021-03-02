package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.Result;
import com.health.pojo.SetMeal;
import com.health.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            List<SetMeal> all = setMealService.findAll();
            return new Result(true, MessageContant.GET_SETMEAL_SUCCESS,all);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.GET_SETMEAL_FAIL);
        }
    }
    @RequestMapping("/findById")
    public Result detailFindById(int id){
        try {
            System.out.println(id);
            SetMeal setMeal = setMealService.detailFindById(id);
            return new Result(true,MessageContant.QUERY_SETMEAL_SUCCESS,setMeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.QUERY_SETMEAL_FAIL);
        }
    }
}
