package com.health.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.google.gson.Gson;
import com.health.utils.DateUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class test {
    //@Test
    public void t1() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "26S6j86TkgozpL4faLhsM_bHjzgtFaXDBtrZTCwM";
        String secretKey = "QE9GPO9PxLmx69CA9tvUelcL1tHL0fcmlfIpGAK9";
        String bucket = "33333333333";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\myproject\\test_project\\health01\\health01_consumer\\src\\main\\webapp\\img\\boxed-bg.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }

    //@Test
    public void t2POI() {
        /**
         * XSSFWorkbook:工作簿
         * XSSFSheet：工作表
         * Row：每行
         * Cell：每列
         */
        try {
            //加载指定文件
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\ll\\Desktop\\12.xlsx")));
            //读取excel文件中的第一个sheet
            XSSFSheet sheetAt = excel.getSheetAt(0);
            //遍历标签页，获取每一行数据
            for (Row row : sheetAt) {
                //遍历每一列
                for (Cell cell : row) {
                    System.out.println(cell.getStringCellValue());
                }
            }
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //@Test
    public void t3POI() {
        /**
         * 读取excel数据
         * XSSFWorkbook:工作簿
         * XSSFSheet：工作表
         * Row：每行
         * Cell：每列
         */
        try {
            //加载指定文件
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\ll\\Desktop\\12.xlsx")));
            //读取excel文件中的第一个sheet
            XSSFSheet sheetAt = excel.getSheetAt(0);
            //获取工作表的最后一行的行号
            int lastRowNum = sheetAt.getLastRowNum();
            for (int i = 0; i < lastRowNum; i++) {//遍历每一行
                XSSFRow row = sheetAt.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    XSSFCell cell = row.getCell(j);//遍历获取一行的每一列的值
                    System.out.println(cell.getStringCellValue());
                }
            }
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //@Test
    public void t4POI() {
        /**
         * 导出
         * excel写入数据
         * XSSFWorkbook:工作簿
         * XSSFSheet：工作表
         * Row：每行
         * Cell：每列
         */
        try {
            //在内存中创建excel文件
            XSSFWorkbook excel = new XSSFWorkbook();
            //创建工作簿
            XSSFSheet one = excel.createSheet("one");
            XSSFRow title = one.createRow(0);//创建行
            title.createCell(0).setCellValue("姓名");//创建列
            title.createCell(1).setCellValue("年龄");
            title.createCell(2).setCellValue("薪资");//创建列
            title.createCell(3).setCellValue("入职时间");//创建列

            XSSFRow row1 = one.createRow(1);
            row1.createCell(0).setCellValue("小凹字");//创建列
            row1.createCell(1).setCellValue("32");
            row1.createCell(2).setCellValue("3232");
            row1.createCell(3).setCellValue("23231");

            FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\ll\\Desktop\\13.xlsx"));
            excel.write(outputStream);
            outputStream.flush();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //@Test
    public void t5() throws ParseException {
        Date date = new Date("Mon Dec 28 00:00:00 CST 2020");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(java.sql.Date.valueOf(dateFormat.format(date)));
    }

    @Test
    public void t6() {
        //创建集合
        Set<Student> students = new HashSet<>();
        //1.添加数据
        Student s = new Student("张三", 12);
        Student s1 = new Student("李四", 11);
        Student s2 = new Student("王五", 12);

        students.add(s);
        students.add(s1);
        students.add(s2);
        students.add(s2);
        System.out.println(students);
        for (Student student : students) {
            System.out.println(student);
        }

        System.out.println("----------------------------------------------------");
        List<Student> list = new ArrayList<>();
        list.add(s);
        list.add(s1);
        list.add(s2);
        list.add(s2);
        System.out.println(list);
    }

    // @Test
    public void t7() throws Exception {
        Calendar calendar = Calendar.getInstance();//获得日历对象,模拟的时间就是当前系统时间
        calendar.add(Calendar.MONTH, -12);//往前推12个月，即一年前
        System.out.println(new SimpleDateFormat("yyyy").format(calendar.getTime()));
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            String time = new SimpleDateFormat("yyyy").format(calendar.getTime());
            System.out.println(time);
        }


        String s = "2020-10";
        System.out.println(s.substring(0, 4));
    }

    @Test
    public void t8() throws Exception {
        Student student = new Student();
        student.setName("订单");
        student.setAge(12);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Student.class, "name");

        System.out.println(filter.getExcludes());
        System.out.println(filter.getIncludes());

        System.out.println(JSON.toJSONString(student, filter));

    }

    //@Test
    public void t9() {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   //日期格式化
            Date now = calendar.getTime();
            calendar.add(calendar.MONTH, -1);    //得到当前日期减一个月的时间点
            System.out.println(calendar.getTime());//一月前
            while (calendar.getTime().before(now)) {
                System.out.println(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
