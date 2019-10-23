package com.test;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;

public class Test {

	public static void main(String[] args) throws Exception {
		// textlists存放列表中要展示的字符串
		String[] textlists = { "doudou1", "doudou2", "doudou3" };
		// 生成一个工作簿对象
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个名称为Info的表单
		HSSFSheet sheet = workbook.createSheet("Info");

		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFillBackgroundColor(HSSFColorPredefined.RED.getIndex());
		//cellStyle.setFillForegroundColor(HSSFColorPredefined.RED.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("测试");
		cell.setCellStyle(cellStyle);

		// 设置下拉列表作用的单元格(firstrow, lastrow, firstcol, lastcol)
		CellRangeAddressList regions = new CellRangeAddressList(1, 100, 0, 3);
		// 生成并设置数据有效性验证
		DVConstraint constraint = DVConstraint
				.createExplicitListConstraint(textlists);
		HSSFDataValidation data_validation_list = new HSSFDataValidation(
				regions, constraint);
		// 将有效性验证添加到表单
		sheet.addValidationData(data_validation_list);

		try {
			OutputStream os = new FileOutputStream("F:\\2.xls");
			workbook.write(os); // 将工作簿内容输出到Excel文件中
			os.close();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}