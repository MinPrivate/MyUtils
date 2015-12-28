package com.min.utils.file.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowReader {
	public RowReader(){}

	/**
	 * 业务逻辑实现方法
	 * 
	 * @see com.eprosun.util.excel.IRowReader#getRows(int, int, java.util.List)
	 */
	//保存输入地址的ID集合
	ArrayList<String> lines = new ArrayList<String>();
	//保存输入地址的ID Address值对集合
	Map<String,String> addressMap = new HashMap<String, String>();

	public ArrayList<String> getLines() {
		return lines;
	}
	
	public Map<String,String> getAddressMap() {
		return addressMap;
	}

	public void getRows(int sheetIndex, int curRow, List<String> rowlist) {
//		if (curRow == 0) {
//			return;
//		}//将第一行读取
		
//		for (int i = 1; i < rowlist.size(); i++) {
//			String originalId = rowlist.get(0);
//			String value = rowlist.get(i);
//			// System.out.println(value);
//			lines.add(originalId + " " + value);
//		}
		String line = "";
//		for (int i = 0; i < rowlist.size(); i++) {
//			line += rowlist.get(i) + ",";
//		}
		line = rowlist.get(0);
		
//		line = line.substring(0, line.length()-1);//去掉最后一个,
		lines.add(line);
		addressMap.put(line, rowlist.get(1));
	}
}