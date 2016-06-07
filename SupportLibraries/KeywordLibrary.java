package SupportLibraries;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;
import java.util.regex.Pattern;




import org.openqa.selenium.Alert;

import SupportLibraries.Report.Status;

public class KeywordLibrary{

	private static final Logger logger = Logger.getLogger(KeywordLibrary.class.getName());
	private static final String SKIP_STEP = "n/a";
	public static long timeout = 30000;
	
			
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
	
		try{
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
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
	}catch (NoSuchElementException e){
		driver.navigate().to(utilObject.getValueFromConfigProperties("URL"));
		e.printStackTrace();
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
					try{
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
					}catch (NoSuchElementException e){
						driver.navigate().to(utilObject.getValueFromConfigProperties("URL"));
						e.printStackTrace();
		}
		return object;
	}
	
	private static WebElement getWebElement(String identifier, String elementId, WebDriver driver){
		By bySelector = getSelector(identifier,elementId);
		return driver.findElement(bySelector);
	}
	
	private static List<WebElement> getWebElementList(String identifier, String elementId, WebDriver driver){
		By bySelector = getSelector(identifier,elementId);
		return driver.findElements(bySelector);
	}	
	
	private static By getSelector(String identifier, String elementId){
		By bySelector = null;
		if(identifier.equalsIgnoreCase("id")){
			bySelector = By.id(elementId);
		}else if(identifier.equalsIgnoreCase("xpath")){
			bySelector = By.xpath(elementId);
		}else if(identifier.equalsIgnoreCase("className")){
			bySelector = By.className(elementId);
		}else if(identifier.equalsIgnoreCase("name")){
			bySelector = By.name(elementId);
		}else if(identifier.equalsIgnoreCase("css")){
			bySelector = By.cssSelector(elementId);
		}else if(identifier.equalsIgnoreCase("linkText")){
			bySelector = By.linkText(elementId);
		}else if(identifier.equalsIgnoreCase("partialLinkText")){
			bySelector = By.partialLinkText(elementId);
		}
		return bySelector;
	}
	
