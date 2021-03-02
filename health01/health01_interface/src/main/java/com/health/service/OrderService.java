package com.health.service;

import com.health.entity.Result;
import com.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 后台端
     */
    List<Order> showOrderList(Map map);///预约列表
    Result telOrder(Map map,Integer[] setmealId);//电话预约
    void cancelOrder(int id);//电话取消预约
    void updateOrder(Map map,Integer[] setmealId);
    int countOrderByDate(String day);
    int countOrderVisitByDate(String day);//当日到诊人数




    /**
     * 手机端，客户端
     * @param map
     * @return
     */
    Result order(Map map);
    Map findById(int id);


}
