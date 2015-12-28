package com.min.utils.file.xml;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class MatchExcelDetails {
	FileOutputStream fileout;
	Workbook wb;
	Sheet sheet1;

	public void createExcle(String name) throws IOException {
		File newfile = new File(name + "_pro.xls");

		fileout = new FileOutputStream(newfile);
		wb = new HSSFWorkbook();
		sheet1 = wb.createSheet("1");
		init1();

	}

	void init1() {
		Row rows = sheet1.createRow(0);
		rows.createCell(0).setCellValue("Match Level ");
		rows.createCell(1).setCellValue("Counts");
		rows.createCell(2).setCellValue("PCT");
	}
	
	void writeToFile1() {
		Object[][] obj = new Object[5][3];

		Row rows = sheet1.createRow(1);
		String s = "";
		rows.createCell(0).setCellValue("Exact match");
		rows.createCell(2).setCellValue( s+ "%");
		obj[0][0] = "Exact match";
		obj[0][2] = s + "%";
		rows = sheet1.createRow(2);
		rows.createCell(0).setCellValue("Street level match");
		rows.createCell(2).setCellValue(s+ "%");
		obj[1][0] = "Street level match";
		obj[1][2] = s + "%";
	}
	
	/**
	 * 输出到统计信息文件
	 * @throws IOException
	 */
	public void ouputStaticsFile() throws IOException {
		writeToFile1();
		wb.write(fileout);
		fileout.close();
	}
}
