package SupportLibraries;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;

import org.ini4j.Ini;
import org.ini4j.Wini;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.apache.commons.io.FileUtils;

import SupportLibraries.Report.Status;

public class Util{
	public static Properties prop = null;
	public static FileInputStream propFile = null;
	
	public static String getRootPath() throws IOException{
		return new File (".").getCanonicalPath();
	}
		
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: getData
	 * Description: This method is to fetch Data from the column in the Input sheet against the test case that is currently executing based on the current iteration
	 * Return Type: String
	 * @throws Exception 
	 */
	public String getData(String colName, WebDriver driver, String scenario, String testCase, String homePath, Integer currentIteration, String browser, String passScreenshot, String browserFolder) throws Exception{
		String dataValue = "";
		
		//String dbPath = homePath + "\\TestData\\"+testCase+".xlsx";
		String dbPath = homePath + "\\TestData\\"+scenario+".xlsx";
		POI_ReadExcel poiObject = new POI_ReadExcel();
		Report reportObject = new Report();
		try {
			// TODO Auto-generated method stub
			
			List<String> whereClause = new ArrayList<String>();
			whereClause.add("TestScript::"+testCase);
			whereClause.add("Iteration::"+currentIteration.intValue());
			Map<String, List<String>> result = poiObject.fetchWithCondition(dbPath, "TestData", whereClause);
			
			if(result.get("TestScript").size()==0){
				reportObject.Log("Blank column in Test Data", "There is no data in the column "+colName+" for the Iteration "+currentIteration+" of test case "+testCase, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
			
			for(int i=0; i<result.get("TestScript").size(); i++){
				if(testCase.equalsIgnoreCase(result.get("TestScript").get(i)) && currentIteration.intValue()==Integer.parseInt(result.get("Iteration").get(i))){
					dataValue = result.get(colName).get(i);
					break;
				}
			}
			
			return dataValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reportObject.Log("Unhandled Exception in reading Data", "There has been an unhandled exception "+e+" while reading data from the Test Data sheet", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			throw e;
		}
		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: getCurrentDate
	 * Description: This method is get the Current Date in the format 'MM-dd-yyyy'
	 * Return Type: String
	 */
	public static String getCurrentDate(){
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String date = sdf.format(cal.getTime()).toString();
		return date;
		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: getCurrentTime
	 * Description: This method is get the Current Time in the format 'hh-mm-ssa'
	 * Return Type: String
	 */
	public static String getCurrentTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh-mm-ssa");
		String time = sdf.format(cal.getTime()).toString();
		return time;
		
	}
	
		
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: CaptureScreenshot
	 * Description: This method is capture the screenshot of the current page
	 * Return Type: Nothing
	 */
	public void CaptureScreenshot(String screenshotPath, WebDriver driver){
		
		try {
			if(getValueFromConfigProperties("SnapshotType").equalsIgnoreCase("Desktop")){
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Dimension screenSize = toolkit.getScreenSize();
				Robot robot = new Robot();
				Rectangle rectangle = new Rectangle(0, 0, screenSize.width-15, screenSize.height);
				BufferedImage image = robot.createScreenCapture(rectangle);
				ImageIO.write(image, "png", new File(screenshotPath));
			}else if(getValueFromConfigProperties("SnapshotType").equalsIgnoreCase("Browser")){
				File screenshot = null;
				if(getValueFromConfigProperties("GridSetup").equalsIgnoreCase("Yes")){
					WebDriver augmentedDriver = new Augmenter().augment(driver);
					screenshot = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
				}else{
					screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				}				
				FileUtils.copyFile(screenshot, new File(screenshotPath));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: getValue
	 * Description: This method is read the value of a key from Properties.ini file in the framework
	 * Return Type: String
	 */
	public String getDataINI(String sectionName, String key){
		
		String value = "";
		try{
			String homePath = new File (".").getCanonicalPath();
			File f = new File(homePath+"\\OutputDataINI.ini");
			
			Ini a = new Ini();
			a.load(f);
			value = a.get(sectionName, key);
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: setDataINI
	 * Description: This method is to write a value of a key to OutputData.ini file in the framework
	 * Return Type: Nothing
	 */
	public void setDataINI(String key,String value, String testCase, String scenario){
		
		try{
			
			String homePath = new File (".").getCanonicalPath();
			File f = new File(homePath+"\\OutputDataINI.ini");
			
			Wini a = new Wini(f);
			a.load(f);
			//a.remove(scenario+"_"+testCase);
			a.put(scenario+"_"+testCase, key, value);
			
			a.store();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
		
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: getValueFromConfigProperties
	 * Description: This method is to retrieve value from Config.properties file for the key given
	 * Return Type: String
	 */
	public static String getValueFromConfigProperties(String key){
		String value = "";
		prop = new Properties();
		try {
			propFile = new FileInputStream (new File(".").getCanonicalPath()+"\\Config.properties");
			prop.load(propFile);
			value = prop.getProperty(key);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return value;		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: getObjectFromObjectMap
	 * Description: This method is to retrieve object from the particular module ObjectMap.properties file for the key given
	 * Return Type: String
	 */
	public String getObjectFromObjectMap(String key, String scenario){
		String value = "";
		prop = new Properties();
		try {
			propFile = new FileInputStream (new File(".").getCanonicalPath()+"\\ObjectRepository\\"+scenario+".properties");
			prop.load(propFile);
			if(key!=null)
				value = prop.getProperty(key);
			else
				value = null;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//Report.Log("Object Reference Name Wrong","The Object Reference Name given is not present/matching with Object Map", Status.FAIL);
		}
		return value;
	}
}