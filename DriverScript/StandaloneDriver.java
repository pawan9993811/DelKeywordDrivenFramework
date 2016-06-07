package DriverScript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

import org.apache.poi.ddf.EscherColorRef.SysIndexSource;
import org.apache.poi.hslf.model.Sheet;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import SupportLibraries.POI_ReadExcel;
import SupportLibraries.Report;
import SupportLibraries.ExecuteScripts;
import SupportLibraries.Util;
import SupportLibraries.Report.Status;

public class StandaloneDriver {
	
	static String resultsFolder = "";
    static String mainresultsFolder="";
	/**
	 * @param args
	 * Author: Santosh Kumar Anupati
	 * Description: Execute for Standalone execution when Grid Setup is marked No in Config.properties file
	 */
	public static void main(String[] args) {
		String port="";
		// TODO Auto-generated method stub
		Util util = new Util();
		String gridSetup = util.getValueFromConfigProperties("GridSetup");
		if(gridSetup.equalsIgnoreCase("Yes")){
			JOptionPane.showMessageDialog(null,"Grid Setup is Set to Yes in Config file, please execute testNG.xml", "Configuration Error", JOptionPane.ERROR_MESSAGE);
		}else{
			String homePath="";
			Map<String, List<String>> result1=null;
			XSSFWorkbook workBook = null;
		
			int numberOfSheets=0;
			int numberOfExecutedTCs=0;
			try {
				homePath = new File (".").getCanonicalPath();
				String path = homePath+"\\TestCaseSelection_Singlesheet.xlsx";
								
				POI_ReadExcel poiObject = new POI_ReadExcel();
				FileInputStream file = new FileInputStream(new File(path));
				workBook = new XSSFWorkbook(file);
				numberOfSheets = workBook.getNumberOfSheets();
				
				workBook.close();
				
				numberOfExecutedTCs = 0;
				
				
				
				for(int j=0;j<numberOfSheets;j++){
					List<String> whereClause1 = new ArrayList<String>();
					whereClause1.add("Execute::Yes");
					result1 = poiObject.fetchWithCondition(path, workBook.getSheetName(j), whereClause1);
					numberOfExecutedTCs = numberOfExecutedTCs+result1.get("Execute").size();
				}
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String[] ListOfBrowsers = null;
			ListOfBrowsers = util.getValueFromConfigProperties("Browser").split(",");

			ExecuteScripts driverObject = new ExecuteScripts();
			WebDriver driver = null;
			for(int i=0;i<ListOfBrowsers.length;i++){
				ExecuteScripts testngDriver = new ExecuteScripts();
				
				if(numberOfExecutedTCs!=0){
					driver = driverObject.SelectDriver(ListOfBrowsers[i],port);
					driver.manage().window().maximize();
					if(i==0)
					{
						mainresultsFolder=testngDriver.CreateDateFolder(homePath);
						resultsFolder = testngDriver.CreateExecutionFolder(mainresultsFolder);
					//	resultsFolder = testngDriver.CreateExecutionFolder(homePath);
					}	
					for(int k=0;k<numberOfSheets;k++){
						if(util.getValueFromConfigProperties("TestCaseSelection_Format").equalsIgnoreCase("SingleSheet")){
							driverObject.ExecuteDriver(driver,workBook.getSheetName(k),ListOfBrowsers[i],resultsFolder);
						}else if(util.getValueFromConfigProperties("TestCaseSelection_Format").equalsIgnoreCase("Modular")){
							driverObject.ExecuteModularDriver(driver,workBook.getSheetName(k),ListOfBrowsers[i],resultsFolder);
						}
						
					}
				}
				
				driver.manage().deleteAllCookies();
				
				try {
					driver.close();
					driver.quit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Report reportObject = new Report();
			reportObject.CloseSummary(resultsFolder);
			
		}
				
	}	
}