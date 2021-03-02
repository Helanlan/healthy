package com.health.service;

import com.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void upload(List<OrderSetting> list);
    List<Map> getOrderSettingByMonth(String year, String month);
    /*单个设置可预约人数*/
    void update(String orderDate,Integer value);
}
