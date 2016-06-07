/**
 * 
 */
package ReusableFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;

/**
 * 
 * Responsible for creating defect log in excel sheet 
 * as per the format required for the defects to be created in RTC
 * @author skandacharam
 *
 */
public class DefectLogger {

	private List<String> scenarioDefectList;
	private Map<Integer, List<Map<String, String>>> testcaseDefectList;
	private List<String> headerList = new ArrayList<String>();
	private List<String> dataList = new ArrayList<String>();
	private String summaryTemplate;
	private String descHeaderTemplate;
	private String descStepTemplate;
	private String logFolder;	
	private XSSFCellStyle headerCellStyle;
	private XSSFCellStyle dataCellStyle;
	
	private static final Logger logger = Logger.getLogger(DefectLogger.class.getName());

	private static final String DEFECT_LOG_FILE_PREFIX = "AUT-Defect-Log-";
	
	public DefectLogger(List<String> scenarioDefectList, Map<Integer, List<Map<String, String>>> testcaseDefectList, String logFolder) {
		this.scenarioDefectList = scenarioDefectList;
		this.testcaseDefectList = testcaseDefectList;
		this.logFolder = logFolder;
		loadProperties();
	}
	
	/**
	 * This is the only method that should be called by the caller
	 */
	public void createLog(){
		int numberOfDefects = scenarioDefectList.size();
		logger.info("Number of defects to log:"+numberOfDefects);
		if(numberOfDefects > 0){
	        XSSFWorkbook workbook = new XSSFWorkbook();	        
	        createCellStyles(workbook);
	        XSSFSheet sheet = workbook.createSheet("Unit Testing Defect Template");		        
	        createHeader(sheet);
	        createAllDefects(sheet);	        
	        writeToLogFile(workbook);	        
	        try {
				workbook.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}else{
			logger.info("There are no defects to create log.");
		}
	}
	
	/*
	 * Create the required cell styles for the defect log sheet
	 */
	private void createCellStyles(XSSFWorkbook workbook){
        headerCellStyle = getHeaderCellStyle(workbook);
        dataCellStyle = getDataCellStyle(workbook);
	}
	
	
	/*
	 * Create the header with predefined format including auto size
	 */
	private void createHeader(XSSFSheet sheet){
		logger.info("Creating header");
		Row row = sheet.createRow(0);		
		int columnCount = 0;		
		for(String columnTitle : headerList){
			Cell cell = row.createCell(columnCount++);			
			cell.setCellValue(columnTitle);
			cell.setCellStyle(headerCellStyle);			
		}
	}
	
	private void formatSheet(XSSFSheet sheet){
		int columnCount = 0;
		System.getProperties().setProperty("java.awt.headless", "true");
		for(String columnTitle : headerList){
			sheet.autoSizeColumn(columnCount++);			
		}
	}

	private void createAllDefects(XSSFSheet sheet){
		logger.info("Creating defect entries");
		int totalNumOfEntries = scenarioDefectList.size();
		int rowCount = 1;
		for(int keyIndex = 0; keyIndex < totalNumOfEntries; keyIndex++){
			String[] scenarioAndTestCase = scenarioDefectList.get(keyIndex).split("~");
			String summaryData =  getFormattedString(summaryTemplate, scenarioAndTestCase);
			String descHeaderData = getFormattedString(descHeaderTemplate, scenarioAndTestCase);
			String descStepData = getDefectDescDetails(testcaseDefectList.get(keyIndex));
			createDefect(sheet.createRow(rowCount++),summaryData,descHeaderData,descStepData, sheet);
		}		
	}
	
	/*
	 * Create the data row with predefined format including auto size
	 */
	private void createDefect(Row row, String summaryData,String descHeaderData,String descStepData,XSSFSheet sheet){
		logger.info("Creating a defect");
		String[] data = getDefectData(summaryData,descHeaderData,descStepData);
		int columnCount = 0;		
		for(String cellData : data){
			Cell cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellStyle);
			cell.setCellValue(cellData);			
		}
	}
	
	/*
	 * As per the pre-defined format column 12 should contain defect summary
	 * and column 13 should contain defect details
	 */
	private String[] getDefectData(String summaryData,String descHeaderData,String descStepData){
		String[] data = new String[dataList.size()];
		data = dataList.toArray(data);
		data[12] = summaryData;
		data[13] = descHeaderData + descStepData;
		return data;
	}
	
	private String getDefectDescDetails(List<Map<String, String>> detailsList){
		StringBuffer sb = new StringBuffer();
		for(Map<String, String> stepDetails : detailsList){
			String[] stepDataDetails = {stepDetails.get("no"),stepDetails.get("name"),stepDetails.get("desc"),stepDetails.get("img")};
			sb.append("\n");
			sb.append(getFormattedString(descStepTemplate, stepDataDetails));
		}
		return sb.toString();
	}	
	
