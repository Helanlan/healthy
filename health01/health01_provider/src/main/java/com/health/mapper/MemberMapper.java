package com.health.mapper;

import com.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberMapper {
    Member findByTelephone(String telephone);//通过手机号查找
    //注册会员
    void add(Member member);

    //统计一年内会员增长走势图
    int findCountByMonth(String regTimeMonth);

    int countMemberByDate(String date);//当天会新增员总数
    int  allMember();//所有会员数量
    int countMemberAfterDate();
    int countMemberBeforeDate();



    List findAllOrderByMemberId(Map map);//查询会员的所有订单，包括订单信息、套餐信息、会员信息
    Member findMemberById(int id);//会员详情
    void updateMember(Member member);//会员更新个人信息

    Member selById(int memberId);//通过mid查看会员信息

    List<Member> showAllMember();
    int selAge(String birthday);//通过生日计算出年龄
}
