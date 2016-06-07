package SupportLibraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POI_ReadExcel {
	public String eos;
	public Map<String, List<String>> fetchWithCondition(String sheetPath, String sheetName, List<String> whereClause) {
		eos="EndOfScript";
		Map<String, List<String>> excelMap = coreListToMap(sheetPath, sheetName);
		
		for (String clause : whereClause) {
			Map<String, List<String>> finalMap = new HashMap<String, List<String>>();
			List<Integer> addIndex = new ArrayList<Integer>();
			for (Map.Entry<String, List<String>> entry : excelMap.entrySet()) {
				int k = 0;
				
				if (entry.getKey().equalsIgnoreCase(clause.split("::")[0])) {
			
					List<String> vals = new ArrayList<String>();
					for (String val : new ArrayList<String>(entry.getValue())) {
						
					
						//if ((!(val.equalsIgnoreCase(eos)))&&(!(clause.split("::")[1]).isEmpty())){
						if (val.equalsIgnoreCase(clause.split("::")[1])) {
							vals.add(val);
							addIndex.add(k);
						}
						k++;

					
					//} 
					//	else{
						//	System.out.println("cursor here");
						//	break;
						//}
					finalMap.put(entry.getKey(), vals);
					}
					
				}
				}
				
			
			for (Map.Entry<String, List<String>> entry : excelMap.entrySet()) {
				List<String> vals = new ArrayList<String>();
				if (!entry.getKey().equalsIgnoreCase(clause.split("::")[0])) {
					for (int add : addIndex)
						vals.add(entry.getValue().get(add));
					finalMap.put(entry.getKey(), vals);
				}
			}
			excelMap = finalMap;
		}
		return excelMap;
	}
	
	public Map<String, List<String>> coreListToMap(String sheetPath, String sheetName) {
		List<List<String>> tempStorage = coreFetch(sheetPath, sheetName);
		Map<String, List<String>> excelMap = new HashMap<String, List<String>>();
		
		List<List<String>> tempList = new ArrayList<List<String>>();

		for(int j=0; j<tempStorage.get(0).size() ; j++){
			List<String> eachCol = new ArrayList<String>();
			for(int i=1; i<tempStorage.size(); i++){
				try{
					eachCol.add(tempStorage.get(i).get(j));
				}catch(IndexOutOfBoundsException e){
					eachCol.add("");
				}
				
			}
			tempList.add(eachCol);
		}
		
		for(int i=0; i<tempList.size(); i++){
			excelMap.put(tempStorage.get(0).get(i), tempList.get(i));
		}
		return excelMap;
	}
	
	public List<List<String>> coreFetch(String sheetPath, String sheetName) {
		List<List<String>> tempStorage = new ArrayList<List<String>>();
		FileInputStream file = null;
		XSSFWorkbook workbook = null;
		RemoveBlankRows(sheetPath,sheetName);
		try {
			
			file = new FileInputStream(new File(sheetPath));
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);
		
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				List<String> rowWise = new ArrayList<String>();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				int i = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					
					if(i != cell.getColumnIndex()){
                        for(int z=i; z<cell.getColumnIndex(); z++){
                              rowWise.add("");
                        }                                   
					}else if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
                        rowWise.add("");
					}

					
					i = cell.getColumnIndex()+1;
					
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						rowWise.add(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						rowWise.add(Integer.toString((int) (cell.getNumericCellValue())));
						break;
					
					}
				}
				tempStorage.add((ArrayList<String>) rowWise);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (file != null)
					file.close();
				if (workbook != null)
					workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return tempStorage;
	}
	
	// This method will remove blank rows(formatted) from data sheets.
	
	public void RemoveBlankRows(String sheetPath,String sheetName ){
		
		FileInputStream file = null;
		XSSFWorkbook workbook = null;

		

        boolean stop = false;
        boolean nonBlankRowFound;
        short c;
        XSSFRow lastRow = null;
        XSSFCell cell = null;
  try{
		file = new FileInputStream(new File(sheetPath));
		workbook = new XSSFWorkbook(file);
		
		XSSFSheet hsheet = workbook.getSheet(sheetName);
        while (stop == false) {
        	
            nonBlankRowFound = false;
            lastRow = hsheet.getRow(hsheet.getLastRowNum());
            for (c = lastRow.getFirstCellNum(); c <= lastRow.getLastCellNum(); c++) {
                cell = lastRow.getCell(c);
                if (cell != null && lastRow.getCell(c).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
                    nonBlankRowFound = true;
                }
            }
            if (nonBlankRowFound == true) {
                stop = true;
            } else {
                hsheet.removeRow(lastRow);
            }
        }
        file.close();
        FileOutputStream output_file =new FileOutputStream(new File(sheetPath));  //Open FileOutputStream to write updates
        
        workbook.write(output_file); //write changes
          
        output_file.close();

  }
  catch (Exception e) {
		e.printStackTrace();
	}

  
		
		}
}
