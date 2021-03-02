package com.health.service;

import com.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTel(String telephone);

    //注册会员
    void add(Member member);

    List<Integer> findCountByMonth(List<String> months);//查询每个月会员增长数量

    List<Member> showAllMember();
    int selAge(String birthday);//通过生日计算出年龄

    List findAllOrderByMemberId(Map map);//查询会员的所有订单，包括订单信息、套餐信息、会员信息

    Member findMemberById(int id);//会员详情
    void updateMember(Member member);//会员更新个人信息
}
