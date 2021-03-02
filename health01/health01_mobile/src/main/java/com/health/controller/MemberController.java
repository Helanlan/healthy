package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.health.constant.MessageContant;
import com.health.constant.RedisMessageContant;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员服务
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login(HttpServletResponse resp, @RequestBody Map map) {//@RequestBody是因为前端提交的数据时json，需要转成map
        String telephone = map.get("telephone").toString();
        String validateCode = map.get("validateCode").toString();
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageContant.SENDTYPE_LOGIN);
        if (validateCode != null && validateCodeRedis != null && validateCode.equals(validateCodeRedis)) {
            //验证码输入正确了
            Member member = memberService.findByTel(telephone);
            //判断是不是会员
            if (member == null) {
                //不是会员则自动注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setPassword("123456");
                member.setRegTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                member.setName(telephone);
                memberService.add(member);
            }
            //向客户端写入cookies
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);//保留一天
            resp.addCookie(cookie);
            //保存到redis
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);//在redis保存30分钟
            return new Result(true,MessageContant.LOGIN_SUCCESS);
        } else {
            //验证码输入不正确
            return new Result(false, MessageContant.VALIDATECODE_ERROR);
        }
    }

    /**
     * 某人的预约记录
     */
    @RequestMapping("/findAllOrderByMemberId")
    public Result findAllOrderByMemberId(@RequestBody Map map) {
        try {
            System.out.println(map);
            List allOrderByMemberId = memberService.findAllOrderByMemberId(map);
            return  new Result(true,MessageContant.MEMBER_ORDER_FIND_SUCCESS,allOrderByMemberId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.MEMBER_ORDER_FIND_FAIL);
        }
    }
    /**
     * 会员信息
     */
    @RequestMapping("/findMemberById")
    public Result findMemberById(int id) {
        try {
            System.out.println(id);
            Member member = memberService.findMemberById(id);
            return  new Result(true,MessageContant.MEMBER_FIND_SUCCESS,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.MEMBER_FIND_FAIL);
        }
    }
    /**
     * 会员信息
     */
    @RequestMapping("/updateMemberById")
    public Result updateMemberById(@RequestBody Member member) {
        try {
            System.out.println(member);
           memberService.updateMember(member);
            return  new Result(true,MessageContant.MEMBER_UPDATE_SUCCESS,member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.MEMBER_UPDATE_FAIL);
        }
    }
}
