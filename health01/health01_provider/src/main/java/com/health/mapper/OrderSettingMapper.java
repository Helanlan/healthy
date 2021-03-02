package com.health.mapper;

import com.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingMapper {
    void add(OrderSetting setting);
    int findByOrderDate(String orderDate);
    void updateNumberByOrderDate(OrderSetting setting);

    List<OrderSetting> getOrderSettingByMonth(Map map);/*文件上传的时候如果有重复的日期就修改相应的值*/

    /*单个设置可预约人数*/
    void update(Map<String, Object> map);
    void add1(Map<String, Object> map);//手动设置日期当天的预约数量，不是批量导入


    //更新已预约人数
    void  editReservations(OrderSetting orderSetting);

    OrderSetting findOrderSettingByDate(String orderDate);


}
