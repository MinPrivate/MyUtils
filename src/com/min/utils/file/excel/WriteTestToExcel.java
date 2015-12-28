package com.min.utils.file.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class WriteTestToExcel {

	FileOutputStream fileout;
	Workbook wb;
	protected Sheet sheet;
	protected Sheet sheet1;
	protected Sheet sheet2;
	
	Font fontFTitle;
	Font fontContent;
	CellStyle style;
	CellStyle style1;
	
	private int row = 1;
	
	Map<String,List<String>> sourceData;

	public WriteTestToExcel(String fileName,Map<String, List<String>> sourceData) {
		super();
		this.sourceData = sourceData;
		
		try {
			this.createExcle(fileName);
			init();
			WriteToExcel();
			closeFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 创建excle文件
	public void createExcle(String fileName) throws IOException {
		File newfile = new File(fileName);

		fileout = new FileOutputStream(newfile);
		wb = new HSSFWorkbook();
		sheet = wb.createSheet("Quality Code");
		sheet1 = wb.createSheet("Quality Code_AliceBetter");
		sheet2 = wb.createSheet("Quality Code_AliceNotBetter");

		String fontName = "宋体";
		fontFTitle = createFonts(wb, Font.BOLDWEIGHT_BOLD, fontName, false, (short)12);		//Big Title
		fontContent = createFonts(wb, Font.BOLDWEIGHT_NORMAL, fontName, false, (short)11);		//content
		
		style = wb.createCellStyle();
//		style.setWrapText(true);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		
		style1 = wb.createCellStyle();
		style1.setWrapText(true);
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		// 设置前景色
		style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		style1.setFillForegroundColor(IndexedColors.TAN.getIndex());
	}

	public void closeFile() throws IOException {
		wb.write(fileout);
		fileout.close();
	}
	
	// 初始化excel 文件的头
	void init() {
		setSheetHeader(sheet);
		setSheetHeader(sheet1);
		setSheetHeader(sheet2);
	}
	void setSheetHeader(Sheet sheet){
		Row rows = sheet.createRow(0);
		float rowHeight = 20;
		rows.setHeightInPoints(rowHeight);
		
		createHeaderCell(wb, rows, 0,"input_id", fontFTitle, style1);
		createHeaderCell(wb, rows, 1,"input_address", fontFTitle, style1);
		createHeaderCell(wb, rows, 2,"alice_cleansed_address", fontFTitle, style1);
		createHeaderCell(wb, rows, 3,"Alice_QC_Old", fontFTitle, style1);
		createHeaderCell(wb, rows, 4,"FL_QC", fontFTitle, style1);
		createHeaderCell(wb, rows, 5,"firstlogic_cleansed_address", fontFTitle, style1);
		createHeaderCell(wb, rows, 6,"Which one is better_Old", fontFTitle, style1);
		createHeaderCell(wb, rows, 7,"Which one is better_New", fontFTitle, style1);
	}
	
	
	public void WriteToExcel() {
	  String example = "";
		this.readExcelToWrite(example, sourceData.get(example));
	}
	
	// 将输入文件的内容输出到输出文件的内容中
	private void readExcelToWrite(String example, List<String> sourceLine) {
		try {
			// 创建行
			Row rows = sheet.createRow(row);

			String emptyCell = "";
			//col0 input_id
			if(example != null){
				createCell(wb, rows, 0,example, fontContent, style);
			}else{
				createCell(wb, rows, 0,emptyCell, fontContent, style);
			}
			//col1	input_address
			if(example != null){
				createCell(wb, rows, 1,example, fontContent, style);
			}else{
				createCell(wb, rows, 1,emptyCell, fontContent, style);
			}
			//col2	alice_cleansed_address
			if(example != null){
				createCell(wb, rows, 2,example, fontContent, style);
			}else{
				createCell(wb, rows, 2,emptyCell, fontContent, style);
			}
			//col3	Alice_QC_Old
			if(example != null){
				createCell(wb, rows, 3,example, fontContent, style);
			}else{
				createCell(wb, rows, 3,emptyCell, fontContent, style);
			}
			
			//col4	Alice_QC_New
			if(sourceLine.get(4) != null){
				createCell(wb, rows, 4,sourceLine.get(4), fontContent, style);
			}else{
				createCell(wb, rows, 4,emptyCell, fontContent, style);
			}
			//col5	firstlogic_cleansed_address
			if(sourceLine.get(5) != null){
				createCell(wb, rows, 5,sourceLine.get(5), fontContent, style);
			}else{
				createCell(wb, rows, 5,emptyCell, fontContent, style);
			}
			//col6	Which one is better_Old
			if(sourceLine.get(6) != null){
				createCell(wb, rows, 6,sourceLine.get(6), fontContent, style);
			}else{
				createCell(wb, rows, 6,emptyCell, fontContent, style);
			}
			//col7	Which one is better_New
			createCell(wb, rows, 7,emptyCell, fontContent, style);
			

			row++;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ReadExcelError" + e);
		}

	}
	
	public static void createHeaderCell(Workbook wb, Row row, int column,
			String value, Font font, CellStyle cellStyle)
	{
		Cell cell = row.createCell(column);
		cell.setCellValue(value);

		//新增的四句话，设置CELL格式为文本格式  
        HSSFDataFormat format = (HSSFDataFormat) wb.createDataFormat();  
        cellStyle.setDataFormat(format.getFormat("@"));  
        
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		cellStyle.setFont(font);
		cellStyle.setFillBackgroundColor(HSSFColor.RED.index);	//??
		cell.setCellStyle(cellStyle);
	}
	
	public static void createCell(Workbook wb, Row row, int column,
			String value, Font font, CellStyle cellStyle)
	{
		Cell cell = row.createCell(column);
		cell.setCellValue(value);

		//新增的四句话，设置CELL格式为文本格式  
        HSSFDataFormat format = (HSSFDataFormat) wb.createDataFormat();  
        cellStyle.setDataFormat(format.getFormat("@"));  
		
		
//		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		cellStyle.setFont(font);
		cellStyle.setFillBackgroundColor(HSSFColor.RED.index);	//??
		cell.setCellStyle(cellStyle);
	}
	
	public static Font createFonts(Workbook wb, short bold, String fontName,
			boolean isItalic, short hight)
	{
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setBoldweight(bold);
		font.setItalic(isItalic);
		font.setFontHeightInPoints(hight);
		return font;
	}
}
