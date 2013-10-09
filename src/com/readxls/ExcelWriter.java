package com.readxls;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.logs.Logging;
//import org.apache.poi.hssf.usermodel.HSSFHyperlink;


public class ExcelWriter {
	//public static String filename = System.getProperty("user.dir")+"\\src\\com\\config\\SuiteController.xlsx";
	public  String path;
	public  FileInputStream fis = null;
	public  FileOutputStream fileOut =null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row   =null;
	private XSSFCell cell = null;
	
	public ExcelWriter(String path) {
		
		this.path=path;
		
		openWorkbook(path); 
		
	}
	private void openWorkbook(String path) {
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(new BufferedInputStream(fis));
			sheet = workbook.getSheetAt(0);
			fis.close();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getCellData(String sheetName,String colName,int rowNum){
		try{
			if(rowNum <=0)
				return "";
		
		int index = workbook.getSheetIndex(sheetName);
		int col_Num=-1;
		if(index==-1)
			return "";
		
		sheet = workbook.getSheetAt(index);
		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
				col_Num=i;
		}
		if(col_Num==-1)
			return "";
		
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		cell = row.getCell(col_Num);
		
		if(cell==null)
			return "";
		//System.out.println(cell.getCellType());
		if(cell.getCellType()==Cell.CELL_TYPE_STRING)
			  return cell.getStringCellValue();
		else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
			  
			  String cellText  = String.valueOf(cell.getNumericCellValue());
			  if (HSSFDateUtil.isCellDateFormatted(cell)) {
		           // format in form of M/D/YY
				  double d = cell.getNumericCellValue();

				  Calendar cal =Calendar.getInstance();
				  cal.setTime(HSSFDateUtil.getJavaDate(d));
		            cellText =
		             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
		           cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +
		                      cal.get(Calendar.MONTH)+1 + "/" + 
		                      cellText;
		           
		           //System.out.println(cellText);

		         }

			  
			  
			  return cellText;
		  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
		      return ""; 
		  else 
			  return String.valueOf(cell.getBooleanCellValue());
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
	}
	
	// returns the row count in a sheet
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data){
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);
		

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		

