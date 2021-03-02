package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.mapper.OrderSettingMapper;
import com.health.pojo.OrderSetting;
import com.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;

    /**
     * 预约数据上传
     * @param list
     */
    @Override
    public void upload(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {

            for (OrderSetting orderSetting : list) {

                /**
                 * 有就跟新，没有就添加
                 */
                int orderDateNum = orderSettingMapper.findByOrderDate(orderSetting.getOrderDate());
                System.out.println(orderDateNum);
                if (orderDateNum > 0) {
                    orderSettingMapper.updateNumberByOrderDate(orderSetting);
                } else {
                    orderSettingMapper.add(orderSetting);
                }
            }
        }

    }

    /**
     * 获取数据库的预约数据，传到前端
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String year, String month) {
        String begin = year + "-" + month + "-" + "1";
        String end = year + "-" + month + "-" + "31";
        Map<String, String> map = new HashMap<>();
        map.put("startDate", begin);
        map.put("endDate", end);
        List<OrderSetting> list = orderSettingMapper.getOrderSettingByMonth(map);
        List<Map> list1 = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("date", orderSetting.getOrderDate().substring(orderSetting.getOrderDate().lastIndexOf("-")+1));
            map1.put("number", orderSetting.getNumber());
            map1.put("reservations", orderSetting.getReservations());
            list1.add(map1);
        }
        return list1;
    }

    @Override
    public void update(String orderDate,Integer value) {
        int byOrderDate = orderSettingMapper.findByOrderDate(orderDate);
        Map<String, Object> map = new HashMap<>();
        map.put("orderDate", orderDate);
        map.put("value", value);
        if (byOrderDate>0) {
            orderSettingMapper.update(map);
        }else {
            orderSettingMapper.add1(map);
        }
    }
}
