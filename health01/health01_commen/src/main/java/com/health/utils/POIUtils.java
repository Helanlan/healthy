package com.health.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class POIUtils {
    public static final String xls = "xls";
    public static final String xlsx = "xlsx";
    public static final String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * 读取excel文件，解析后返回
     */
    public static List<String[]> readExcel(MultipartFile multipartFile) throws IOException {
        //检查文件是否合法
        if (multipartFile == null) {
            throw new FileNotFoundException("文件不存在");
        }

        String filename = multipartFile.getOriginalFilename();//获取原始文件名
        //判断文件是否是excel
        if (!filename.endsWith("xls") && !filename.endsWith("xlsx")) {
            throw new IOException("不是excel文件");
        }

        //条件满足后获取工作簿对象
        Workbook workbook = null;
        InputStream inputStream = multipartFile.getInputStream();
        if (filename.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (filename.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }


        //创建返回对象，每行的值作为一个数组，所有行作为一个集合返回
        List<String[]> list =new ArrayList<>();
        if (workbook!=null){
            //遍历工作表
            System.out.println("getNumberOfSheets:"+workbook.getNumberOfSheets());
            for (int sheetNum=0;sheetNum<workbook.getNumberOfSheets();sheetNum++){
                Sheet sheet = workbook.getSheetAt(sheetNum);//获得工作表
                if (sheet!=null){
                    System.out.println("getLastRowNum:"+sheet.getLastRowNum());
                    for (int rowNum=sheet.getFirstRowNum()+1;rowNum<=sheet.getLastRowNum();rowNum++){//遍历除了第一行的所有行
                        Row row = sheet.getRow(rowNum);
                        String[] cells=new String[row.getPhysicalNumberOfCells()];//创建一个数组，长度是这一行的列数
                        if (row!=null){
                            for (int cellNum=row.getFirstCellNum();cellNum<row.getLastCellNum();cellNum++){
                                Cell cell = row.getCell(cellNum);//每列
                                cells[cellNum]= getCellValue(cell);
//                                System.out.println(cellNum+"--->"+row.getCell(0)+"--->"+getCellValue(cell));
                                if (cells[cellNum]==null|| cells[cellNum].equals("")){
                                    continue;
                                }
                            }
                        }
                        list.add(cells);
                    }
                }
            }
            workbook.close();
        }
        return list;
    }
    public static String getCellValue(Cell cell){
        String cellValue="";
        if (cell==null){
            return cellValue;
        }
        //如果是日期格式，需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if (dataFormatString.equals("m/d/yy")){//根据实际情况填
            cellValue=new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //如果是数字格式，也要特殊处理，让其按照字符串来读去
        if (cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }


        //判断数据类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC:
                cellValue=String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue=String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue= String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue=cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_BLANK:
                cellValue="";
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue="非法字符";
                break;
            default:
                cellValue="未知类型";
                break;
        }
        return cellValue;
    }
}