		row=sheet.getRow(0);
		for(int i=0;i<20;i++){ // fixing max column search value to 20 as, have row.getLastCellNum is giving error when we try to write in a newly created sheet
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		}
		if(colNum==-1)
			return false;

		
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null) {
	        cell = row.createCell(colNum);
//	        cell.setCellStyle()
		}

	    // cell style
	    //CellStyle cs = workbook.createCellStyle();
	    //cs.setWrapText(true);
	    //cell.setCellStyle(cs);
		cell.setCellValue(data);
	    //sheet.autoSizeColumn(colNum); 
	    sheet.setColumnWidth(colNum, 5000);
	    
	
		    
	    try{
	    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
	    	workbook.write(brOut);
		    brOut.close();
	    }catch(Exception e){
	    	Thread.sleep(2000);
	    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
	    	workbook.write(brOut);
		    brOut.close();
	    }
    	
	   
	    
	    
		//workbook.write(fileOut);
		//fileOut.close();	
	    fis.close();
	    
	    openWorkbook(path); 

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getCellData(String sheetName,int colNum,int rowNum){
		try{
			if(rowNum <=0)
				return "";
		
		int index = workbook.getSheetIndex(sheetName);

		if(index==-1)
			return "";
		
	
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		cell = row.getCell(colNum);
		if(cell==null)
			return "";
	  if(cell.getCellType()==Cell.CELL_TYPE_STRING)
		  {
		  	try{
		  	return cell.getStringCellValue();
		  	}
		  	catch(Exception ex){
		  		return cell.toString();
		  	}
		  }
	  else
	   if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
		  
		  String cellText  = String.valueOf(cell.getNumericCellValue());
		  if (HSSFDateUtil.isCellDateFormatted(cell)) {
	           // format in form of M/D/YY
			  double d = cell.getNumericCellValue();

			  Calendar cal =Calendar.getInstance();
			  cal.setTime(HSSFDateUtil.getJavaDate(d));
	            cellText =
	             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
	           cellText = cal.get(Calendar.MONTH)+1 + "/" +
	                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
	                      cellText;
	           
	          // System.out.println(cellText);

	         }

		  
		  
		  return cellText;
	  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
	      return "";
	  else 
		  return String.valueOf(cell.getBooleanCellValue());
		}
		catch(Exception e){
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
		}
	}
	
	
	public boolean setCellDataColNo(String sheetName,int colNo,int rowNum, String data){
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);
		

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		//int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		

		row=sheet.getRow(0);
		for(int i=0;i<20;i++){ // fixing max column search value to 20 as, have row.getLastCellNum is giving error when we try to write in a newly created sheet
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			//if(row.getCell(i).getStringCellValue().trim().equals(colName))
			//	colNum=i;
		}
		//if(colNum==-1)
		//	return false;

		
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNo);	
		if (cell == null) {
	        cell = row.createCell(colNo);
//	        cell.setCellStyle()
		}

	    // cell style
	    //CellStyle cs = workbook.createCellStyle();
	    //cs.setWrapText(true);
	    //cell.setCellStyle(cs);
		cell.setCellValue(data);
	    //sheet.autoSizeColumn(colNum); 
	    sheet.setColumnWidth(colNo, 5000);
	    
	
		    try{
		    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
		    	workbook.write(brOut);
			    brOut.close();
		    }catch(Exception e){
		    	Logging.log("GOT PROCESS LOCK EXCEPTION");
		    	Thread.sleep(2000);
		    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
		    	workbook.write(brOut);
			    brOut.close();
		    }
	    	

	   
	    
	    
		//workbook.write(fileOut);
		//fileOut.close();	
	    fis.close();
	    
	    openWorkbook(path); 

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data,String url){
		//System.out.println("setCellData setCellData******************");
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		//System.out.println("A");
		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
				colNum=i;
		}
		
		if(colNum==-1)
			return false;
		sheet.autoSizeColumn(colNum); //ashish
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
			
	    cell.setCellValue(data);
	    XSSFCreationHelper createHelper = workbook.getCreationHelper();

	    //cell style for hyperlinks
	    //by default hypelrinks are blue and underlined
	    CellStyle hlink_style = workbook.createCellStyle();
	    XSSFFont hlink_font = workbook.createFont();
	    hlink_font.setUnderline(XSSFFont.U_SINGLE);
	    hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);
	    //hlink_style.setWrapText(true);

	    XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
	    link.setAddress(url);
	    cell.setHyperlink(link);
	    cell.setCellStyle(hlink_style);
	    sheet.setColumnWidth(colNum, 5000);
	    try{
	    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
	    	workbook.write(brOut);
		    brOut.close();
	    }catch(Exception e){
	    	Thread.sleep(2000);
	    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
	    	workbook.write(brOut);
		    brOut.close();
	    }
    	
	    
	    openWorkbook(path);

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	// returns true if sheet is created successfully else false
	public boolean addSheet(String  sheetname){		
		
		FileOutputStream fileOut;
		try {
			 workbook.createSheet(sheetname);	
			    //sheet.setColumnWidth(colNum, 5000);
			    try{
			    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
			    	workbook.write(brOut);
				    brOut.close();
			    }catch(Exception e){
			    	Thread.sleep(4000);
			    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
			    	workbook.write(brOut);
				    brOut.close();
			    }
		    			    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// returns true if sheet is removed successfully else false if sheet does not exist
	public boolean removeSheet(String sheetName){		
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return false;
		
		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
		    //sheet.setColumnWidth(colNum, 5000);
		    try{
		    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
		    	workbook.write(brOut);
			    brOut.close();
		    }catch(Exception e){
		    	Thread.sleep(2000);
		    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
		    	workbook.write(brOut);
			    brOut.close();
		    }
	    				    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	// returns true if column is created successfully
	public boolean addColumn(String sheetName,String colName){
		//System.out.println("**************addColumn*********************");
		
		try{				
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1)
				return false;
			
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		sheet=workbook.getSheetAt(index);
		
		row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);
		
		//cell = row.getCell();	
		//if (cell == null)
		//System.out.println(row.getLastCellNum());
		if(row.getLastCellNum() == -1)
			cell = row.createCell(0);
		else
			cell = row.createCell(row.getLastCellNum());
	        
	        cell.setCellValue(colName);
	        cell.setCellStyle(style);
	        
		    //sheet.setColumnWidth(colNum, 5000);
		    try{
		    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
		    	workbook.write(brOut);
			    brOut.close();
		    }catch(Exception e){
		    	Thread.sleep(2000);
		    	BufferedOutputStream brOut = new BufferedOutputStream(new FileOutputStream(path));
		    	workbook.write(brOut);
			    brOut.close();
		    }
	    		    

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	// removes a column and all the contents
  // find whether sheets exists	
	public boolean isSheetExist(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1){
			index=workbook.getSheetIndex(sheetName.toUpperCase());
				if(index==-1)
					return false;
				else
					return true;
		}
		else
			return true;
	}
	
}

