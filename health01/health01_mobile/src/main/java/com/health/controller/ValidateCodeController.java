package com.health.controller;

import com.health.constant.MessageContant;
import com.health.constant.RedisMessageContant;
import com.health.entity.Result;
import com.health.utils.SMSUtils;
import com.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);//随机生成4为验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());//发送验证码
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+ RedisMessageContant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true, MessageContant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);//随机生成4为验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());//发送验证码
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+ RedisMessageContant.SENDTYPE_LOGIN,5*60,validateCode.toString());//保存5分钟
        return new Result(true, MessageContant.SEND_VALIDATECODE_SUCCESS);
    }
}
