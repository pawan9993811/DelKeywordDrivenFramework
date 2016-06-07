package ReusableFunctions;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import SupportLibraries.*;
import SupportLibraries.Report.Status;

public class ReusableFunctions{
	
	public long timeout = 30000;
	
	/**
	 * Author: Madhulika Mitra
	 * Method Name: EndOfScript
	 * Return Type: Nothing
	 * Description: This method marks the end of the script. It is to move to the next test, if there is none then end the execution
	 */
	public void EndOfScript(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();	
			
			reportObject.Log("The script is complete",objectName,Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			while(true){
				break;
			}
			
			}
			
	

	
	/**
	 * Method Name: Login
	 * Description: This method is to Login to the Application
	 * Parameters: Nothing
	 * Return Type: Nothing
	 * @throws Exception 
	 */	
	public void Login(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) throws Exception{
		Report reportObject = new Report();
		Util utilObject = new Util();
		Thread.sleep(5000);
		
		if(driver.findElements(By.xpath("//div[@id='userNavButton' and @class='menuButtonButton']")).size()!=0){
			reportObject.Log("Verification of Home Page","The user logged in successfully", Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
		else{
			if(driver.findElements(By.id("username")).size()!=0){
				reportObject.Log("Verification of Login Page","The Login page is displayed", Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				//Launching the URL and maximizing the window
				driver.get(utilObject.getValueFromConfigProperties("URL"));
				driver.manage().window().maximize();
				if(driver.findElement(By.id("username")).isDisplayed()){
					reportObject.Log("Verification of Login Page","The Login page is displayed", Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
				}else{
					reportObject.Log("Verification of Login Page","The Login page is not displayed", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
				}			
			}
			driver.findElement(By.id("username")).clear();
			driver.findElement(By.id("username")).sendKeys(utilObject.getData("Username", driver, scenario, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder));
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(utilObject.getData("Password", driver, scenario, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder));
			
			driver.findElement(By.xpath("//button[@id='Login' and @name='Login']")).click();
			driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);
			
			if(driver.findElements(By.linkText("Remind me later. Just log me in »")).size()!=0){
				driver.findElement(By.linkText("Remind me later. Just log me in »")).click();
				driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);
			}
			
			if(driver.findElements(By.xpath("//div[@id='userNavButton' and @class='menuButtonButton']")).size()!=0){
				reportObject.Log("Verification of Home Page","The user logged in successfully", Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				reportObject.Log("Verification of Home Page","The user did not log in successfully", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
		}		
	}
			
	public boolean switchWindow(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) throws IOException {
		Report reportObject = new Report();
		String title = dataValue;
	    String currentWindow = driver.getWindowHandle();
	    Set <String> availableWindows = driver.getWindowHandles();
	    if (!availableWindows.isEmpty()) {
		    for (String windowId : availableWindows) {
			    if (driver.switchTo().window(windowId).getTitle().equals(title)) {
			    	driver.switchTo().window(windowId);
			    	reportObject.Log("Switching Window", "Switched the control to the Window with the title "+title, Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			    	return true;
			    }else {
			        driver.switchTo().window(currentWindow);
			       
			    }
		    }
	    }
	    reportObject.Log("Switching Window", "Did not find a window with the title "+title+". Hence, staying in the current window itself", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	    return false;   
	    }
	
	/**
	 * Method Name: Logout
	 * Description: This method is to Logout of the Application
	 * Parameters: Nothing
	 * Return Type: Nothing
	 */
	public void Logout(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		Util utilObject = new Util();
		
			if(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed()){
				//if(driver.findElement(By.xpath("//a[contains(text(),'Log Out') and @title='Log Out']")).isDisplayed()){
				reportObject.Log("Verification of Presence of Logout Link","The Log out link is displayed", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
				/*driver.findElement(By.xpath("//a[contains(text(),'Log Out') and @title='Log Out']")).click();
				try {
					Alert alert = driver.switchTo().alert();
					alert.accept();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				}
				driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);*/
				driver.navigate().to(utilObject.getValueFromConfigProperties("URL"));
				if(driver.findElements(By.id("username")).size()!=0){
					reportObject.Log("Verification of Login Page","The Login page is displayed", Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
				}
			}else{
				reportObject.Log("Verification of Presence of Logout Link","The Log out link is not displayed", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
	}
	
	public void selectPhoneType(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		
		try {
			Util util = new Util();
			if(browser.equalsIgnoreCase("Firefox")){
				try {
					driver.findElement(By.cssSelector("#new_row_PhoneType")).click();
					driver.findElement(By.cssSelector("#new_row_PhoneType")).sendKeys(Keys.ARROW_DOWN);
					Thread.sleep(2000);
					driver.findElement(By.cssSelector("#new_row_PhoneType")).sendKeys(Keys.TAB);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				driver.findElement(By.cssSelector("#new_row_PhoneType")).click();
				Select selectBox = new Select(driver.findElement(By.cssSelector("#new_row_PhoneType")));
				selectBox.selectByVisibleText(util.getData("PhoneType", driver, scenario, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void selectAllMissingVerification(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		
		try {
			
			if(driver.findElements(By.xpath("//table[@id='grid0']/tbody/tr")).size()!=0){
				for(int i=0;i<(driver.findElements(By.xpath("//table[@id='grid0']/tbody/tr")).size()-1);i++){
					driver.findElement(By.xpath("//table[@id='grid0']/tbody/tr["+(i+2)+"]/td[9]/input[@type='checkbox']/following-sibling::label")).click();
					driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
public void writeCaseNumber(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		
	Util utilObject = new Util();
	String applicationNum = "";
	String caseNumber = "";
		try {
			applicationNum = utilObject.getDataINI(utilObject.getData("SectionName", driver, scenario, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder), "APPLICATION_NUMBER");
			caseNumber = applicationNum.substring(1,applicationNum.length());
			utilObject.setDataINI("CASE_NUMBER", caseNumber, testCase, scenario);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


public void writeHIX_AccountNumber(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
	
	Util utilObject = new Util();
	String accountNumber = "";
	
		try {
			accountNumber = driver.findElement(By.xpath(".//*[@id='Messagestab1']/div/div/b")).getText();
			accountNumber = accountNumber.split(" :: ")[1];
			utilObject.setDataINI("ACCOUNT_ID", accountNumber, testCase, scenario);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}