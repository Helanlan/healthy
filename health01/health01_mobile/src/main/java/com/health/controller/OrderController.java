package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.health.constant.MessageContant;
import com.health.constant.RedisMessageContant;
import com.health.entity.Result;
import com.health.pojo.Order;
import com.health.service.OrderService;
import com.health.utils.SMSUtils;
import com.health.utils.SMSUtilsNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约订单
 * 考虑：日期当天是否设置了预约（这一天是否可以预约），日期当天预约是否已满，检测用户是否重复预约（同一天预约同一个套餐则无法预约），
 * 检查用户是否是会员（如果是会员则直接预约，如果不是会员则自动注册并预约）
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {//将前端提交的内容保存到map中
        //map:{setmealId=2, idCard=666666666666666, sex=1, name=11, validateCode=7297, telephone=16607362843, orderDate=2020-12-18}
        String telephone = (String) map.get("telephone");
        String validateCodeToRedis = jedisPool.getResource().get(telephone + RedisMessageContant.SENDTYPE_ORDER);//redis保存的验证码
        String validateCodeInput = (String) map.get("validateCode");//用户输入的验证码
        if (validateCodeToRedis != null && validateCodeInput != null && validateCodeToRedis.equals(validateCodeInput)) {//redis保存的验证码和用户输入的验证码最对比
            Result result = null;
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型（电话预约会直接录入，，这里是微信自动预约）
            try {
                /**
                 * 为防止网络等原因数据提交失败，需要try--catch
                 */
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {//如果预约成功则开始给用户发送短信
                try {
                    System.out.println("发送短信了");
//                    System.out.println(map);
                    SMSUtilsNotice.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, (String) map.get("name"), map.get("orderDate").toString());

                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            return new Result(false, MessageContant.VALIDATECODE_ERROR);
        }
    }

    /**
     * 一个订单详情
     */
    @RequestMapping("/findById")
    public Result findById(int id) {
        try {
            Map map= orderService.findById(id);
            System.out.println(map);
            return  new Result(true,"",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"订单详情获取失败");
        }
    }
}
