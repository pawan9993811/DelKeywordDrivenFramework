package SupportLibraries;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;



import org.openqa.selenium.Alert;

import SupportLibraries.Report.Status;

public class KeywordLibrary_updated{
	
	public long timeout = 0;
		
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: findElementByType
	 * Return Type: WebElement
	 * Description: This method finds the element based on the type of identifier given in the object ID preceding the actual object ID and returns a Web element
	 */
	public WebElement findElementByType(WebDriver driver, String scenario, String testCase, String homePath, int currentIteration, String objectID, String browser, String passScreenshot, String browserFolder){
		Util utilObject = new Util();
		WebElement object = null;
		String identifier,actualObjectID = "";
		identifier = objectID.split(":")[0].trim();
		if(objectID.split(":").length>2){
			for(int z=1;z<(objectID.split(":").length);z++){
				if(z!=1){
					actualObjectID = actualObjectID+":"+objectID.split(":")[z].trim();
				}else if (z==1){
					actualObjectID = actualObjectID+objectID.split(":")[z].trim();
				}
			}
		}else{
			actualObjectID = objectID.split(":")[1].trim();
		}
				
		if(actualObjectID.contains("#getData=")){
			String[] splittedString = actualObjectID.split("#");
			String finalObjectID = "";
			for(int i=0;i<splittedString.length;i++){
				if(splittedString[i].startsWith("getData=")){
					try {
						splittedString[i] = utilObject.getData((splittedString[i]).split("getData=")[1], driver, scenario, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				finalObjectID = finalObjectID+splittedString[i];
			}
			actualObjectID = finalObjectID;
		}
	
		if(identifier.equalsIgnoreCase("id")){
			object = driver.findElement(By.id(actualObjectID));
		}else if(identifier.equalsIgnoreCase("xpath")){
			object = driver.findElement(By.xpath(actualObjectID));
		}else if(identifier.equalsIgnoreCase("className")){
			object = driver.findElement(By.className(actualObjectID));
		}else if(identifier.equalsIgnoreCase("name")){
			object = driver.findElement(By.name(actualObjectID));
		}else if(identifier.equalsIgnoreCase("css")){
			object = driver.findElement(By.cssSelector(actualObjectID));
		}else if(identifier.equalsIgnoreCase("linkText")){
			object = driver.findElement(By.linkText(actualObjectID));
		}else if(identifier.equalsIgnoreCase("partialLinkText")){
			object = driver.findElement(By.partialLinkText(actualObjectID));
		}
		return object;
		}
		
		


	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: findElementsByType
	 * Return Type: List WebElement
	 * Description: This method finds the elements based on the type of identifier given in the object ID preceding the actual object ID and returns a list of Web element
	 */
	public static List findElementsByType(WebDriver driver, String scenario, String testCase, String homePath, int currentIteration, String objectID, String browser, String passScreenshot, String browserFolder){
		Util utilObject = new Util();
		List<WebElement> object = null;
		String identifier,actualObjectID = "";
		identifier = objectID.split(":")[0].trim();
		if(objectID.split(":").length>2){
			for(int z=1;z<(objectID.split(":").length);z++){
				if(z!=1){
					actualObjectID = actualObjectID+":"+objectID.split(":")[z].trim();
				}else if (z==1){
					actualObjectID = actualObjectID+objectID.split(":")[z].trim();
				}
			}
		}else{
			actualObjectID = objectID.split(":")[1].trim();
		}
		
		if(actualObjectID.contains("#getData=")){
			String[] splittedString = actualObjectID.split("#");
			String finalObjectID = "";
			for(int i=0;i<splittedString.length;i++){
				if(splittedString[i].startsWith("getData=")){
					try {
						splittedString[i] = utilObject.getData((splittedString[i]).split("getData=")[1], driver, scenario, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				finalObjectID = finalObjectID+splittedString[i];
			}
			actualObjectID = finalObjectID;
		}
		
		if(identifier.equalsIgnoreCase("id")){
			object = driver.findElements(By.id(actualObjectID));
		}else if(identifier.equalsIgnoreCase("xpath")){
			object = driver.findElements(By.xpath(actualObjectID));
		}else if(identifier.equalsIgnoreCase("className")){
			object = driver.findElements(By.className(actualObjectID));
		}else if(identifier.equalsIgnoreCase("name")){
			object = driver.findElements(By.name(actualObjectID));
		}else if(identifier.equalsIgnoreCase("css")){
			object = driver.findElements(By.cssSelector(actualObjectID));
		}else if(identifier.equalsIgnoreCase("linkText")){
			object = driver.findElements(By.linkText(actualObjectID));
		}else if(identifier.equalsIgnoreCase("partialLinkText")){
			object = driver.findElements(By.partialLinkText(actualObjectID));
		}
		return object;
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: enter_text
	 * Return Type: Nothing
	 * Description: This method enters the text specified in KeyInData column of Test case
	 */
	public void enter_text(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).clear();
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).sendKeys(dataValue);
		
		reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Gurdeep Arora
	 * Method Name: mouse_over
	 * Return Type: Nothing
	 * Description: Method to perform Mouse Hover action
	 */
	public void mouse_over(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		
		Report reportObject = new Report();
		Actions action1= new Actions(driver);
		action1.moveToElement(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser,                 passScreenshot, browserFolder)).build().perform();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reportObject.Log("Performing Mouse Hover action  "+objectName, "Mouse Hover Action done "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	
	}


	/**
	 * Author: Gurdeep Arora
	 * Method Name: SwitchFocusToFrame
	 * Return Type: None
	 * Description: This method sets focus to a frame in order to click/select objects inside the frame
	 */
	public void SwitchFocusToFrame(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		driver.switchTo().frame(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser,passScreenshot, browserFolder));
		//driver.switchTo().frame(driver.findElement(By.id("loginFrame")));
		reportObject.Log("Selecting frame to access objects in it "+objectName, "Selected Frame "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Gurdeep Arora
	 * Method Name: SwitchFocusToDefault
	 * Return Type: None
	 * Description: This method switches focus from a frame back to the main window
	 */
	public void SwitchFocusToDefault(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		driver.switchTo().defaultContent();
		reportObject.Log("Switching focus from frame to main window "+objectName, "Switched focus to main window "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	
	
	/**
	 * Author: Jitin Prakash Kulshreshta
	 * Method Name: confirm_Message
	 * Return Type: Nothing
	 * Description: This method enters the text specified in KeyInData column of Test case
	 */
	public void confirm_Message(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		reportObject.Log("Confirm Delete Message "+objectName, "Delete Message Confirmed ", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: selectByVisibleText
	 * Return Type: Nothing
	 * Description: This method selects the visible text specified in KeyInData column of Test case in a select box
	 */
	public void selectByVisibleText(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		Select selectBox = new Select(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder));
		selectBox.selectByVisibleText(dataValue);
		reportObject.Log("Selecting by visible text in the dropdown "+objectName, "Selected the value "+dataValue+" in the select box ", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: selectByIndex
	 * Return Type: Nothing
	 * Description: This method selects the visible text specified in KeyInData column of Test case in a select box
	 */
	public void selectByIndex(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		Select selectBox = new Select(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder));
		selectBox.selectByIndex((int)Float.parseFloat(dataValue));
		reportObject.Log("Selecting by index in the dropdown "+objectName, "Selected the index "+(int)Float.parseFloat(dataValue)+" in the select box", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: selectByValue
	 * Return Type: Nothing
	 * Description: This method selects the visible text specified in KeyInData column of Test case in a select box
	 */
	public  void selectByValue(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		Select selectBox = new Select(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder));
		selectBox.selectByValue(dataValue);
		reportObject.Log("Selecting by value in the dropdown "+objectName, "Selected the value "+dataValue+" in the select box", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: selectByoption
	 * Return Type: Nothing
	 * Description: This method selects the visible text specified in KeyInData column of Test case in a select box
	 */
	public  void selectByoption(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		Select selectBox = new Select(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder));
		//selectBox.selectByValue(dataValue);
		
		
		List<WebElement> options = selectBox.getOptions();
		for (WebElement option : options) {
		    if (option.getText().toString().equalsIgnoreCase(dataValue)) {
		    	System.out.println(option.getText().toString());
		        option.click();
		    }
		}
		reportObject.Log("Selecting by value in the dropdown "+objectName, "Selected the value "+dataValue+" in the select box", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: click
	 * Return Type: Nothing
	 * Description: This method clicks on an object specified in the Object Repository and referred in the Object ID column
	 */
	public  void click(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		if(objectName.equalsIgnoreCase("PHONE_TYPE_CELL")){
			System.out.println("test");
		}
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).click();
		Report reportObject = new Report();
		reportObject.Log("Clicking on the Element "+objectName, "Clicked on the object", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Jitin Prakash Kulshreshta
	 * Method Name: sendkey
	 * Return Type: Nothing
	 * Description: This method clicks on an object specified in the Object Repository and referred in the Object ID column
	 */
	public void sendkey(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).sendKeys("");
		Report reportObject = new Report();
		reportObject.Log("Clicking on the Element "+objectName, "Clicked on the object", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: doubleClick
	 * Return Type: Nothing
	 * Description: This method double clicks on an object specified in the Object Repository and referred in the Object ID column
	 */
	public void doubleClick(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Actions ac1 = new Actions(driver);
		ac1.doubleClick(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder)).perform();
		Report reportObject = new Report();
		reportObject.Log("Double Clicking on the Element "+objectName, "Double clicked on the object", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: clickByLinkText
	 * Return Type: Nothing
	 * Description: This method clicks on an object using the text of the link in the entire page
	 */
	public void clickByLinkText(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		driver.findElement(By.linkText(dataValue)).click();
		reportObject.Log("Clicking on the link with text "+dataValue, "Clicked on a link having text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: waitForPageToLoad
	 * Return Type: Nothing
	 * Description: This method waits for the page to load by waiting for an implicit wait period
	 */
	public void waitForPageToLoad(String homePath, String testCase, String scenario, String browser,String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		timeout = 60000;
		driver.manage().timeouts().implicitlyWait(timeout,TimeUnit.SECONDS);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: waitForXPath
	 * Return Type: Nothing
	 * Description: This method waits for the object to appear in the application by waiting for a certain period of time
	 */
	public void waitForXPath(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		try {
			Thread.sleep(1000);
			int count=1;
			while(findElementsByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).size()==0){
				count++;
				Thread.sleep(2000);
				if(count==5)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: waitForLinkText
	 * Return Type: Nothing
	 * Description: This method waits for the link with the given text to appear
	 */
	public void waitForLinkText(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		try {
			int count=1;
			while(driver.findElements(By.linkText(dataValue)).size()==0){
				count++;
				Thread.sleep(2000);
				if(count==2)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: delay
	 * Return Type: Nothings
	 * Description: This method inserts a sleep time of timeout value specified in the WebDriverHelper class
	 */
	public void delay(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		try {
			Thread.sleep(Long.parseLong(Integer.toString(((int)Float.parseFloat(dataValue)))));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: WriteDataToOutputFile
	 * Return Type: Nothing
	 * Description: This method writes data to Output INI file
	 */	
	public void WriteDataToOutputFile(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		String columnValue = dataValue;
		String data = columnValue.split(";")[0].trim();
		String key = columnValue.split(";")[1].trim();
		Util utilObject = new Util();
		
		if(data.startsWith("getData=")){
			try {
				data = utilObject.getData(data.split("getData=")[1].trim(), driver, scenario, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			utilObject.setDataINI(key, data, testCase, scenario);
		}else if(data.startsWith("ObjectText")){
			try {
				data = findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getText().toString();
			System.out.println("The app no" +data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			utilObject.setDataINI(key, data, testCase, scenario);
		} else{
			utilObject.setDataINI(key, data, testCase, scenario);
		}		
	}
	
	/**
	 * Author: Deepika Agnihotri
	 * Method Name: highlight
	 * Return Type: Nothing
	 * Description: This method highlight an object specified in the Object Repository and referred in the Object ID column
	 */
	public void highlight(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) throws InterruptedException{
		
	//Creating JavaScriptExecuter Interface
      JavascriptExecutor js = (JavascriptExecutor)driver;
      
      // Starting loop to highlight and then de-highlight the web element
	   for (int iCnt = 0; iCnt < 2; iCnt++) {
		   //Execute Javascript
		   if(iCnt == 0){
			   js.executeScript("arguments[0].setAttribute('style', arguments[1]);", findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder), "color: yellow; border: 10px solid yellow;");
			   Report reportObject = new Report();
			   reportObject.Log("Highilighting the Element "+objectName, "Highlighted successfully", Status.highlight, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			   Thread.sleep(3000);
		   }else{
			   js.executeScript("arguments[0].setAttribute('style', arguments[1]);", findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder), "");
		   }            
       }
	}
	
	/**
	 * Author: Deepika Agnihotri
	 * Method Name: captureScreenshot
	 * Return Type: Nothing
	 * Description: This method captures screenshot of the page
	 */
	public void captureScreenshot(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) throws InterruptedException{
	   Report reportObject = new Report();
	   reportObject.Log("Capturing screenshot", "Captured screenshot", Status.done, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	//------------------------Verifying keywords
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: verifyElementPresent
	 * Return Type: Nothing
	 * Description: This method verifies for the existence of an object
	 */
	public void verifyElementPresent(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementsByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).size()!=0){
			if(!onPassLog.equalsIgnoreCase("")){
				reportObject.Log("Verifying the Presence of Element "+objectName, onPassLog, Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				reportObject.Log("Verifying the Presence of Element "+objectName, "The Element "+objectName+" is present", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
		}else{
			if(!onFailLog.equalsIgnoreCase("")){
				reportObject.Log("Verifying the Presence of Element "+objectName, onFailLog, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				reportObject.Log("Verifying the Presence of Element "+objectName, "The Element "+objectName+" is  not present", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: verifyElementNotPresent
	 * Return Type: Nothing
	 * Description: This method verifies for the non-existence of an object
	 */
	public void verifyElementNotPresent(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementsByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).size()==0){
			if(!onPassLog.equalsIgnoreCase("")){
				reportObject.Log("Verifying that "+objectName+" Element is not present", onPassLog, Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				reportObject.Log("Verifying that "+objectName+" Element is not present", "The Element "+objectName+" is not present", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
			
		}else{
			if(!onFailLog.equalsIgnoreCase("")){
				reportObject.Log("Verifying that "+objectName+" Element is not present", onFailLog, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				reportObject.Log("Verifying that "+objectName+" Element is not present", "The Element "+objectName+" is  present", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: verifyElementText
	 * Return Type: Nothing
	 * Description: This method verifies for the text of the element
	 */
	public void verifyElementText(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getText().equals(dataValue)){
			reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" has the text "+dataValue, Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else{
			reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" does not have the text "+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: verifyElementDisabled
	 * Return Type: Nothing
	 * Description: This method verifies for the editable state of the object
	 */
	public void verifyElementDisabled(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("disabled").equals("true")){
			reportObject.Log("Verifying the State of the object "+objectName, "The element "+objectName+" is in disabled state", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else{
			reportObject.Log("Verifying the State of the object "+objectName, "The element "+objectName+" is not in disabled state", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}		
	}
	
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: verifyCheckBoxChecked
	 * Return Type: Nothing
	 * Description: This method verifies whether a check box is checked
	 */
	public void verifyCheckBoxChecked(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).isSelected()){
			reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is checked", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else{
			reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is not checked", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
	}
	
	
	/**
	 * Method Name: CurrentMonth
	 * Return Type: Nothing
	 * Description: This method is to fetch the current month
	 */
	public void CurrentMonth(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder)
	{
		Calendar rightNow = Calendar.getInstance();
		java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat("MM");
		dataValue=(df1.format(rightNow.getTime()));
		
		Report reportObject = new Report();
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).clear();
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).sendKeys(dataValue);
		
		reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		
	}

	/**
	 * Method Name: CurrentYear
	 * Return Type: Nothing
	 * Description: This method is to fetch the current Year
	 */
	public void CurrentYear(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder)
	{
		Report reportObject = new Report();
		Calendar rightNow = Calendar.getInstance();
		java.text.SimpleDateFormat df2 = new java.text.SimpleDateFormat("YYYY");
		dataValue=df2.format(rightNow.getTime());
		
			findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).clear();
			findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).sendKeys(dataValue);
			
		
			reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	/**
	 * Method Name: custYear
	 * Return Type: Nothing
	 * Description: This method is to fetch the current Year
	 */
	public void custYear(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder)
	{
		Report reportObject = new Report();
		Calendar prev3Year = Calendar.getInstance();
		  prev3Year.add(Calendar.YEAR, -3);
		  dataValue=String.valueOf(prev3Year.get(Calendar.YEAR));
		  System.out.println(dataValue);
		
			findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).clear();
			findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).sendKeys(dataValue);
			
		
			reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	
public void genRandom(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
	Random rand=new Random();
	Report reportObject = new Report();
	int randomNum = rand.nextInt((9999 - 1000) + 1) + 1000;
	dataValue=String.valueOf(randomNum);
	System.out.println(dataValue);
	
	findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).clear();
	findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).sendKeys(dataValue);
	

	reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	
}
/**

 * Method Name: checkbox_value
 * Return Type: Nothing
 * Description: This method checks the checkbox value
 */
public void checkbox_value(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
	Report reportObject = new Report();
	
	try{
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).click();
		reportObject.Log("selecting checkbox "+objectName, "The checkbox "+objectName+" is checked", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}

catch(Exception e){

	reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is not checked", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
}
	}
/**
 
 * Method Name: getTitle
 * Return Type: Nothing
 * Description: This method registers the title of the page and is a pass if it matches the expected
 */
public void getTitle(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
	Report reportObject = new Report();
	 
	 System.out.println("The page title: "+driver.getTitle());
	 try{
	if((driver.getPageSource().contains(objectName)) || (driver.getTitle().contains(dataValue)))
	  {
		reportObject.Log("Page Opened"+objectName, "The page "+objectName+" has successfully opened", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	  }
	else
		 reportObject.Log(""+objectName, "The page "+objectName+" has not opened", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	  
	 }
	 catch(Exception e){
		 reportObject.Log(" "+objectName, ""+objectName+" exxception block", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	 }
}

/**

 * Method Name: ReadDataFromOutputFile
 * Return Type: Nothing
 * Description: This method reads the value from the output file and puts it in the data field
 */
public void ReadDataFromOutputFile(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){

	Util utilObject = new Util();
	Report reportObject = new Report();
	String columnValue = dataValue;
	String sectionName = columnValue.split(";")[0].trim();
	String key = columnValue.split(";")[1].trim();
	
	dataValue=utilObject.getDataINI(sectionName, key);
		
	
	findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).clear();
	findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).sendKeys(dataValue);
	
	reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);		
	

}


/**

 * Method Name: pickFromDatePicker
 * Return Type: Nothing
 * Description: This method reads the value from the output file and puts it in the data field
 */
public void pickFromDatePicker(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){

	Report reportObject = new Report();
/*	driver.get("http://jqueryui.com/datepicker/");
	driver.switchTo().frame(0);
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	//Click on image so that datepicker will come
	 * 
*/	
	findElementByType(driver, scenario, testCase, homePath, currentIteration, objectId, browser, passScreenshot, browserFolder).click();
	//driver.findElement(By.xpath(".//*[@id='dimg_applicationReceivedDate']")).click();
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	//Click on next so that we will be in next month
	//findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).click();

	/*DatePicker is a table.So navigate to each cell 
	* If a particular cell matches value 13 then select it
	*/
	WebElement dateWidget = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']"));
	//List rows=dateWidget.findElements(By.tagName("tr"));
	
	List<WebElement> columns=dateWidget.findElements(By.tagName("td"));

	for (WebElement cell:columns){
	//Select 13th Date 
	if (cell.getText().equals("1")){
	cell.findElement(By.linkText("1")).click();
	break;
	}
	} 




	
	reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);		
	

}

/**

 * Method Name: pickFromDatePicker
 * Return Type: Nothing
 * Description: This method reads the value from the output file and puts it in the data field
 */
public void pickLastDateFromDatePicker(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){

	Report reportObject = new Report();
/*	driver.get("http://jqueryui.com/datepicker/");
	driver.switchTo().frame(0);
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	//Click on image so that datepicker will come
	 * 
*/	
	findElementByType(driver, scenario, testCase, homePath, currentIteration, objectId, browser, passScreenshot, browserFolder).click();
	//driver.findElement(By.xpath(".//*[@id='dimg_applicationReceivedDate']")).click();
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	//Click on next so that we will be in next month
	//findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).click();

	/*DatePicker is a table.So navigate to each cell 
	* If a particular cell matches value 13 then select it
	*/
	WebElement dateWidget = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']"));
	//List rows=dateWidget.findElements(By.tagName("tr"));
	
	List<WebElement> columns=dateWidget.findElements(By.tagName("td"));

	for (WebElement cell:columns){
	//Select 13th Date 
	if (cell.getText().equals("30")){
	cell.findElement(By.linkText("30")).click();
	break;
	}
	} 




	
	reportObject.Log("Entering text in the text box "+objectName, "Entered the text "+dataValue, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);		
	

}

//added methods
public void verifySelectedValue(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
	   Report report = new Report();
		
	   Select selectBox = new Select(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder));
      if(selectBox.getFirstSelectedOption().getText().equalsIgnoreCase(dataValue)){
            if(!onPassLog.equalsIgnoreCase("")){
                  report.Log("Verifying the selected value in the dropdown "+objectName, onPassLog, Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
            }else{
                  report.Log("Verifying the selected value in the dropdown "+objectName, "The dropdown "+objectName+" has the value "+dataValue+" selected", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
            }                 
      }else{
            if(!onFailLog.equalsIgnoreCase("")){
                  report.Log("Verifying the selected value in the dropdown "+objectName, onFailLog, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
            }else{
                  report.Log("Verifying the selected value in the dropdown "+objectName, "The dropdown "+objectName+" does not have the value "+dataValue+" selected", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
            }
            
      }
}

}