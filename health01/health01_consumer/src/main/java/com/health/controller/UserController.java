package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageContant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.UserService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * 后台用户的个各种操作
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/getUsername")
    public Result getUsername(){
        //不需要查询后台数据库，直接使用security提供的（认证完成后会将当前用户的信息保存到框架提供的上下文对象中，基于session）
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       System.out.println(user);
        if (user!=null) {
            return new Result(true, MessageContant.GET_USERNAME_SUCCESS, user.getUsername());
        }else {
            return new Result(false,MessageContant.GET_USERNAME_FAIL);
        }
    }

    /**
     * 分页查询
     */
    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){
        return userService.findByPage(queryPageBean);
    }
    /**
     * 添加用户
     */
    @RequestMapping("/userAdd")
    public Result userAdd(@RequestBody User user,Integer[] roleIds) {
        try {
            userService.userAdd(user,roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageContant.USER_ADD_FAIL);
        }
        return new Result(true,MessageContant.USER_ADD_SUCCESS);
    }
    /**
     * 获得用户包含的角色列表，用于复选框回显
     */
    @RequestMapping("/findRoleCheckList")
    public Integer[] findRoleCheckList(int id) {
        return userService.findRoleCheckList(id);
    }

    /**
     * 编辑时用于数据回显
     */
    @RequestMapping("/findUserById")
    public Result findUserById(int id) {
        try {
            User user = userService.findUserById(id);
            return new Result(true, MessageContant.USER_FIND_SUCCESS, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_FIND_FAIL);
        }
    }

    @RequestMapping("/userEdit")
    public Result userEdit(@RequestBody User user,Integer[] roleIds) {
        try {
            userService.userEdit(user,roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_EDIT_FAIL);
        }
        return new Result(true, MessageContant.USER_EDIT_SUCCESS);
    }
    @RequestMapping("/userDelete")
    public Result userDelete(int id) {
        try {
            userService.userDelete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_DEL_FAIL);
        }
        return new Result(true, MessageContant.USER_DEL_SUCCESS);
    }
    @RequestMapping("/userShowAll")
    public Result userShowAll() {
        try {
            userService.userShowAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_FIND_FAIL);
        }
        return new Result(true, MessageContant.USER_FIND_SUCCESS);
    }

    /**
     * 用户信息全部导出Excel
     */
    @RequestMapping("/userExportExcel")
    public Result userExportExcel(HttpServletRequest request, HttpServletResponse response) {
        List<User> list = userService.userShowAll();
        System.out.println(list);
        try {
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_users.xlsx";
            XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(filePath));
            XSSFSheet sheet = workbook.getSheetAt(0);//第一个表
            XSSFRow r;
            for (int i=0;i<list.size();i++){
                String userName="";
                String sex="";
                String telephone="";
                String birthday="";
                String station="";
                String remark="";
                if (list.get(i).getUserName()!=null) {
                    userName = list.get(i).getUserName();
                }
                if (list.get(i).getSex()!=null) {
                    sex = list.get(i).getSex();
                }
                if (list.get(i).getTelephone()!=null) {
                    telephone = list.get(i).getTelephone();
                }
                if (list.get(i).getBirthday()!=null) {
                    birthday = list.get(i).getBirthday();
                }
                if (list.get(i).getStation()!=null) {
                    station = list.get(i).getStation();
                }
                if (list.get(i).getRemark()!=null){
                    remark = list.get(i).getRemark();
                }
                r = sheet.createRow(i+1);
                r.createCell(0).setCellValue(userName);
                r.createCell(1).setCellValue(sex);
                r.createCell(2).setCellValue(telephone);
                r.createCell(3).setCellValue(birthday);
                r.createCell(4).setCellValue(station);
                r.createCell(5).setCellValue(remark);
            }
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表文件类型是excel
            response.setHeader("content-Disposition", "attachment;filename=users.xlsx");//指定以附件形式下载
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_EXCEL_GET_FAIL);
        }
    }
    /**
     * 用户信息全部导出pdf
     */
    @RequestMapping("/userExportPDF")
    public Result userExportPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<User> list = userService.userShowAll();
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_userPDF.jrxml";//获取health_business1.jrxml的绝对路劲
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_userPDF.jasper";
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);//编译模板

            //填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,null, new JRBeanCollectionDataSource(list));//mysql 方式

            System.out.println(111);
            //通过输出流进行表格下载，基于浏览器作为客户端下载（两头一流）
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");//代表文件类型是excel
            response.setHeader("content-Disposition", "attachment;filename=report_usersPDF.pdf");//指定以附件形式下载
            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            outputStream.flush();
            outputStream.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_EXCEL_GET_FAIL);
        }
    }
    /**
     * 用户信息全部导出Excel
     */
    @RequestMapping("/userExportExcelIsChecked")
    public Result userExportExcelIsChecked(HttpServletRequest request, HttpServletResponse response,Integer[] ids) {
        List<User> list = userService.selByIds(ids);
        try {
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_users.xlsx";
            XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(filePath));
            XSSFSheet sheet = workbook.getSheetAt(0);//第一个表
            XSSFRow r;
            for (int i=0;i<list.size();i++){
                String userName="";
                String sex="";
                String telephone="";
                String birthday="";
                String station="";
                String remark="";
                if (list.get(i).getUserName()!=null) {
                    userName = list.get(i).getUserName();
                }
                if (list.get(i).getSex()!=null) {
                    sex = list.get(i).getSex();
                }
                if (list.get(i).getTelephone()!=null) {
                    telephone = list.get(i).getTelephone();
                }
                if (list.get(i).getBirthday()!=null) {
                    birthday = list.get(i).getBirthday();
                }
                if (list.get(i).getStation()!=null) {
                    station = list.get(i).getStation();
                }
                if (list.get(i).getRemark()!=null){
                    remark = list.get(i).getRemark();
                }
                r = sheet.createRow(i+1);
                r.createCell(0).setCellValue(userName);
                r.createCell(1).setCellValue(sex);
                r.createCell(2).setCellValue(telephone);
                r.createCell(3).setCellValue(birthday);
                r.createCell(4).setCellValue(station);
                r.createCell(5).setCellValue(remark);
            }
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表文件类型是excel
            response.setHeader("content-Disposition", "attachment;filename=users.xlsx");//指定以附件形式下载
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_EXCEL_GET_FAIL);
        }
    }
    /**
     * 用户信息全部导出pdf
     */
    @RequestMapping("/userExportPDFIsChecked")
    public Result userExportPDFIsChecked(HttpServletRequest request, HttpServletResponse response,Integer[] ids) {
        try {
            List<User> list = userService.selByIds(ids);
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_userPDF.jrxml";//获取health_business1.jrxml的绝对路劲
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_userPDF.jasper";
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);//编译模板

            //填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,null, new JRBeanCollectionDataSource(list));//mysql 方式

            //通过输出流进行表格下载，基于浏览器作为客户端下载（两头一流）
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");//代表文件类型是excel
            response.setHeader("content-Disposition", "attachment;filename=report_usersPDF.pdf");//指定以附件形式下载
            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            outputStream.flush();
            outputStream.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageContant.USER_EXCEL_GET_FAIL);
        }
    }
}
