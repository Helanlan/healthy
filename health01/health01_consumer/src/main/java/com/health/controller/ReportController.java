package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.service.MemberService;
import com.health.service.OrderService;
import com.health.service.ReportService;
import com.health.service.SetMealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;
    @Reference
    private OrderService orderService;

    /*一年内新增会员数量统计*/
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(Map<String, Object> map) {
        Calendar calendar = Calendar.getInstance();//获得日历对象,模拟的时间就是当前系统时间
        calendar.add(Calendar.MONTH, -12);//往前推12个月，即一年前
        List<String> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            String date = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            months.add(date);
            List<Integer> memberCount = memberService.findCountByMonth(months);
            map.put("months", months);
            map.put("memberCount", memberCount);

        }
        return new Result(true, MessageContant.REPORT_MEMBER_SUCCESS, map);
    }

    /**
     * 查询一个月内每天的预约到诊数量统计
     */
    @RequestMapping("/getOrderVisitRoport")
    public Result getOrderVisitRoport(Map<String, Object> map) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   //日期格式化
        Calendar calendar = Calendar.getInstance();//获得日历对象,模拟的时间就是当前系统时间
        Date today = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);//往前推1个月
        List<String> days = new ArrayList<>();
        List<Integer> orderNums =new ArrayList<>();
        List<Integer> visitNums=new ArrayList<>();
        while (calendar.getTime().before(today)) {
            String formatDay = sdf.format(calendar.getTime());
            days.add(formatDay);
            int orderNum = orderService.countOrderByDate(formatDay);//预约数量
            int visitNum = orderService.countOrderVisitByDate(formatDay);
            orderNums.add(orderNum);
            visitNums.add(visitNum);

            map.put("days",days);
            map.put("orderNums",orderNums);
            map.put("visitNums",visitNums);
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }



        return new Result(true, MessageContant.REPORT_MEMBER_SUCCESS, map);
    }

    /*年龄分布统计*/
    @RequestMapping("/getMemberAgeReport")
    public Result getMemberAgeReport(Map<String, Object> map) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Member> members = memberService.showAllMember();
        List<String> list = new ArrayList<>();
        list.add("0-6");
        list.add("7-12");
        list.add("13-17");
        list.add("18-45");
        list.add("46-69");
        list.add(">69");
        List<Integer> ages = new ArrayList<>();
        ages.add(0);//0-6
        ages.add(0);//7-12
        ages.add(0);//13-17
        ages.add(0);//18-45
        ages.add(0);//46-69
        ages.add(0);//>60
        for (Member member : members) {
            String birthday = member.getBirthday();
            int ageCount = memberService.selAge(birthday);
            if (ageCount >= 0 && ageCount <= 6) {
                ages.set(0, ages.get(0) + 1);
            } else if (ageCount >= 7 && ageCount <= 12) {
                ages.set(1, ages.get(1) + 1);
            } else if (ageCount >= 13 && ageCount <= 17) {
                ages.set(2, ages.get(2) + 1);
            } else if (ageCount >= 18 && ageCount <= 45) {
                ages.set(3, ages.get(3) + 1);
            } else if (ageCount >= 46 && ageCount <= 69) {
                ages.set(4, ages.get(4) + 1);
            } else if (ageCount > 69) {
                ages.set(5, ages.get(5) + 1);
            }
            System.out.println(ageCount);


            map.put("ageGroup", list);
            map.put("ageCount", ages);
        }
        System.out.println(map);
        return new Result(true, MessageContant.REPORT_MEMBER_SUCCESS, map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(Map<String, Object> map) {
        try {
            Map<String, Object> map1 = setMealService.countSetmeal(map);
            return new Result(true, MessageContant.REPORT_SETMEAL_SUCCESS, map1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.REPORT_SETMEAL_FAIL);
        }
    }


    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageContant.REPORT_FORM_GET_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.REPORT_FORM_GET_FAIL);
        }
    }


    /**
     * excel格式文件下载
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            /**
             * 取出返回的结果，并写入到报表中
             */
            Map<String, Object> map = reportService.getBusinessReportData();
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) map.get("hotSetmeal");


            //基于excel的模板文件在内存中创建一个excel表格
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";//获取report_template.xlsx的绝对路劲
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(filePath));
            //读取第一个工作表
            XSSFSheet sheetAt = excel.getSheetAt(0);
            XSSFRow row = sheetAt.getRow(2);
            XSSFCell cell = row.getCell(5);
            cell.setCellValue(reportDate);//日期

            row = sheetAt.getRow(4);
            cell = row.getCell(5);
            cell.setCellValue(todayNewMember);//今日新增会员
            cell = row.getCell(7);
            cell.setCellValue(totalMember);//总的会员数

            row = sheetAt.getRow(5);
            cell = row.getCell(5);
            cell.setCellValue(thisWeekNewMember);//本周新会员数量
            cell = row.getCell(7);
            cell.setCellValue(thisMonthNewMember);//本月新会员数量


            row = sheetAt.getRow(7);
            cell = row.getCell(5);
            cell.setCellValue(todayOrderNumber);
            cell = row.getCell(7);
            cell.setCellValue(todayVisitsNumber);//今日到诊数量


            row = sheetAt.getRow(8);
            cell = row.getCell(5);
            cell.setCellValue(thisWeekOrderNumber);
            cell = row.getCell(7);
            cell.setCellValue(thisWeekVisitsNumber);//本周到诊数量


            row = sheetAt.getRow(9);
            cell = row.getCell(5);
            cell.setCellValue(thisMonthOrderNumber);
            cell = row.getCell(7);
            cell.setCellValue(thisMonthVisitsNumber);//本月到诊数量


            int rowNum = 12;
            for (Map<String, Object> map1 : hotSetmeal) {
                String name = (String) map1.get("name");
                long total = (long) map1.get("total");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");//MySQL数据库的SUM()函数、除的结果，返回给dao层的是BigDecimal类型。
                row = sheetAt.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(total);
                row.getCell(6).setCellValue(proportion.doubleValue());
            }


            //通过输出流进行表格下载，基于浏览器作为客户端下载（两头一流）
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表文件类型是excel
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式下载
            excel.write(outputStream);
            outputStream.flush();
            outputStream.close();
            excel.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.REPORT_FORM_GET_FAIL);
        }

    }

    /**
     * pdf类型文件下载
     * JavaBean
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            /**
             * 取出返回的结果，并写入到报表中
             */
            Map<String, Object> map = reportService.getBusinessReportData();
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) map.get("hotSetmeal");

            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business1.jrxml";//获取health_business1.jrxml的绝对路劲
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business1.jasper";
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);//编译模板

            //填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, map, new JRBeanCollectionDataSource(hotSetmeal));//mysql 方式

            //通过输出流进行表格下载，基于浏览器作为客户端下载（两头一流）
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");//代表文件类型是excel
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");//指定以附件形式下载
            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            outputStream.flush();
            outputStream.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.REPORT_FORM_GET_FAIL);
        }

    }
}

