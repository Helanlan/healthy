package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.Result;
import com.health.pojo.OrderSetting;
import com.health.service.OrderSettingService;
import com.health.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 预约数量设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("excelFile")MultipartFile file){
        try {
            //转换一下类型
            List<String[]> list = POIUtils.readExcel(file);
            List<OrderSetting> orderSettings=new ArrayList<>();
            for (String[] row:list){
                OrderSetting setting =new OrderSetting(row[0],Integer.parseInt(row[1]));
                orderSettings.add(setting);
            }
//            System.out.println("orderSettings:"+orderSettings);
            //批量导入
            orderSettingService.upload(orderSettings);
            return new Result(true, MessageContant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageContant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String year,String month){
        try {
            List<Map> orderSettingByMonth = orderSettingService.getOrderSettingByMonth(year, month);
//            System.out.println("orderSettingByMonth:"+orderSettingByMonth);
            return new Result(true,MessageContant.GET_ORDERSETTING_SUCCESS,orderSettingByMonth);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/update")
    public Result update(String orderDate,Integer number){
        try {
//            System.out.println(orderDate);
//            System.out.println(number);
            orderSettingService.update(orderDate,number);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageContant.ORDERSETTING_SUCCESS);
    }
}
