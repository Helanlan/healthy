package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.health.constant.MessageContant;
import com.health.entity.Result;
import com.health.mapper.MemberMapper;
import com.health.mapper.OrderMapper;
import com.health.mapper.OrderSettingMapper;
import com.health.pojo.Member;
import com.health.pojo.Order;
import com.health.pojo.OrderMemberSetMeal;
import com.health.pojo.OrderSetting;
import com.health.service.OrderService;
import com.health.utils.MD5Utils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约订单
 * 考虑：
 * 1、日期当天是否设置了预约（这一天是否可以预约），
 * 2、日期当天预约是否已满，
 * 3、检测用户是否重复预约（同一个用户同一天预约同一个套餐则无法预约），通过手机号码区分
 * 4、检查用户是否是会员（如果是会员则直接预约，如果不是会员则自动注册并预约）
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderSettingMapper orderSettingMapper;



    @Override
    public Result order(Map map) {
        System.out.println(map);
        OrderSetting orderSetting = orderMapper.findByDateToOrderSetting(map.get("orderDate").toString());//预约时需要检测日期当天有没有设置预约，同时有没有约满
        if (orderSetting == null) {//日期当天是否设置了预约
            return new Result(false, MessageContant.SELECTED_DATE_CANNOT_ORDER);
        }
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {//日期当天预约是否已满
            return new Result(false, MessageContant.ORDER_FULL);
        }
        Order order=null;
        //检测用户是否重复预约
        Member member = memberMapper.findByTelephone((String) map.get("telephone"));//通过手机号码查找会员
        if (member == null) {// //检查用户是否是会员,==null说明不是会员
            String name = (String) map.get("name");
            String sex = map.get("sex").toString();
            String idCard = map.get("idCard").toString();
            String phoneNumber = map.get("telephone").toString();
            member = new Member();
            member.getName();
            member.setName(name);
            member.setIdCard(idCard);
            member.setSex(sex);
            member.setPhoneNumber(phoneNumber);
            member.setRegTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            String password = "123456";
            password= MD5Utils.md5(password);//密码加密
            member.setPassword(password);//默认密码
            memberMapper.add(member);
            //注册会员并预约
        } else {
            map.put("memberId", member.getId());
            order = orderMapper.findOrderByOrderDateAndSetmealId(map);//检测有没有相同的预约订单
            if (order!=null&&!order.equals("")) {
                return new Result(false, MessageContant.ORDER_REPEAT_SUBMIT);
            }
        }
        System.out.println("implmap:" + map);
        //预约成功
        map.put("memberId", member.getId());
        map.put("orderState", Order.ORDERSTATE_NO);
        orderMapper.addOrder(map);

        //设置已预约人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingMapper.editReservations(orderSetting);
        order = orderMapper.findOrderByOrderDateAndSetmealId(map);//检测有没有相同的预约订单//一个账号一个套餐同一天只能预约一次
        System.out.println("===>"+order.getId());
        return new Result(true, MessageContant.ORDER_SUCCESS,order.getId());
    }

    /**
     * 显示预约订单详情
     * @param id
     */
    @Override
    public Map findById(int id) {
       return orderMapper.findById(id);
    }





    /**
     * 后台端====================================================================================================================
     */
    @Override
    public List<Order> showOrderList(Map map) {
        System.out.println(map);
        List<Order> orders = orderMapper.showOrderList(map);
        System.out.println(orders);
        return orders;
    }

    @Override
    public Result telOrder(Map map,Integer[] setmealId) { //2021-01-21T16:00:00.000Z
        try {
            OrderSetting orderSetting = orderMapper.findByDateToOrderSetting(map.get("orderDate").toString());//预约时需要检测日期当天有没有设置预约，同时有没有约满
            if (orderSetting == null) {//日期当天是否设置了预约
                return new Result(false, MessageContant.SELECTED_DATE_CANNOT_ORDER);
            }
            if (orderSetting.getReservations() >= orderSetting.getNumber()) {//日期当天预约是否已满
                return new Result(false, MessageContant.ORDER_FULL);
            }
            Order order=null;
            Member member = memberMapper.findByTelephone((String) map.get("phoneNumber"));
            if (member==null){//会员账号不存在就注册
                member=new Member();
                member.setName((String) map.get("name"));
                member.setPhoneNumber((String) map.get("phoneNumber"));
                member.setRegTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                memberMapper.add(member);
            }else {//检测是否有重复订单
                map.put("memberId", member.getId());
                System.out.println("33333:"+map);
                order = orderMapper.findOrderByOrderDateAndSetmealId(map);//检测有没有相同的预约订单
                if (order!=null&&!order.equals("")) {
                    return new Result(false, MessageContant.ORDER_REPEAT_SUBMIT);
                }
            }
            map.put("memberId",member.getId());
            map.put("orderType", Order.ORDERTYPE_TELEPHONE);
            map.put("orderState", Order.ORDERSTATE_NO);
            //map:{orderType=电话预约, phoneNumber=34567890, setmealId=1, name=热帖, orderDate=2021-01-21, memberId=20, orderState=未到诊}
            orderMapper.addOrder(map);
            //设置已预约人数
            orderSetting.setReservations(orderSetting.getReservations() + 1);
            orderSettingMapper.editReservations(orderSetting);
            order = orderMapper.findOrder(map);
            return new Result(true, MessageContant.ORDER_SUCCESS, order.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.ORDER_FIND_FAIL);
        }

    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    public void cancelOrder(int id) {
        Map map = orderMapper.findById(id);
        /*
        * <!--预约成功后更新已预约人数-->
            <update id="editReservations" parameterType="com.health.pojo.OrderSetting">
                update t_ordersetting set reservations=#{reservations} where orderDate=#{orderDate};
            </update>
            map:{orderType=微信预约, setmealId=6, orderId=21, memberName=55, telephone=15574258360, setmealName=瑞慈优生孕前体检, orderDate=2021-01-16, memberId=3, orderState=未到诊}
            * */
        String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(map.get("orderDate"));
        OrderSetting orderSetting = orderSettingMapper.findOrderSettingByDate(orderDate);
        System.out.println(orderSetting);
        orderSetting.setReservations(orderSetting.getReservations()-1);
        orderSettingMapper.editReservations(orderSetting);//删除后可预约人数减一
        orderMapper.deleteOrder(id);
    }

    @Override
    public void updateOrder(Map map,Integer[] setmealId) {
        /**
         * 修改的时候先通过用户id查询用户，
         * 检测原手机号和先收集好是否相同，不同就删除重新添加
         * 否则改
         */
        String telephone = (String) map.get("telephone");
        int memberId = (int) map.get("memberId");
        Member member = memberMapper.selById(memberId);
        if (!telephone.equals(member.getPhoneNumber())){
            System.out.println("修改订单时手机号码变了,干脆删除了重新下单吧");
            cancelOrder((int) map.get("orderId"));
            telOrder(map,setmealId);
        }
        orderMapper.updateOrder(map);
    }


    /**
     * 统计
     * @param day
     * @return
     */
    @Override
    public int countOrderByDate(String day) {//当天预约数量
        return orderMapper.countOrderByDate(day);
    }
    @Override
    public int countOrderVisitByDate(String day) {//当天到诊数量
        return orderMapper.countVisitOrderToday(day);
    }
}