	/**
	 * Method Name: jsEnterText
	 * Return Type: Nothing
	 * Description: Same as enter_text() keyword except that this keyword uses javascript to exter text. 
	 * The object id must be specified using ID selector
	 */
	public void jsEnterText(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		String webElementObjectId = objectId.split(":")[1];
		String script = getEnterTextJavaScript(webElementObjectId, dataValue);
		executeJavaScript(script, driver);
		reportObject.Log("Entering text(JS) in the text box "+objectName, "Entered the text "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	private String getEnterTextJavaScript(String webElementObjectId, String dataValue){
		StringBuilder sb = new StringBuilder();
		sb.append("var textElement = document.getElementById('");
		sb.append(webElementObjectId);
		sb.append("');");	
		sb.append(" textElement.setAttribute('value','");
		sb.append(dataValue);
		sb.append("');");	
		return sb.toString();
	}	
	
	/**
	 * Method Name: jsSelectByVisibleText
	 * Return Type: Nothing
	 * Description: Same as selectByVisibleText keyword except that this method uses Selenium WebDriver JavaScript API to select the visible text.
	 * The selector must be ID selector.
	 */	
	public void jsSelectByVisibleText(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		int index = getSelectOptionIndex(homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
		String webElementObjectId = objectId.split(":")[1];
		String script = getSelectOptionIndexJavaScript(webElementObjectId, index);
		logger.info("Script-->"+script);
		executeJavaScript(script, driver);
		reportObject.Log("Selecting(JS) by visible text in the dropdown "+objectName, "Selected the value "+dataValue+" in the select box ", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	private int getSelectOptionIndex(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		List<WebElement> webElements = findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser,passScreenshot, browserFolder).findElements(By.tagName("option"));
		int index = 0;
		for(WebElement webElm : webElements){
			if(webElm.getAttribute("text").equals(dataValue)){
				return index;
			}
			index++;			
		}
		return -1;		
	}
	
	private static String getSelectOptionIndexJavaScript(String webElementObjectId, int index){
		StringBuilder sb = new StringBuilder();
		sb.append("var selDropDown = document.getElementById('");
		sb.append(webElementObjectId);
		sb.append("');");	
		sb.append(" selDropDown.selectedIndex = ");
		sb.append(index);
		sb.append(";");	
		return sb.toString();
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
		action1.moveToElement(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder)).build().perform();
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
	 * Method Name: selectMultipleByVisibleText
	 * Return Type: Nothing
	 * Description: This method selects the multiple text's specified in KeyInData column of Test case in a select box
	 */
	public void selectMultipleByVisibleText(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		String[]  dataValuesArr = dataValue.split(";");
		if(dataValuesArr != null && dataValuesArr.length > 0){
			Select selectBox = new Select(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder));		
			for(int i=0; i<dataValuesArr.length; i++){
				selectBox.selectByVisibleText(dataValuesArr[i].trim());
			}
		}
		reportObject.Log("Selecting by visible text in the select box "+objectName, "Selected the value "+dataValue+" in the select box ", Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
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
		findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).click();
		Report reportObject = new Report();
		reportObject.Log("Clicking on the Element "+objectName, "Clicked on the object", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/**
	 * Method Name: jsClick
	 * Return Type: Nothing
	 * Description: Same as click keyword except that this method uses Selenium WebDriver JavaScript API to click the object.
	 * The selector must be ID selector.
	 */
	public void jsClick(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		String webElementObjectId = objectId.split(":")[1];
		StringBuilder sb = new StringBuilder();
		sb.append("document.getElementById('");
		sb.append(webElementObjectId);
		sb.append("').click();");
		
		executeJavaScript(sb.toString(),driver);
		
		reportObject.Log("Clicking(JS) on the Element "+objectName, "Clicked on the object", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	/*
	 * Convenient method to execute Java Script
	 */
	private static void executeJavaScript(String jsStatement, WebDriver driver){
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript(jsStatement);
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
	 * Method Name: verifyElementByValue
	 * Return Type: Nothing
	 * Description: This method verifies the value of a HTML ELement
	 */
	public void verifyElementByValue(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("value").equals(dataValue)){
			reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" has the text "+dataValue, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("value").startsWith("0")){
			if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("value").substring(1).equals(dataValue))
				reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" has the text "+dataValue, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			else
				reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" does not have the text "+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
		else{
			reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" does not have the text "+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}		
	}
	
	
	/**
	 * Method Name: verifyMandatoryElementHighlight
	 * Return Type: Nothing
	 * Description: This method verifies if the mandatory field is highlighted
	 */
	public void verifyMandatoryElementHighlight(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("style").equals("background-color: rgb(255, 223, 223); border-width: 1px; border-color: rgb(255, 0, 0); border-style: solid;") ){
			reportObject.Log("Verify the highlight of the object"+objectName, "The field "+objectName+" is highlighted for mandatory validation"+dataValue, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
		else{
			reportObject.Log("Highlight of the object failed "+objectName, "The element "+objectName+" does not have the highlight"+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
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
	 * Method Name: verifyDropDownValues
	 * Return Type: Nothing
	 * Description: This method verifies the options present in the drop down
	 */
	public void verifyDropDownValues(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		boolean flag = true;
		Report reportObject = new Report();
		ArrayList<String> dataValues = new ArrayList<String>();
		String[] array_dataValues = null;
		String data = dataValue;
		if(data.contains(";")){
			array_dataValues = data.split(";");
			for(int i=0;i<array_dataValues.length;i++){
				dataValues.add(array_dataValues[i].toString());
			}
		}else{
			dataValues.add(data);
		}
		
		//Get the options of the drop down list.
	    List<WebElement> listOfOptions = findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).findElements(By.tagName("option"));
		
	    if(!(listOfOptions.size()==dataValues.size())){
	    	flag = false;
	    }else{
	    	for(int j=0;j<listOfOptions.size();j++){
				if(!(listOfOptions.get(j).getText().trim().equals(dataValues.get(j)))){
					flag = false;
					break;
				}
			}
	    }
	    
		if(flag){
			reportObject.Log("Verifying the values in the dropdown "+objectName, "The expected values are present in the dropdown", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else{
			reportObject.Log("Verifying the values in the dropdown "+objectName, "The expected values are not present in the dropdown", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
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
	
	/////////////////////////////////////
	
	public void closePopupWindow(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){		
		closeAllOtherWindows(homePath, testCase, scenario,browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
		driver.switchTo().window(WebDriverHelper.mainWindowHandle);
	}
	
	private static void closeAllOtherWindows(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Set<String> allWindowHandles = driver.getWindowHandles();
		for (String currentWindowHandle : allWindowHandles) {
			if (!currentWindowHandle.equals(WebDriverHelper.mainWindowHandle)) {
				try{
					driver.switchTo().window(currentWindowHandle);
					driver.close();
				}catch(Exception e){
					logger.warning("Failed to close window with windowhandle:"+currentWindowHandle);
					logger.warning(e.getMessage());
				}
			}
		}		
	}
	
	/**
	 * Method Name: verifyElementTitle
	 * Return Type: Nothing
	 * Description: This method verifies for the title attribute of the element
	 */
	public void verifyElementTitle(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("title").equals(dataValue)){
			reportObject.Log("Verifying the title of the object "+objectName, "The element "+objectName+" has the title "+dataValue, Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else{
			reportObject.Log("Verifying the title of the object "+objectName, "The element "+objectName+" does not have the title "+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
		
	}
	
	/**
	 * Method Name: verifyRequiredField
	 * Return Type: Nothing
	 * Description: This method verifies for the required fields src attribute
	 */
	public void verifyRequiredField(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
					
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("src").endsWith(dataValue)){
			reportObject.Log("Verify required field: "+objectName, "The element "+objectName+" is required ", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else{
			reportObject.Log("Verify required field: "+objectName, "The element "+objectName+" is not a required field "+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
		}	
	/**
	 * Method Name: verifyElementPartialText
	 * Return Type: Nothing
	 * Description: This method verifies for the partial text of the element
	 */
	public void verifyElementPartialText(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getText().contains(dataValue)){
			reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" has the text "+dataValue, Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}else{
			reportObject.Log("Verifying the Text of the object "+objectName, "The element "+objectName+" does not have the text "+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}		
	}
	
	/**
	 * Method Name: waitForWebElement
	 * Return Type: Nothing
	 * Description: This method will wait implicitly for this web element for the specified time in seconds.
	 * This will be useful in cases of ajax calls to wait for the element to load without submit
	 */
	public void waitForWebElement(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		WebDriverWait wdWait = new WebDriverWait(driver, Long.parseLong(dataValue));
		wdWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objectId)));
	}
	
	/**
	 * Method Name: handleMultipleElements
	 * Return Type: Nothing
	 * Description: This method handles multiple elements having same name i.e. array of web elements
	 * like checkbox, radio buttons etc. having same id
	 */	
	public void handleMultipleElements(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		List webElements = findElementsByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder);
		String[] typeAndData = getTypeAndData(dataValue);
		String[] value = getMultipleElementsData(typeAndData[1],dataValue);
		if(typeAndData[0].equalsIgnoreCase("checkbox")){
			selectMultipleCheckbox(webElements,value, homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
		}
	}
	
	private void selectMultipleCheckbox(List webElements, String[] value,String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		for(int index =0; index < value.length; index++){
			int elementIndex = Integer.parseInt(value[index]);
			RemoteWebElement element = ((RemoteWebElement)webElements.get(elementIndex-1));
			element.click();
		}
		reportObject.Log("Selected multiple check box with index ",value.toString(), Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
	
	private String[] getTypeAndData(String dataValue){
		return dataValue.split(":");
	}
	
	private String[] getMultipleElementsData(String data, String dataValue){
		if(data.contains("to")){
			String[] value = data.split(" to ");
			return getArrayOfIntegersAsString(Integer.parseInt(value[1]));			
		}else if(data.contains(",")){
			return data.split(",");
		}else{
			return new String[]{dataValue};
		}
	}
	
	private String[] getArrayOfIntegersAsString(int maxNum){
		String[] value = new String[maxNum];
		for(int count = 0; count < maxNum; count++){
			value[count] = String.valueOf(count + 1);
		}
		return value;
	}
	
	/**
	 * Method Name: storeSessionData
	 * Return Type: Nothing
	 * Description: Stores the session data for the given KeyInData value
	 */
	public void storeSessionData(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		WebElement webElement = findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder);
		String elementValue = webElement.getText();
		DriverSession.getInstance().add(dataValue, elementValue);
	}
	
	/**
	 * Method Name: enterSSN1
	 * Return Type: Nothing
	 * Description: Auto generates SSN based on user settings in the TestData tab.
	 * If the corresponding value in TestData tab is "auto" then it will auto generate
	 * the value otherwise it will use the value as is in TestData tab 
	 */
	public void enterSSN1(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){		
		setIfAutoSSN(3, dataValue);
		enter_text(homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
	}
	
	/**
	 * Method Name: enterSSN2
	 * Return Type: Nothing
	 * Description: Auto generates SSN based on user settings in the TestData tab.
	 * If the corresponding value in TestData tab is "auto" then it will auto generate
	 * the value otherwise it will use the value as is in TestData tab 
	 */
	public void enterSSN2(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		setIfAutoSSN(2, dataValue);
		enter_text(homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
	}
	
	/**
	 * Method Name: enterSSN3
	 * Return Type: Nothing
	 * Description: Auto generates SSN based on user settings in the TestData tab.
	 * If the corresponding value in TestData tab is "auto" then it will auto generate
	 * the value otherwise it will use the value as is in TestData tab 
	 */
	public void enterSSN3(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		setIfAutoSSN(4, dataValue);
		enter_text(homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
	}	
	
	private static int getSSN(int length){
		if(length == 3){
			return getRandomNum(100, 999);
		}else if(length == 2){
			return getRandomNum(10, 99);
		}else if(length == 4){
			return getRandomNum(1000, 9999);
		}
		return 0;
	}
	
	private void setIfAutoSSN(int length, String dataValue){
		if(dataValue.equalsIgnoreCase("auto")){
			dataValue = String.valueOf(getSSN(length));
		}		
	}
	
	  private static  int getRandomNum(int aStart, int aEnd){
		    if (aStart > aEnd) {
		      throw new IllegalArgumentException("Start cannot exceed End value for SSN range.");
		    }
		    Random aRandom = new Random();
		    //get the range, casting to long to avoid overflow problems
		    long range = (long)aEnd - (long)aStart + 1;
		    // compute a fraction of the range, 0 <= frac < range
		    long fraction = (long)(range * aRandom.nextDouble());
		    int randomNumber =  (int)(fraction + aStart);    
		    
		    return randomNumber;

		}
	  
	/**
	 * Method Name: buildWorksheet
	 * Return Type: N/A
	 * Description: This method can be used to insert or update the data in a dyna table for Income screens. 
	 * It also validates the dates populated in the 1st column of each row in the dynatable. 
	 * We can ignore the date validation by mentioning 'n/a' in the datavalue in the Test Data. 
	 * The input Data in excel sheet should be comma separated for a row and 2 rows data should be separated by a semicolon.
	 * e.g.
	 * 10/21/2015,2,Actual,n/a,21,Hard Copy;n/a,Anticipated,Unusually Low,n/a,Hard Copy
	 * where 
	 * 10/21/2015,2,Actual,n/a,21,Hard Copy: 1st row data and
	 * n/a,n/a,Anticipated,Unusually Low,n/a,Hard Copy : 2nd row data
	 * and Row data are separated by a ";"
	 * Also, it can be used for editing the nth row of the dyna table.
	 * e.g. n/a,2,Actual,n/a,21,Hard Copy;n/a,Actual,Unusually Low,n/a,Hard Copy
	 * This method will replace the existing data in the example data with the given data.
	 * Note: When the dyna table don't have a date column as the 1st column then just start the property value in the object repository with a semicolon.
	 * Also, if we want to edit the nth row in a dyna table then we need to provide data at least till the nth row starting from the 1st row with the new details and the older details * as n/a.
	 * e.g. 
	 * e.g. n/a,n/a,n/a,n/a,n/a,n/a;n/a,Actual,Unusually Low,n/a,Hard Copy
	 * This will modify the data in the 2nd row of the dynatable.
	 * 
	 */
	public static void buildWorksheet(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) {		
		Report reportObject = new Report();
		String objectIds = objectId;
		int numberofDynaRows = findDynarows(dataValue);
		String objects[] = objectIds.split(";");
		String pattern = Pattern.quote("?");
		//first one is row
		String rowIdentifier = objects[0].split(":")[0].trim();
		String row = objects[0].split(":")[1].trim();

		String[] rowData = dataValue.split(";");
		
		for(int rowNum = 1; rowNum <= numberofDynaRows; rowNum++){
			String currentRow = row.replaceAll(pattern, String.valueOf(rowNum));
			logger.info("currentRow-->"+currentRow);
			WebElement rowWebElement = getWebElement(rowIdentifier,currentRow, driver);
			rowWebElement.click();
		
			String[] columnData = rowData[rowNum-1].split(",");
			for(int columnNum = 1; columnNum <=columnData.length;columnNum++ ){
				String columnObjectId = objects[columnNum];
				columnObjectId = columnObjectId.split(":")[1];
				String currentColumnObjectId = columnObjectId.replaceAll(pattern, String.valueOf(rowNum));
				logger.info("currentColumnObjectId-->"+currentColumnObjectId);
				if(!columnData[columnNum - 1].equalsIgnoreCase(SKIP_STEP)){
					if(columnNum == 1){
						validateDateFields(currentColumnObjectId, columnData[columnNum - 1], homePath, testCase, scenario, browser, objectId, objectName,  onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
					}else{
						performDynaAction(currentColumnObjectId,columnData[columnNum - 1], homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);	
					}
				}else{
					reportObject.Log("Skipping the data entry for "+currentColumnObjectId,
							" as it is marked to skip.",
							Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);					
				}
			}
		}
	}
	
	private static void performDynaAction(String webElementId, String inputData, String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) {
		Report reportObject = new Report();
		String script = getDynaTableJavaScript(webElementId,inputData);
		
		try{
			executeJavaScript(script, driver);
			reportObject.Log(
					"Set the dyna table data for web element:"+webElementId,
					"with data: " + inputData,
					Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);			
		}catch(Exception e){
			reportObject.Log(
					"Failed to set the dyna table data for web element:"+webElementId+ " with data: " + inputData,
					"Failed with exception message: "+e.getMessage(),
					Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);			
		}		
	}
	
	private static String getDynaTableJavaScript(String webElementId, String elementData){
		StringBuffer sb = new StringBuffer();
		sb.append("var webElement = document.getElementById('"+webElementId+"');");
		sb.append("var elementType = webElement.tagName;");				
		sb.append(" if(elementType === 'INPUT'){");
		sb.append(" webElement.value = '");
		sb.append(elementData);
		sb.append("';");		
		sb.append(" } else if(elementType === 'SELECT'){");
		sb.append(" for(var index=0; index<webElement.length; index++){");
		sb.append("	var optText = webElement.options[index].text;");
		sb.append("	if(optText == '"+elementData+"'){");
		sb.append("		webElement.selectedIndex = index;");
		sb.append("	}");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	

	/**
	 * Method Name: findDynarows
	 * Return Type: Nothing
	 * Description: This method is used to get number of Rows in an pre-existing Dyna Table based on input test data
	 */
	
	 private static int findDynarows(String data){
		 String rows[] = data.split(";");
		 return rows.length;
	 }
	 
	 /**
		 * Method Name: validateExpectedDynaRows
		 * Return Type: Nothing
		 * Description: This method is used to assert the number of expected rows in Summary screens
		 */
	 
	 public static void validateExpectedDynaRows(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) {
		 Report reportObject = new Report();
		 reportObject.Log("Validating the RowCount in a SummaryPage ", "Expected Count :: "+ dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		 String xPath=objectId.split("xpath:")[1].trim();
		 String temp=driver.findElement(By.xpath(xPath)).getText();
		 String expectedRows=dataValue;
		 String totalRows=String.valueOf(temp.charAt(temp.length()-1));
		 if(expectedRows.equalsIgnoreCase(totalRows)){
			 reportObject.Log("Validating the RowCount in a SummaryPage ", "Expected Count :: "+ dataValue + "  Actual Count :: "+totalRows, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		 }else{
			 reportObject.Log("Validating the RowCount in a SummaryPage ", "Expected Count :: "+ dataValue + "  Actual Count :: "+totalRows, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		 }
	 }
	 
	/**
	 * Method Name: verifyCalculatedDate 
	 * Return Type: void 
	 * Description: This method is used to take input of user or system date and compare with the
	 * another field using checkVerifyCalculatedDate method
	 */
	public void verifyCalculatedDate(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		String outputDate = findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).getAttribute("value");
		try {
			if (outputDate
					.equalsIgnoreCase(getCalculatedDate(dataValue))) {
				reportObject.Log("Validating the date ", "Calculated Date :: "
						+ getCalculatedDate(dataValue) + " and Actual Date :: " + outputDate,
						Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			} else {
				reportObject.Log("Validating the date ", "Calculated date by keyword :: "
						+ getCalculatedDate(dataValue) + "  Actual value :: " + outputDate,
						Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportObject.Log("Validating the date ", "Calculated date by keyword :: "
					+ getCalculatedDate(dataValue) + "  Actual value :: " + outputDate,
					Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
	}

	/**
	 * Method Name: getCalculatedDate
	 * Return Type: String 
	 * Description: This method is used to take input of user or system date and compare with the
	 * another field depending on format and return String
	 */
	private static String getCalculatedDate(String userDate) {
		String[] inputArray = userDate.split(",");
		String formattedDate = "";
		GregorianCalendar sysCalendar = new GregorianCalendar();
		sysCalendar = getInputDate(inputArray[0]);
		long differnceDate = getDateCalculated(sysCalendar, inputArray[1]);
		if (inputArray.length > 2) {
			formattedDate = getOutputFormatDate(inputArray[1], inputArray[2],
					differnceDate);
		} else {
			long diffDay = TimeUnit.MILLISECONDS.toDays(differnceDate);
			formattedDate = String.valueOf(diffDay);
		}
		return formattedDate;
	}

	/**
	 * Method Name: getOutputFormatDate Return Type: String Description: This
	 * method is used to take input date typr and return String of date
	 * depending on the user input
	 */
	private static String getOutputFormatDate(String inputArray1,
			String inputArray2, long differnceDate) {
		String[] operationArray = inputArray1.split(";");
		String opDateType = operationArray[0];
		long diffDay = TimeUnit.MILLISECONDS.toDays(differnceDate);
		String returnDate = "";
		if ((!opDateType.equalsIgnoreCase("mm")
				&& !opDateType.equalsIgnoreCase("dd")
				&& !opDateType.equalsIgnoreCase("yy") && !opDateType
					.equalsIgnoreCase("yyyy"))
				|| (inputArray2.equals("") || inputArray2.equals(" ") || inputArray2 == null)) {
			returnDate = String.valueOf(diffDay);
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(inputArray2);
				Date date1 = new Date(differnceDate);
				returnDate = sdf.format(date1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnDate;
	}

	/**
	 * Method Name: getDateCalculated Return Type: long Description: This method
	 * is used to take input date and date type and return the difference
	 */
	private static long getDateCalculated(GregorianCalendar sysCalendar,
			String inputArray) {
		String[] operationArray = inputArray.split(";");
		String opDateType = operationArray[0];
		String opDate = operationArray[1];
		long differnceDate = 0;
		if (opDateType.equalsIgnoreCase("mm")) {
			int operationMonthDate = Integer.parseInt(opDate);
			sysCalendar.add(Calendar.MONTH, operationMonthDate);
			differnceDate = sysCalendar.getTime().getTime();
		} else if (opDateType.equalsIgnoreCase("dd")) {
			int operationDayDate = Integer.parseInt(opDate);
			sysCalendar.add(Calendar.DAY_OF_MONTH, operationDayDate);
			differnceDate = sysCalendar.getTime().getTime();
		} else if (opDateType.equalsIgnoreCase("yy")
				|| opDateType.equalsIgnoreCase("yyyy")) {
			int operationYearDate = Integer.parseInt(opDate);
			sysCalendar.add(Calendar.YEAR, operationYearDate);
			differnceDate = sysCalendar.getTime().getTime();
		} else {
			Date operationDate = null;
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						opDateType);
				operationDate = simpleDateFormat.parse(opDate);

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sysCalendar.getTime().getTime() > operationDate.getTime()) {
				differnceDate = sysCalendar.getTime().getTime()
						- operationDate.getTime();
			} else {
				differnceDate = operationDate.getTime()
						- sysCalendar.getTime().getTime();
			}
		}
		return differnceDate;
	}

	/**
	 * Method Name: getInputDate Return Type: GregorianCalendar Description:
	 * This method is used to take input date type and return the date in
	 * GregorianCalendar format
	 */
	private static GregorianCalendar getInputDate(String inputArray) {
		String[] inputDateArray = inputArray.split(":");
		Calendar currentDate = Calendar.getInstance();
		GregorianCalendar sysCalendar = new GregorianCalendar();
		if (inputArray.contains("sys")) {
			int currentMonth = currentDate.get(Calendar.MONTH);
			int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
			int currentYear = currentDate.get(Calendar.YEAR);
			sysCalendar = new GregorianCalendar(currentYear, currentMonth,
					currentDay);
			if (inputDateArray.length > 1) {
				String[] ipDateArr = inputDateArray[1].split(";");
				String ipDateType = ipDateArr[0];
				int ipDate = Integer.parseInt(ipDateArr[1]);
				if (ipDateType.equalsIgnoreCase("mm")) {
					sysCalendar.add(Calendar.MONTH, ipDate);
				} else if (ipDateType.equalsIgnoreCase("dd")) {
					sysCalendar.add(Calendar.DAY_OF_MONTH, ipDate);
				} else if (ipDateType.equalsIgnoreCase("yyyy")) {
					sysCalendar.add(Calendar.YEAR, ipDate);
				}
			} else {
				sysCalendar = new GregorianCalendar(
						currentDate.get(Calendar.YEAR),
						currentDate.get(Calendar.MONTH),
						currentDate.get(Calendar.DAY_OF_MONTH));
			}
		} else {
			String[] userDefDateArr = inputArray.split(";");
			String userDateDefType = userDefDateArr[0];
			String userDateDef = userDefDateArr[1];
			SimpleDateFormat sdf = new SimpleDateFormat(userDateDefType);
			try {
				Date d = sdf.parse(userDateDef);
				sysCalendar.setTime(d);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sysCalendar;
	}
	 
	 /**
      * Method Name: enterSystemDate
      * Return Type: Nothing
      * Description: Auto generates Day, Month, Year based on user inputs in the TestData tab.
      * If the corresponding value in TestData tab is "mm;0" or "dd;0" or "yy;0" then it will auto generate
      * the date value based on the Day or Month or Year format and it will be given as  
       * For Past Dates   : [mm;-1/dd;-1/yy;-1] 
       * For Future Dates : [mm;1/dd;1/yy;1]
      * where -1/+1 implies the difference with from the current time
      * else the data in the TestData tab will be used 
       */
	 
      public void enterSystemDate(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
          validateAndSetSystemDate(homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
          enter_text(homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error, browserFolder);
      }
      
     
      /**
        * Method Name: validateAndSetSystemDate
        * Return Type: Nothing
        *Description: This method is used to validate and set System Date
        */
      
      private static void validateAndSetSystemDate(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){           
          Report reportObject = new Report();
    	  try {
			if(dataValue.contains(",")) {
				String[] dataValuesArr = dataValue.split(",");
				String returnFormat = getDateFormat(dataValuesArr[1]);
				if (dataValuesArr[0].contains(";")) {
					String[] dateValuesArr = dataValuesArr[0].split(";");
					if (dateValuesArr != null && dateValuesArr.length == 2) {
						String dateFormat = dateValuesArr[0];
						int dateValue = Integer.parseInt(dateValuesArr[1]);
						if (dateFormat.trim().equalsIgnoreCase("dd")) {
							setDateFormat(returnFormat, getDateValue(dateValue, Calendar.DATE), dataValue);
						}
						if (dateFormat.trim().equalsIgnoreCase("mm")) {
							setDateFormat(returnFormat, getDateValue(dateValue, Calendar.MONTH), dataValue);
						}
						if (dateFormat.trim().equalsIgnoreCase("yy")) {
							setDateFormat(returnFormat, getDateValue(dateValue, Calendar.YEAR), dataValue);
						}
					}
				}
			}else if(dataValue.contains(";")){
                String[]  dataValuesArr = dataValue.split(";");
                if(dataValuesArr != null && dataValuesArr.length == 2 ){
                     String dateFormat = dataValuesArr[0];
                     String returnFormat = getDateFormat(dataValuesArr[0]);
                     int dateValue = Integer.parseInt(dataValuesArr[1]);                           
                     if(dateFormat.trim().equalsIgnoreCase("dd")){
                    	 setDateFormat(returnFormat,getDateValue(dateValue, Calendar.DATE), dataValue);
                     }
                     if(dateFormat.trim().equalsIgnoreCase("mm")){
                    	 setDateFormat(returnFormat, getDateValue(dateValue, Calendar.MONTH), dataValue);   
                     }
                     if(dateFormat.trim().equalsIgnoreCase("yy")){                              
                    	 setDateFormat(returnFormat, getDateValue(dateValue, Calendar.YEAR), dataValue);
                     }
                }
           }
          } catch (Exception e) {
        	  reportObject.Log("Generate System Date "+objectName, "Invalid Date Format for the "+objectName+" and Entered Value is "+dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
               e.printStackTrace();
          }             
      }
      
	   /**
	    * Method Name: getDateFormat
	    * Return Type: String
	    *Description: This method is used to getDateFormat based on the inputs
	    */
		private static String getDateFormat(String format) {
			switch (format.toLowerCase()) {
			case "dd":
				return format = "dd"; 
			case "mm":
				return format = "MM";
			case "yy":
				return format = "yyyy";
			}
			return format;
		}
      
      /**
        * Method Name: getDateValue
        * Return Type: Date
        * Description: This method is used to get Date based on the inputs
        */
      
      private static Date getDateValue(int dateValue, int calendarValue){
          Calendar cal = Calendar.getInstance();
          cal.add(calendarValue, dateValue);
          cal.getTime();    
          return cal.getTime();
      }
      
      /**
       * Method Name: setDateFormat
       * Return Type: Nothing
       * Description: This method is used to get Date Format based on the inputs
       */
     
     private static void setDateFormat(String format, Date date, String dataValue){
         SimpleDateFormat sdf = new SimpleDateFormat(format);
         dataValue =  sdf.format(date);
     }
      /**
       * Method Name : clickDynaTableEditButton
       * Return Type : Nothing
       * Description : This method is used to edit Specific row in the dyna table in the Summary Screens.
       * e.g. input in Scenario Sheet
       * 1 where
       * 1 = row to be edited i.e 1st row in this case
       */
      
      public static void clickDynaTableEditButton(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
          Report reportObject = new Report(); 
    	  String xpathString = objectId;
           String newXpathString = xpathString.replaceAll("\\?", dataValue).split("xpath:")[1];
           driver.findElement(By.xpath(newXpathString)).click();
           reportObject.Log("ClickDynaTableEditButton "+dataValue, "Clicked on a DynaTableEditButton "+dataValue, Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
 }
  
      /**
       * Method Name: validateDateFiels
       * Return Type: Nothing
       *Description : This method is used to validate date fields in a dyna table. It is internally called by insertDynaTableData.
       */
      
      private static boolean validateDateFields(String xPath, String dataValue, String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
    	  Report reportObject = new Report();
    	  boolean isValid=false;
    	  reportObject.Log("Dyna Table Date Validation", "Expected Date :: "+dataValue , Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			String actualData=driver.findElement(By.id(xPath)).getAttribute("value");
			if(dataValue!=null && !dataValue.equalsIgnoreCase("") && !dataValue.equalsIgnoreCase(SKIP_STEP)){
				if(actualData.equalsIgnoreCase(dataValue)){
					reportObject.Log("Dyna Table Date Validation", "Expected Date :: "+dataValue + " Actual Value :: "+actualData , Status.PASS,driver, testCase, scenario, browser, passScreenshot, browserFolder);
					isValid=true;
				}else{
					reportObject.Log("Dyna Table Date Validation", "Expected Date :: "+dataValue + " Actual Value :: "+actualData , Status.FAIL,driver, testCase, scenario, browser, passScreenshot, browserFolder);
				}
			}else{
				reportObject.Log("Dyna Table Date Validation not Done", "", Status.DONE,driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
			return isValid;
    	}
      
      /**
       * Method Name : validateDynaTableData
       * Return Type : Nothing
       * Description : This method is used to validate data field values in a dyna table. 
       *  It will verify all the data in the dyna table with few restrictions, 
       *  If the dyna table has data which is not wrapped and it is in 2 lines  
       *  i.e; From HTML perspective, if they are separated by a  tag (<br />) or scenarios where the data 
       *  is not a direct text i.e. they are either a hyperlink or span is explicitly mentioned to the cell. 
       *  This can be used to Verify Expected data in the Summary Screens wherever required.
       *  e.g. input in Scenario Sheet
       *  1,2,Refer - Mandatory TANF
       *  where 
       *  1 = row Number i.e 1st row in this case
       *  2 = Column Number i.e 2nd Column in this case
       *  Refer - Mandatory TANF = Data to be Verified or Expected Value in this case
       */
      
	public static void validateDynaTableData(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		String data[] = dataValue.split(",");
		String row = data[0];
		String column = data[1];
		String expectedValue = "";
		StringBuffer tempData = new StringBuffer();
		if (data.length == 3) {
			expectedValue = data[2];
		} else if (data.length > 3) {
			for (int i = 2; i < data.length; i++) {
				tempData.append(data[i]);
				if (i != data.length - 1) {
					tempData.append(",");
				}
			}
			expectedValue = tempData.toString().trim();
		}

		reportObject.Log("Validation of Dyna Table Data", "Expected Data :: "
				+ expectedValue, Status.DONE,driver, testCase, scenario, browser, passScreenshot, browserFolder);
		String objectIds[] = objectId.replaceAll("\\?", "1").split(";");
		String toBeVerifiedObject = objectIds[Integer.parseInt(String
				.valueOf(Integer.parseInt(column) - 1))];
		String xPathtemp = toBeVerifiedObject.replaceFirst("1", row);
		String xPath = xPathtemp.split("xpath:")[1];
		String actualValue = driver.findElement(By.xpath(xPath))
				.getAttribute("innerHTML").replaceAll("&nbsp;", " ");
		if (expectedValue != null && !expectedValue.equalsIgnoreCase("")
				&& !expectedValue.equalsIgnoreCase(SKIP_STEP)) {
			if (expectedValue
					.replaceAll(" ", "")
					.trim()
					.equalsIgnoreCase(
							actualValue.replaceAll(" ", "")
									.replaceAll("\t", "").trim())) {
				reportObject.Log("Validation of Dyna Table Data", "Expected Data :: "
						+ expectedValue + "  Actual Data :: " + actualValue,
						Status.PASS,driver, testCase, scenario, browser, passScreenshot, browserFolder);
			} else {
				reportObject.Log("Validation of Dyna Table Data", "Expected Data :: "
						+ expectedValue + "  Actual Data :: " + actualValue,
						Status.FAIL,driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
		} else {
			reportObject.Log("Validation of Dyna Table Data", " Skipped"
					+ actualValue, Status.DONE,driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}

	}
      
      /**
  	 * Method Name: jsOpenNewWindow
  	 * Return Type: Nothing
  	 * Description: This method will open a new browser window with specified url in getData 
  	 */
	public void jsOpenNewWindow(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder) {
		Report reportObject = new Report();
		StringBuilder sb = new StringBuilder();
		sb.append("window.open('" + dataValue + "').focus();");

		executeJavaScript(sb.toString(), driver);
		Set<String> allWindowHandles = driver.getWindowHandles();
		for (String currentWindowHandle : allWindowHandles) {
			if (!currentWindowHandle.equals(WebDriverHelper.mainWindowHandle)) {
				try {
					driver.switchTo().window(currentWindowHandle);
					// driver.close();
				} catch (Exception e) {
					logger.warning("Failed to give control to window with windowhandle:"
							+ currentWindowHandle);
					logger.warning(e.getMessage());
				}
			}
		}

		reportObject.Log("Opened a new window", "", Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
	}
     /**
      * Method Name : handleAlertBox
      * Return Type: Nothing
      * Description :  This method will handle the Alert box Click Events. 
      */
	public static void handleAlertBox(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		Alert alert = driver.switchTo().alert();
		reportObject.Log("Alert Box Confirmation", "Expected Data :: "+dataValue , Status.DONE, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		if(dataValue.equalsIgnoreCase("OK")||dataValue.equalsIgnoreCase("YES"))
		{
			reportObject.Log("Alert Box Confirmation", "Expected Data :: "+ dataValue, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			alert.accept(); 
		}
		else if(dataValue.equalsIgnoreCase("No") || dataValue.equalsIgnoreCase("Cancel")){
			reportObject.Log("Validation of Dyna Table Data", "Expected Data :: "+ dataValue, Status.PASS, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			alert.dismiss();
		}else{
			reportObject.Log("Validation of Dyna Table Data", "Expected Data :: "+ dataValue, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
	}
	/**
	 * Method Name: verifyCheckBox
	 * Return Type: Nothing
	 * Description: This method verifies whether a check box is checked/unchecked
	 * Method Arguments Possible Vales : checked/unchecked
	 */
	public void verifyCheckBox(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		Report reportObject = new Report();
		if(dataValue.equalsIgnoreCase("checked")){
			if(findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).isSelected()){
				reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is checked", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is not checked", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}
		}else if(dataValue.equalsIgnoreCase("unchecked")){
			if(!findElementByType(driver, scenario, testCase, homePath, currentIteration.intValue(), objectId, browser, passScreenshot, browserFolder).isSelected()){
				reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is unchecked", Status.pass, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}else{
				reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is checked", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			}			
		}
		else{
			reportObject.Log("Verifying the State of the checkbox "+objectName, "The checkbox "+objectName+" is FAIL", Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);			
		}
	}
	
	/**
	 * Method Name : switchToWindow
	 * Return Type: Nothing
	 * Description : This method will switch the control from parent window to child window
	 * This keyword is used to transfer the control from parent window to child window when we 
	 * click on search icon of a field
	 */
	public void switchToWindow(String homePath, String testCase, String scenario,String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, Integer currentIteration, Boolean error, String browserFolder){
		for (String handle : driver.getWindowHandles()) {
		    driver.switchTo().window(handle);
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
		}catch(Exception e){
		
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