	private void loadProperties(){		
		ResourceBundle bundle = ResourceBundle.getBundle("ReusableFunctions.defectlog");
		loadHeaderAndData(bundle);
		loadTemplates(bundle);
		ResourceBundle.clearCache();
	}
	
	/*
	 * As the column info is based on the pre-defined format,
	 * column numbering is used as criteria to load the properties using a counter
	 */
	private void loadHeaderAndData(ResourceBundle bundle){
		try{
			int index = 0;			
			while(bundle.getString("header.column."+(++index)) != null){
				headerList.add(bundle.getString("header.column."+(index)));
				dataList.add(bundle.getString("data.column."+(index)));
			}
		}catch(MissingResourceException e){
			//e.printStackTrace();
		}		
	}
				
	
	private void loadTemplates(ResourceBundle bundle){
		summaryTemplate = bundle.getString("data.summary");
		descHeaderTemplate = bundle.getString("data.description.header");
		descStepTemplate = bundle.getString("data.description.step");
	}
	
	private String getFormattedString(String template, String[] arguments){
		MessageFormat formatter = new MessageFormat("");
		formatter.applyPattern(template);
		return formatter.format(arguments);
	}
	
	private void writeToLogFile(XSSFWorkbook workbook){
		try {
			logger.info("writing to defect log file");
			String filename = logFolder + File.separatorChar +getDefectLogFileName(logFolder);
			FileOutputStream outputStream = new FileOutputStream(filename);			
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDefectLogFileName(String defectLogFolder){
		if(defectLogFolder != null){
			String[] folderSplit = defectLogFolder.split("RegressionRun_");
			if(folderSplit.length > 1){
				return DefectLogger.DEFECT_LOG_FILE_PREFIX + folderSplit[1]+ ".xlsx";
			}
		}
		return "DefectLog.xlsx";		
	}	
	
	/*
	 * only header specific style is changed all other remain common to all cells
	 */
	private static XSSFCellStyle getHeaderCellStyle(XSSFWorkbook workbook){
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle = getDataCellStyle(workbook);									
		cellStyle.setFillBackgroundColor(new XSSFColor(new Color(0,20,118)));		
		cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
		XSSFFont font = workbook.createFont();
		font.setColor(new XSSFColor(Color.WHITE));
		cellStyle.setFont(font);		
		return cellStyle;
	}
	
	/*
	 * Data cell style for alignment and border, which are common for all cells
	 */
	private static XSSFCellStyle getDataCellStyle(XSSFWorkbook workbook){
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);		
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.TOP);		
		return cellStyle;
	}	
	
	public static void main(String[] args){
		test();
	}
	
	/*
	 * Simple test case to test the file creation as per the format
	 */
	private static void test(){
		List<String> scenarioDefectList = new ArrayList<String>();
		scenarioDefectList.add("Scenario-1~TestCase-5");
		scenarioDefectList.add("Scenario-2~TestCase-7");
		
		Map<Integer, List<Map<String, String>>> testcaseDefectList = new HashMap<Integer, List<Map<String, String>>>();
		
		Map<String, String> scn1Map = new HashMap<String, String>();
		scn1Map.put("no", "4");
		scn1Map.put("name", "test case intake");
		scn1Map.put("desc", "case intake failed while entering person details");
		scn1Map.put("img", "tc5-4-img.png");
		
		Map<String, String> scn2Map = new HashMap<String, String>();
		scn2Map.put("no", "9");
		scn2Map.put("name", "test case intake 9");
		scn2Map.put("desc", "9 case intake failed while entering person details");
		scn2Map.put("img", "9tc5-4-img.png");	
		
		List<Map<String, String>> scn1List = new ArrayList<Map<String, String>>();
		scn1List.add(scn1Map);
		scn1List.add(scn2Map);
		
		testcaseDefectList.put(0, scn1List);
		
		scn1Map = new HashMap<String, String>();
		scn1Map.put("no", "15");
		scn1Map.put("name", "15 test case intake");
		scn1Map.put("desc", "15 case intake failed while entering person details");
		scn1Map.put("img", "15tc5-4-img.png");
		
		scn1List = new ArrayList<Map<String, String>>();
		scn1List.add(scn1Map);	
		
		testcaseDefectList.put(1, scn1List);
		
		DefectLogger logger = new DefectLogger(scenarioDefectList,testcaseDefectList, "C://AUT");
		logger.createLog();
	}

}
