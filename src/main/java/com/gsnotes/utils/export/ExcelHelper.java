package com.gsnotes.utils.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.gsnotes.utils.export.ExcelHandlerException;

public class ExcelHelper {
	
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[]  HEADERS = { "ID ETUDIANT", "CNE", "NOM", "PRENOM", "ID NIVEAU ACTUEL", "Type" };
	
	public static boolean hasExcelFormat(MultipartFile file) {
	    if (!TYPE.equals(file.getContentType())) {
	      return false;
	    }
	    return true;
	  }
	
	
	
	
	public static List<ArrayList<Object>> readFromExcel(String pFileName, int pSheet) throws ExcelHandlerException {

		List<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();

		try {
			Workbook workbook = null;
			try {
				FileInputStream excelFile = new FileInputStream(new File(pFileName));
				workbook = new XSSFWorkbook(excelFile);
				Sheet datatypeSheet = workbook.getSheetAt(pSheet);
				Iterator<Row> iterator = datatypeSheet.iterator();

				while (iterator.hasNext()) {

					ArrayList<Object> rowValues = new ArrayList<Object>();

					Row currentRow = iterator.next();
					Iterator<Cell> cellIterator = currentRow.iterator();

					while (cellIterator.hasNext()) {

						Cell currentCell = cellIterator.next();

						if (currentCell.getCellType() == CellType.STRING) {

							rowValues.add(currentCell.getStringCellValue());

						} else if (currentCell.getCellType() == CellType.NUMERIC) {
							rowValues.add(currentCell.getNumericCellValue());
						}

					}

					data.add(rowValues);

				}
			} finally {
				if (workbook != null) {
					workbook.close();
				}
			}
		} catch (Exception ex) {
			throw new ExcelHandlerException("Error while importing an excel file", ex);
		}

		return data;

	}
//	public static boolean hasColumnsTypes(MultipartFile file) throws IOException {
//		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//	    XSSFSheet worksheet = workbook.getSheetAt(0);
//	    XSSFRow row = worksheet.getRow(0);
//	    
//		XSSFCell cellID = row.getCell(0);
//		XSSFCell cellCNE = row.getCell(1);
//		XSSFCell cell1NOM = row.getCell(2);
//		XSSFCell cell1PRENOM = row.getCell(3);
//		XSSFCell cell1IDNIVEAU = row.getCell(4);
//		XSSFCell cell1TYPE = row.getCell(5);
//
//		if (cellID.getStringCellValue() == "ID ETUDIANT" &&
//				cellCNE.getStringCellValue() == "CNE" &&
//						cell1NOM.getStringCellValue() == "NOM" &&
//								cell1PRENOM.getStringCellValue() == "PRENOM" &&
//										cell1IDNIVEAU.getStringCellValue() == "ID NIVEAU ACTUEL" &&
//												cell1TYPE.getStringCellValue() == "Type" ) {
//		    return true;
//		}
//		return false;
//	}
	
//	public int NumberOfColumns(MultipartFile file) {
//		FileInputStream fileInputStream = new FileInputStream(file);
//		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
//        Sheet sheet = workbook.getSheet("0");
//
//
//        int numberOfCells = 0;
//        Iterator rowIterator = sheet.rowIterator();
//        /**
//         * Escape the header row *
//         */
//        if (rowIterator.hasNext())
//        {
//            Row headerRow = (Row) rowIterator.next();
//            //get the number of cells in the header row
//            numberOfCells = headerRow.getPhysicalNumberOfCells();
//        }
//        System.out.println("number of cells "+numberOfCells);
//		
//	}

}
