package com.health.mapper;

import com.health.pojo.Member;
import com.health.pojo.Order;
import com.health.pojo.OrderMemberSetMeal;
import com.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderMapper {
    /**
     * mobile端使用，
     */
    OrderSetting findByDateToOrderSetting(String orderDates);//预约时需要检测日期当天有没有设置预约，同时有没有约满
    //检测是否是会员
    Order findOrderByOrderDateAndSetmealId(Map map);//检测是否重复预约

    void addOrder(Map map);
    Order findOrder(Map map);//查找这个会员这天是否预约了这个套餐
    Map findById(int id);//显示预约订单详情

    /**
     * 按日期查询统计
     */
    int countOrderByDate(String date);//当天预约总数
    int countOrderAfterDate();
    int countOrderBeforeDate();
    int countVisitOrderToday(String date);//当天已到诊总数
    int countVisitOrderThisWeek();
    int countVisitOrderThisMonth();
    List<Map<String,Object>> hotSetMeal();


    /**
     * 后台端使用
     */
    List<Order> showOrderList(Map map);
    void deleteOrder(int id);
    void updateOrder(Map map);
}
