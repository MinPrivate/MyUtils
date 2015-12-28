package com.min.utils.file.excel;


import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelReaderUtil {

    //excel2003扩展名
    public static final String EXCEL03_EXTENSION = ".xls";
    //excel2007扩展名
    public static final String EXCEL07_EXTENSION = ".xlsx";
    public static int totalCount;
    
    /**
     * 读取Excel文件，可能是03也可能是07版本
     * @param excel03
     * @param excel07
     * @param fileName
     * @throws Exception 
     */
    public static void readExcel(RowReader reader, String fileName) throws Exception {
        // 处理excel2003文件
//     String fileName = AliceCleanSettings.getInputFileName();
     
        if (fileName.endsWith(EXCEL03_EXTENSION)) {
        	HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(fileName));
    		HSSFSheet sheet = wb.getSheetAt(0);
    		totalCount = sheet.getLastRowNum();
            Excel2003Reader excel03 = new Excel2003Reader();
            excel03.setRowReader(reader);
            excel03.process(fileName);
            // 处理excel2007文件
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {
        	XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(fileName));
        	 XSSFSheet sheet = wb.getSheetAt(0);
        	 totalCount = sheet.getLastRowNum();
            Excel2007Reader excel07 = new Excel2007Reader();
            excel07.setRowReader(reader);
            excel07.process(fileName);
        
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
    }
    public static void main(String[] args) throws Exception {
        RowReader reader = new RowReader();
        ExcelReaderUtil.readExcel(reader, "D:/5records22.xls");
    }
}