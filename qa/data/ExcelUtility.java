package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/* ---------------------------------- NOTE: --------------------------------------------
 * Author: Vidan Gjozev
 * This utility will not work without the external libraries
 * You can download them from: http://poi.apache.org/download.html (Binary)
 * Extract the zip file, add all libs except for log4j (will throw unnecessary warnings)
 */

public class ExcelUtility {

	private static XSSFWorkbook ExcelWBook;
	private static XSSFSheet ExcelWSheet;
	private static XSSFCell Cell;
	private static XSSFRow Row;
	
/*	Different methods of loading the file:
	static InputStream inputStream = ExcelUtility.class.getResourceAsStream("src/FunctionalTestData.xlsx");
	
	static String filePath = inputStream.toString();
	
 	static URL url = ExcelUtility.class.getClassLoader().getResource("FunctionalTestData.xlsx");*/
	  
	
	
	// Set File path and Open Excel file
	// @params - Excel Path and Sheet Name
	public static void setExcelFile(String path, String sheetName) throws Exception {
		
		try {
			// Open the Excel file
			FileInputStream excelFile = new FileInputStream(path);
			
			
			// Access the data sheet
			ExcelWBook = new XSSFWorkbook(excelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
		} catch (Exception e) {
			throw (e);
		}
	}
	
	// Read the test data from the excel file
	// @params - Row num and Col num
	public static String getCellData(int rowNum, int colNum) throws Exception {
		try {
			Cell = ExcelWSheet.getRow(rowNum).getCell(colNum);
			String cellData = Cell.getStringCellValue();
			return cellData;
		} catch (Exception e) {
			return "ERROR: Empty cell. Check row & column values.";
		}
	}
	
	
	 // Write in the Excel cell
	 // @params - Row num and Col num
	/** 
	  	public static void setCellData(String Result, int RowNum, int ColNum)
			throws Exception {
		try {
			Row = ExcelWSheet.getRow(RowNum);
			Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Result);
			} else {
				Cell.setCellValue(Result);
			}

			// For Future testing:
			//   Open the file to write the results
			FileOutputStream fileOut = new FileOutputStream(
					Constants.File_Path + Constants.File_Name);

			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
		} catch (Exception e) {
			throw (e);
		}
	}*/
}
