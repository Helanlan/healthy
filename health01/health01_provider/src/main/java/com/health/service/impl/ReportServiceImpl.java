package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.mapper.MemberMapper;
import com.health.mapper.OrderMapper;
import com.health.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;


    @Override
    public Map<String, Object> getBusinessReportData() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Map<String, Object> map = new HashMap<>();
        int todayNewMember = memberMapper.countMemberByDate(today);//今日新增会员数量
        int thisWeekNewMember = memberMapper.countMemberAfterDate();//本周新增会员
        int thisMonthNewMember = memberMapper.countMemberBeforeDate();//本月新增会员
        int allMember = memberMapper.allMember();//全部会员
        int todayOrder = orderMapper.countOrderByDate(today);//今日预约数量
        int thisWeekOrder = orderMapper.countOrderAfterDate();//本周预约
        int thisMonthOrder = orderMapper.countOrderBeforeDate();//本月预约
        int visitOrderToday = orderMapper.countVisitOrderToday(today);//今日预约并已到诊
        int visitOrderThisWeek = orderMapper.countVisitOrderThisWeek();//本周
        int visitOrderThisMonth = orderMapper.countVisitOrderThisMonth();//本月预约并到诊数量
        List<Map<String, Object>> hotSetMeals = orderMapper.hotSetMeal();

        map.put("reportDate",today);
        map.put("todayNewMember", todayNewMember);
        map.put("totalMember", allMember);
        map.put("thisWeekNewMember", thisWeekNewMember);
        map.put("thisMonthNewMember", thisMonthNewMember);
        map.put("todayOrderNumber", todayOrder);
        map.put("todayVisitsNumber", visitOrderToday);
        map.put("thisWeekOrderNumber", thisWeekOrder);
        map.put("thisWeekVisitsNumber", visitOrderThisWeek);
        map.put("thisMonthOrderNumber", thisMonthOrder);
        map.put("thisMonthVisitsNumber", visitOrderThisMonth);
        map.put("hotSetmeal", hotSetMeals);
        System.out.println(map);
        return map;
    }
}
