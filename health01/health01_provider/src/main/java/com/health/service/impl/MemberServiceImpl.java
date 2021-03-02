package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.health.mapper.MemberMapper;
import com.health.pojo.Member;
import com.health.service.MemberService;
import com.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 会员服务
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;
    @Override
    public Member findByTel(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            password= MD5Utils.md5(password);//加密
            member.setPassword(password);
        }
        memberMapper.add(member);
    }

    /**
     * 根据月份查找会员注册数量
     * 统计一年内会员增长走势图
     * @param months
     * @return
     */
    @Override
    public List<Integer> findCountByMonth(List<String> months) {
        List<Integer> count=new ArrayList<>();
        for (String month:months){
//            String year = month.substring(0, 4);
            String date = month+"-1";
            int countMember = memberMapper.findCountByMonth(date);
            count.add(countMember);
        }
        return count;
    }

    @Override
    public List<Member> showAllMember() {
        return memberMapper.showAllMember();
    }

    @Override
    public int selAge(String birthday) {
        int selAge = memberMapper.selAge(birthday);
        System.out.println(selAge);
        return selAge;
    }

    @Override
    public List findAllOrderByMemberId(Map map) {//查询会员的所有订单，包括订单信息、套餐信息、会员信息
        System.out.println(map);
        return memberMapper.findAllOrderByMemberId(map);
    }

    @Override
    public Member findMemberById(int id) {//会员详情
        return memberMapper.findMemberById(id);
    }

    @Override
    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }
}
