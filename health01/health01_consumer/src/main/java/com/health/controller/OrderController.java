package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.pojo.Order;
import com.health.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Reference
    private OrderService orderService;

    /**
     * 获取所有的订单列
     * @return
     */
    @RequestMapping("/showOrderList")
    public Result showOrderList(@RequestBody Map map) {
        //orderDate=["2021-01-11T16:00:00.000Z","2021-02-16T16:00:00.000Z"]
        System.out.println(map);
        try {
            List orderDate = (List) map.get("orderDate");
            if (orderDate!=null) {
                String startDate = orderDate.get(0).toString().replace("Z", " UTC");
                String endDate = orderDate.get(1).toString().replace("Z", " UTC");
                SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                Date start = format.parse(startDate);
                Date end = format.parse(endDate);

                String startDateString = defaultFormat.format(start).split(" ")[0];
                String endDateString = defaultFormat.format(end).split(" ")[0];
                map.put("startDate", startDateString);
                map.put("endDate", endDateString);
            }
            List<Order> orders = orderService.showOrderList(map);
            return new Result(true, MessageContant.ORDER_FIND_SUCCESS, orders);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ORDER_FIND_FAIL);
        }
    }
    /**
     * 电话预约，后台人员帮助客户下单
     * @return
     */
    @RequestMapping("/telOrder")
    public Result telOrder(@RequestBody Map map,Integer[] setmealId) {
        try {
            String orderDate = (String) map.get("orderDate");
            System.out.println(orderDate);
            if (orderDate!=null) {
                orderDate = orderDate.replace("Z", " UTC");

                SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                Date date = format.parse(orderDate);
                String result = defaultFormat.format(date);
                String dateString = result.split(" ")[0];
                map.put("orderDate", dateString);
                map.put("setmealId", setmealId[0]);
            }
            System.out.println(map);
            Result result1= orderService.telOrder(map,setmealId);
            return result1;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ORDER_ADD_FAIL);
        }
    }
    /**
     * 后台帮客户取消预约
     * @return
     */
    @RequestMapping("/cancelOrder")
    public Result cancelOrder(int id) {
        try {
            orderService.cancelOrder(id);
            return new Result(true, MessageContant.ORDER_DEL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ORDER_DEL_FAIL);
        }
    }
    /**
     * 后台帮客户修改预约
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageContant.ORDER_FIND_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ORDER_FIND_FAIL);
        }
    }
    @RequestMapping("/updateOrder")
    public Result updateOrder(@RequestBody Map map,Integer[] setmealId) {
        try {
            System.out.println(map);
            String orderDate = (String) map.get("orderDate");
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");

            Date parseOrderDate = simpleDateFormat.parse(orderDate);
            System.out.println();
            if (!orderDate.equals(simpleDateFormat.format(parseOrderDate))) {
                orderDate = orderDate.replace("Z", " UTC");

                SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                Date date = format.parse(orderDate);
                String result = defaultFormat.format(date);
                String dateString = result.split(" ")[0];
                map.put("orderDate", dateString);
            }
            map.put("setmealId", setmealId[0]);
            orderService.updateOrder(map,setmealId);
            return new Result(true, MessageContant.ORDER_EDIT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.ORDER_EDIT_FAIL);
        }
    }
}
