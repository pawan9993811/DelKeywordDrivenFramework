package SupportLibraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import SupportLibraries.Report.Status;

public class ExecuteScripts{
	public Map<String, List<String>> result2;
	public String resultsFolder="";
	public String dataPath = "";
	public String testCase = "";
	public String testCaseDesc = "";
	public String Node = "";
	public String browser = "";
	public String methodName = "";
	public String keyword = "";
	public String objectName = "";
	public String objectID = "";
	public String dataValue = "";
	public String passScreenshot = "";
	public String onPassLog = "";
	public String onFailLog = "";
	public static String resultsPath = "";
	public static String dateNTime = "";
	public static String executionType = "";
	public int startIteration = 1;
	public int endIteration = 1;
	public int totalIterations = 1;
	public int currentIteration = 1;
	long startTime = 0;
	long endTime = 0;
	public long duration = 0;
	public long totalDuration = 0;
	public static long overallDuration = 0;
	public static int totalTestCases = 0;
	public static int totalTestCasesExecuted = 0;
	public static HashMap<String, Integer> ModuleTotalTestCases = new HashMap<String, Integer>();
	public static HashMap<String, Integer> ModuleTestCasesExecuted = new HashMap<String, Integer>();
	public static HashMap<String, Long> ModuleTotalDuration = new HashMap<String, Long>();
	public static HashMap<String, Boolean> error = new HashMap<String, Boolean>();
	public ArrayList<String> businessModules = new ArrayList<String>();
	public int l;
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: ExecuteModule
	 * Description: This method is to execute the entire code for each module and will be called from the "SupportLibraries.TestNG_Configuration.java" file
	 * Return Type: Nothing
	 */
	public void ExecuteModule(String module, String resultsFolder){
		try {
									
			//Creating objects of class WebDriver, POI_ReadExcel and Report classes respectively
			WebDriver driver = null;
			executionType = "Parallel";
			POI_ReadExcel poiObject = new POI_ReadExcel();
			Report reportObject = new Report();
					
			//Getting the canonical path of the framework
			String homePath = new File (".").getCanonicalPath();
			
			//Setting the path of excel sheet where user selects the Test Cases to execute to the variable 'path'
			String path = homePath+"\\TestCaseSelection_Modular.xlsx";
			
			//Counting the test cases that are marked Yes for execution in the module sheet in TestCaseSelection.xlsx
			List<String> whereClause1 = new ArrayList<String>();
			whereClause1.add("Execute::Yes");
			Map<String, List<String>> result1 = poiObject.fetchWithCondition(path, module, whereClause1);
			ModuleTestCasesExecuted.put(module, result1.get("Execute").size());
			
			//Counting the test cases that are present in the module sheet in TestCaseSelection.xlsx
			ModuleTotalTestCases.put(module, 0);
			List<String> whereClause3 = new ArrayList<String>();
			Map<String, List<String>> result3 = poiObject.fetchWithCondition(path, module, whereClause3);
			ModuleTotalTestCases.put(module, ModuleTotalTestCases.get(module)+result3.get("Execute").size());
			
			//Storing the list of Browsers for the module in an array by splitting the value from Browser column of that module in TestCaseSelection.xlsx
			String[] ListOfBrowsers = null;
			ListOfBrowsers = result1.get("Browser").get(0).split(",");
			
			
			//Checking if there is at-least 1 test case marked Yes for execution in the module sheet of TestCaseSelection.xlsx sheet
			if(result1.get("Execute").size()==0){
				//In case there is no test case marked Yes for execution, adding the (Number of test cases of that module * Number of browsers) to Overall count of test cases
				for(int z=0;z<ListOfBrowsers.length;z++){
					calculateTotalNumberOfTestCases(result3.get("Execute").size());
				}
			}else if(result1.get("Execute").size()!=0){
				//When there is at-least 1 test case marked Yes for execution
				Node=result1.get("VM").get(0);//setting the value for Node
				
				//Loop for repeating the code for each browser (comma separated in Browser column of module sheet)
				for(int z=0;z<ListOfBrowsers.length;z++){
					//Selecting and invoking the driver object of that particular browser using method SelectDriver
					browser = ListOfBrowsers[z];
					driver = SelectDriver(browser,Node);
					driver.manage().window().maximize();
					
					//Setting the variable for calculating total duration of module for current browser to 0
					totalDuration = 0;
					
					//Adding the module's total number of Executed test cases and total test cases per browser to the final count
					calculateTotalNumberOfExecutedTestCases(result1.get("Execute").size());
					calculateTotalNumberOfTestCases(result3.get("Execute").size());
					
					//Creating the Module and corresponding browser folder inside the module folder in Results
					String testCaseHTMLPath = "";
					String scenarioFolder = CreateScenarioFolder(resultsFolder, module);
					String browserFolder = CreateBrowserFolder(scenarioFolder, browser);
					
					//Creating a module Summary HTML file for the current browser inside the above folder of the browser
					String scenarioBrowserHTML = reportObject.CreateScenarioHTML(browserFolder,module, browser,Node);

					//Loop for iterating through the test cases marked Yes one by one in the current browser and current module
					for(l=0; l<result1.get("Execute").size(); l++){
						try {
							//Marking the error value for the test case as False (error is used as flag to skip further steps of test case in case of exception)
							error.put(module+"_"+testCase, false);
							
							//Storing the Execution start time of the Test Case
							Calendar cal1 = Calendar.getInstance();
							startTime = cal1.getTimeInMillis();
							
							//Storing the test case and test case description values from the TestCaseSelection.xlsx sheet of module into variables
							testCase = result1.get("TestScript").get(l);
							testCaseDesc = result1.get("Description").get(l);
							
						      
							//Storing the Number of Iterations and the start index in the Integer variables. Setting the default values to 1.
							try {
								totalIterations = (int)(Float.parseFloat(result1.get("NumberOfIterations").get(l)));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								totalIterations = 1;
							}
							
							try {
								startIteration = (int)(Float.parseFloat(result1.get("StartIndexForIterations").get(l)));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								startIteration = 1;
							}
							
							try {
								endIteration = startIteration+(totalIterations-1);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								endIteration = startIteration;
							}
							
							//Setting the path of Test Data sheets to the variable 'dataPath'
							dataPath = homePath+"\\TestData\\"+module+".xlsx";
														
							//Creating a HTML file for the test case inside the corresponding module's current browser folder
							testCaseHTMLPath = reportObject.CreateTestCaseHTML(browserFolder, testCase, module);
							
							//Loop for repeating the test case execution based on the start and end iteration given for a test case
							for(int i=startIteration;i<=endIteration;i++){
								//Assigning the error flag as False
								error.put(module+"_"+testCase, false);
								//Assigning the current Iteration that is currently executing
								currentIteration = i;
								//Writing to the Test Case HTML result file, the current iteration that is starting
								reportObject.WriteIterationToHTML(testCaseHTMLPath, currentIteration, startIteration, error.get(module+"_"+testCase));											
								
								//Reading the sheet "TestSteps" in the test data workbook of the module and collecting all the steps of this test case into an array list
								List<String> whereClause2 = new ArrayList<String>();
								whereClause2.add("TestScript::"+testCase);
								Map<String, List<String>> result2 = poiObject.fetchWithCondition(dataPath, "TestSteps", whereClause2);
								
								//Loop for iterating through each step of the test case/test script
								for(int k=0; k<result2.get("TestScript").size(); k++){
									if(testCase.equalsIgnoreCase(result2.get("TestScript").get(k))){
										//Storing the value of the column "Keyword" from the sheet "TestSteps"
										keyword = result2.get("Keyword").get(k);
										
										//Storing the value of the column "ObjectID" from the sheet "TestSteps"
										Util utilObject = new Util();
										try{
											objectName = result2.get("ObjectID").get(k);
											//storing the object property from object repository
											objectID = utilObject.getObjectFromObjectMap(objectName, module);
										}catch(Exception e){
											// TODO Auto-generated catch block
										}
										
										//Storing the value of the column "KeyInData" from the sheet "TestSteps"
										try {
											//Directly storing the value present in the column "KeyInData" for current step (hard coded data)
											dataValue = result2.get("KeyInData").get(k);
											
											//When the value in column starts with text "getData=", then connecting with sheet "TestData" and reading the data in mentioned column and storing that data returned in the variable
											//When the value in column starts with text "getDataINI=", then connecting with OutputData.INI file and reading the data in mentioned Section and Key and storing that data returned in the variable
											if(dataValue.startsWith("getData=")){
												dataValue = utilObject.getData(dataValue.split("getData=")[1],driver, module, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder);
											}else if(dataValue.startsWith("getDataINI=")){
												String parameters = dataValue.split("getDataINI=")[1].trim();
												dataValue = utilObject.getDataINI(parameters.split(";")[0].trim(),parameters.split(";")[1].trim());
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											
										}
										
										//Storing the value of pass screenshot column
										try{
											passScreenshot = result2.get("PassScreenshot").get(k);
											if(passScreenshot == null){
												passScreenshot="";
											}
										}catch(Exception e){
											// TODO Auto-generated catch block
											
										}
										
										//Storing the value of OnPassLogMsg column
										try {
											onPassLog = result2.get("OnPassLogMsg").get(k);
											if(onPassLog==null){
												onPassLog = "";
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											onPassLog = "";
										}
										
										//Storing the value of OnFailLogMsg column
										try {
											onFailLog = result2.get("OnFailLogMsg").get(k);
											if(onFailLog==null){
												onFailLog = "";
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											onFailLog = "";
										}
										
										//Checking if the error flag is False or it has been set to True by previous steps. Current step executed only if error flag is false
										if(error.get(module+"_"+testCase)==false){
											//Checking if the keyword value does not end with "#". If a keyword has "#" at end, that keyword is skipped
											if(!keyword.endsWith("#")){
												//Checking if keyword starts with "function=". If it starts with "function=", then framework searches for matching method in ReusableFunctions.ReusableFunctions.java and invokes that method
												if(keyword.startsWith("function=")){
													methodName = keyword.split("function=")[1].trim();
													ExecuteMethod(homePath, testCase, module, browser, objectID, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error.get(module+"_"+testCase), browserFolder);
												}else{
													//If keyword does not start with "function=", then framework searches for matching method in SupportLibraries.KeywordLibrary.java and invokes that method
													ExecuteKeyword(homePath, testCase, module, browser, objectID, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error.get(module+"_"+testCase), browserFolder);
												}
											}
											
										}
										
									}																					
								}
							}
							
							//Storing the Execution End Time of the Test Case (after all iterations are completed)
							Calendar cal2 = Calendar.getInstance();
							endTime = cal2.getTimeInMillis();
							duration = endTime-startTime;
							//Adding the durations of individual Test Cases to display in the Scenario (module) HTML file of current browser
							totalDuration=totalDuration+duration;
							
							//Method to add a row in the Scenario HTML file at the end of execution of a particular test case
							reportObject.AddRowToScenarioHTML(scenarioBrowserHTML, module, testCase, testCaseDesc, browser, duration,Node);
							
							//Method to Close the Test Case HTML file by adding the Test Case Summary at the end of the result file
							reportObject.closeTestCaseHTML(testCaseHTMLPath, testCase, module, duration, startIteration, endIteration, browser);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							reportObject.Log("Exception", "Exception "+e+" occurred while executing the test case "+testCase,Status.FAIL, driver, testCase, module, browser, passScreenshot, browserFolder);
							//Storing the Execution End Time of the Test Case
							Calendar cal2 = Calendar.getInstance();
							endTime = cal2.getTimeInMillis();
							duration = endTime-startTime;
							//Adding the durations of individual Test Cases to display in the Scenario HTML file
							totalDuration=totalDuration+duration;
							
							//Method to add a row in the Scenario HTML file at the end of execution of a particular test case
							reportObject.AddRowToScenarioHTML(scenarioBrowserHTML, module, testCase, testCaseDesc, browser, duration,Node);
							
							//Method to Close the Test Case HTML file by adding the Test Case Summary at the end of the result file
							reportObject.closeTestCaseHTML(testCaseHTMLPath, testCase, module, duration, startIteration, endIteration, browser);
						}
					}
					//Method to close the Scenario HTML file at the end of execution of the Scenario for a browser
					reportObject.closeScenarioHTML(module, browser, scenarioBrowserHTML, totalDuration);
					reportObject.AddRowToSummary(module, browser, resultsFolder+"\\Summary.htm", totalDuration, dateNTime, Node);
					
					driver.manage().deleteAllCookies(); 
					
					//Closing the browser at the end of execution of that particular browser
					try{
						driver.close();
					}catch(Exception e){
						
					}					
				}
				//Quitting the driver object at the end of execution
				driver.quit();
			}
						
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: ExecuteDriver
	 * Description: This method is to execute the entire code for all test cases from TestCaseSelection.xlsx sheet for each browser and will be called from the "DriverScript.StandaloneDriver.java" file
	 * Return Type: Nothing
	 */
	public void ExecuteDriver(WebDriver driver, String module, String browser, String resultsFolder){
		try {

			HashMap<String, Integer> ModuleCurrentExecutingTC = new HashMap<>();
			
			//Creating objects of class WebDriver, POI_ReadExcel and Report classes respectively
			POI_ReadExcel poiObject = new POI_ReadExcel();
			Report reportObject = new Report();
			
			executionType = "Standalone";
				
			//Getting the canonical path of the framework
			String homePath = new File (".").getCanonicalPath();
			
			//Setting the path of excel sheet where user selects the Test Cases to execute to the variable 'path'
			String path = homePath+"\\TestCaseSelection_Singlesheet.xlsx";
			
			//XSSFSheet sheet=poiObject.RemoveBlankRows(path,"TestCases");
			//result1 is for counting cases which are marked yes for execution
			//result2 is to create a mapping between module and associated rows
			//result3 is to capture all the tests which need to be executed
			//result4 is to map each module with each test case
			
			//Counting the test cases that are marked Yes for execution in the module sheet in TestCaseSelection.xlsx
			List<String> whereClause1 = new ArrayList<String>();
			whereClause1.add("Execute::Yes");
			Map<String, List<String>> result1 = poiObject.fetchWithCondition(path,"TestCases", whereClause1);

			for(int k=0;k<result1.get("Execute").size();k++){
				module = result1.get("BusinessModule").get(k);
				ModuleCurrentExecutingTC.put(module, 0);
				List<String> whereClause2 = new ArrayList<String>();
				whereClause2.add("Execute::Yes");
				whereClause2.add("BusinessModule::"+module);
				result2 = poiObject.fetchWithCondition(path,"TestCases", whereClause2);
				ModuleTestCasesExecuted.put(module, result2.get("Execute").size());
				
			}

			ArrayList<String> modulesList = new ArrayList<String>();
			List<String> whereClause4 = new ArrayList<String>();
			
			Map<String, List<String>> result4 = poiObject.fetchWithCondition(path, "TestCases", whereClause4);
			for(int k=0;k<result4.get("Execute").size();k++){
				module = result4.get("BusinessModule").get(k);
				ModuleTotalTestCases.put(module, 0);
				ModuleTotalDuration.put(module, (long) 0);
			}

			
			//Counting the total number of test cases
			//calculateTotalNumberOfTestCases(result4.get("Execute").size());
			
			//Counting the number of test cases in the particular module from TestCaseSelection.xlsx
			Map<String, List<String>> result3 = null;
			for(int k=0;k<result4.get("Execute").size();k++){
				module = result4.get("BusinessModule").get(k);
				if(!module.isEmpty()){
				List<String> whereClause3 = new ArrayList<String>();
				whereClause3.add("BusinessModule::"+module);
				result3 = poiObject.fetchWithCondition(path, "TestCases", whereClause3);
				}
				else
					continue;
							
				boolean found = false;
				for(int z=0;z<modulesList.size();z++){
					if(modulesList.get(z).equalsIgnoreCase(module)){
						found = true;
					} 
				}
				if(!found){
					ModuleTotalTestCases.put(module, ModuleTotalTestCases.get(module)+result3.get("Execute").size());
					modulesList.add(module);
				}

			}
			
			//Counting the test cases that are present in the module sheet in TestCaseSelection.xlsx
			
			//Checking if there is at-least 1 test case marked Yes for execution in the module sheet of TestCaseSelection.xlsx sheet
			if(result1.get("Execute").size()==0){
				
				calculateTotalNumberOfTestCases(result4.get("Execute").size());
				
			}else if(result1.get("Execute").size()!=0){
					//When there is at-least 1 test case marked Yes for execution
				
					//Adding the module's total number of Executed test cases and total test cases per browser to the final count
					calculateTotalNumberOfExecutedTestCases(result1.get("Execute").size());
					calculateTotalNumberOfTestCases(result4.get("Execute").size());
					String scenarioFolder = "";
					String browserFolder = 	"";	
					String scenarioBrowserHTML = "";
					String testCaseHTMLPath = "";
					
					//Loop for iterating through the test cases marked Yes one by one in the current browser and current module
					for(l=0; l<result1.get("Execute").size(); l++){
						try {
							//Creating the Module and corresponding browser folder inside the module folder in Results
							
							module = result1.get("BusinessModule").get(l);
							ModuleCurrentExecutingTC.put(module, ModuleCurrentExecutingTC.get(module)+1);
							scenarioFolder = CreateScenarioFolder(resultsFolder, module);
							browserFolder = CreateBrowserFolder(scenarioFolder, browser);
							
							//Creating a module Summary HTML file for the current browser inside the above folder of the browser
							scenarioBrowserHTML = reportObject.CreateScenarioHTML(browserFolder,module, browser, Node);
							Report.logDefects(resultsFolder);
							
							//Assigning the error flag as False
							error.put(module+"_"+testCase, false);
							
							//Storing the Execution start time of the Test Case
							Calendar cal1 = Calendar.getInstance();
							startTime = cal1.getTimeInMillis();
							
							//Storing the test case and test case description values from the TestCaseSelection.xlsx sheet of module into variables
							testCase = result1.get("TestScript").get(l);
							
							testCaseDesc = result1.get("Description").get(l);
							module=result1.get("BusinessModule").get(l);
													
							//Storing the Number of Iterations and the start index in the Integer variables. Setting the default values to 1.
							try {
								totalIterations = (int)(Float.parseFloat(result1.get("NumberOfIterations").get(l)));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								totalIterations = 1;
							}
							
							try {
								startIteration = (int)(Float.parseFloat(result1.get("StartIndexForIterations").get(l)));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								startIteration = 1;
							}
							
							try {
								endIteration = startIteration+(totalIterations-1);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								endIteration = startIteration;
							}
							
							//Setting the path of Test Data/Business flow sheets to the variable 'dataPath'
							//dataPath = homePath+"\\TestData\\"+testCase+".xlsx";
							dataPath = homePath+"\\TestData\\"+module+".xlsx";
														
							//Creating a HTML file for the test case inside the corresponding module's current browser folder
							testCaseHTMLPath = reportObject.CreateTestCaseHTML(browserFolder, testCase, module);
							
							
							//Loop for repeating the test case execution based on the start and end iteration given for a test case
							for(int i=startIteration;i<=endIteration;i++){
								//Setting the error flag to False
								error.put(module+"_"+testCase, false);
								
								//Assigning the current Iteration that is currently executing
								currentIteration = i;
								reportObject.WriteIterationToHTML(testCaseHTMLPath, currentIteration, startIteration, error.get(module+"_"+testCase));											
								
								//Reading the sheet "TestSteps" in the test data workbook of the module and collecting all the steps of this test case into an array list
								List<String> whereClause2 = new ArrayList<String>();
								whereClause2.add("TestScript::"+testCase);
								Map<String, List<String>> result2 = poiObject.fetchWithCondition(dataPath, "TestSteps", whereClause2);
								
								//Loop for iterating through each step of the test case/test script
								for(int k=0; k<result2.get("TestScript").size(); k++){
									if(testCase.equalsIgnoreCase(result2.get("TestScript").get(k))){
										//Storing the value of the column "Keyword" from the sheet "TestSteps"
										keyword = result2.get("Keyword").get(k);
										
										//Storing the value of the column "ObjectID" from the sheet "TestSteps"
										Util utilObject = new Util();
										try{
											objectName = result2.get("ObjectID").get(k);
											//objectID = utilObject.getObjectFromObjectMap(objectName, testCase);
											objectID = utilObject.getObjectFromObjectMap(objectName, module);
										}catch(Exception e){
											// TODO Auto-generated catch block
										}
										
										//Storing the value of the column "KeyInData" from the sheet "TestSteps"
										try {
											//Directly storing the value present in the column "KeyInData" for current step (hard coded data)
											dataValue = result2.get("KeyInData").get(k);
											//When the value in column starts with text "getData=", then connecting with sheet "TestData" and reading the data in mentioned column and storing that data returned in the variable
											//When the value in column starts with text "getDataINI=", then connecting with OutputData.INI file and reading the data in mentioned Section and Key and storing that data returned in the variable
											if(dataValue.startsWith("getData=")){
												dataValue = utilObject.getData(dataValue.split("getData=")[1],driver, module, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder);
											}else if(dataValue.startsWith("getDataINI=")){
												String parameters = dataValue.split("getDataINI=")[1].trim();
												dataValue = utilObject.getDataINI(parameters.split(";")[0].trim(),parameters.split(";")[1].trim());
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											
										}
										
										//Storing the value of pass screenshot column
										try{
											passScreenshot = result2.get("PassScreenshot").get(k);
											if(passScreenshot == null){
												passScreenshot="";
											}
										}catch(Exception e){
											// TODO Auto-generated catch block
											
										}
										
										//Storing the value of OnPassLogMsg column
										try {
											onPassLog = result2.get("OnPassLogMsg").get(k);
											if(onPassLog==null){
												onPassLog = "";
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											onPassLog = "";
										}
										
										//Storing the value of OnFailLogMsg column
										try {
											onFailLog = result2.get("OnFailLogMsg").get(k);
											if(onFailLog==null){
												onFailLog = "";
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											onFailLog = "";
										}
										
										//Checking if the error flag is False or it has been set to True by previous steps. Current step executed only if error flag is false
										if(error.get(module+"_"+testCase)==false){
											//Checking if the keyword value does not end with "#". If a keyword has "#" at end, that keyword is skipped
											if(!keyword.endsWith("#")){
												//Checking if keyword starts with "function=". If it starts with "function=", then framework searches for matching method in ReusableFunctions.ReusableFunctions.java and invokes that method
												if(keyword.startsWith("function=")){
													methodName = keyword.split("function=")[1].trim();
													ExecuteMethod(homePath, testCase, module, browser, objectID, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error.get(module+"_"+testCase), browserFolder);
												}else{
													//If keyword does not start with "function=", then framework searches for matching method in SupportLibraries.KeywordLibrary.java and invokes that method
													ExecuteKeyword(homePath, testCase, module, browser, objectID, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error.get(module+"_"+testCase), browserFolder);
												}
											}
											
										}

										

									}			
									
								}
								
								//Storing the Execution End Time of the Test Case (after all iterations are completed)
								Calendar cal2 = Calendar.getInstance();
								endTime = cal2.getTimeInMillis();
								duration = endTime-startTime;
								//Adding the durations of individual Test Cases to display in the Scenario (module) HTML file
								ModuleTotalDuration.put(module, ModuleTotalDuration.get(module)+duration);
								//totalDuration=totalDuration+duration;
								//Method to add a row in the Scenario HTML file at the end of execution of a particular test case
							
								reportObject.AddRowToScenarioHTML(scenarioBrowserHTML,module,testCase,testCaseDesc,browser, duration,"");
								
								//Method to Close the Test Case HTML file by adding the Test Case Summary at the end of the result file
								reportObject.closeTestCaseHTML(testCaseHTMLPath, testCase, module, duration, startIteration, endIteration,browser);
							}
							
						
							
							if(ModuleCurrentExecutingTC.get(module)==ModuleTestCasesExecuted.get(module)){
								//Method to close the Scenario HTML file at the end of execution of the Scenario for a browser
								reportObject.closeScenarioHTML(module, browser, scenarioBrowserHTML, ModuleTotalDuration.get(module));
								reportObject.AddRowToSummary(module, browser, resultsFolder+"\\Summary.htm", ModuleTotalDuration.get(module), dateNTime, "");
							}
							
							
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							reportObject.Log("Exception", "Exception "+e+" occurred while executing the test case "+testCase,Status.FAIL, driver, testCase, module, browser, passScreenshot, browserFolder);
							
							//Storing the Execution End Time of the Test Case
							Calendar cal2 = Calendar.getInstance();
							endTime = cal2.getTimeInMillis();
							duration = endTime-startTime;
							
							//Adding the durations of individual Test Cases to display in the Scenario HTML file
							totalDuration=totalDuration+duration;
							
							//Method to add a row in the Scenario HTML file at the end of execution of a particular test case
							reportObject.AddRowToScenarioHTML(scenarioBrowserHTML, module, testCase, testCaseDesc, browser, duration,Node);
							
							//Method to Close the Test Case HTML file by adding the Test Case Summary at the end of the result file
							reportObject.closeTestCaseHTML(testCaseHTMLPath, testCase, module, duration, startIteration, endIteration, browser);
							
							if(ModuleCurrentExecutingTC.get(module)==ModuleTestCasesExecuted.get(module)){
								//Method to close the Scenario HTML file at the end of execution of the Scenario for a browser
								reportObject.closeScenarioHTML(module, browser, scenarioBrowserHTML, ModuleTotalDuration.get(module));
								reportObject.AddRowToSummary(module, browser, resultsFolder+"\\Summary.htm", ModuleTotalDuration.get(module), dateNTime, "");
							}
						}
					}
				}
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: ExecuteModularDriver
	 * Description: This method is to execute the entire code for each module and browser and will be called from the "DriverScript.StandaloneDriver.java" file
	 * Return Type: Nothing
	 */
	public void ExecuteModularDriver(WebDriver driver, String module, String browser, String resultsFolder){
		try {
			
			Node="";
			
			//Creating objects of class WebDriver, POI_ReadExcel and Report classes respectively
			POI_ReadExcel poiObject = new POI_ReadExcel();
			Report reportObject = new Report();
				
			//Getting the canonical path of the framework
			String homePath = new File (".").getCanonicalPath();
			
			//Setting the path of excel sheet where user selects the Test Cases to execute to the variable 'path'
			String path = homePath+"\\TestCaseSelection_Modular.xlsx";
			
			//Counting the test cases that are marked Yes for execution in the module sheet in TestCaseSelection.xlsx
			List<String> whereClause1 = new ArrayList<String>();
			whereClause1.add("Execute::Yes");
			Map<String, List<String>> result1 = poiObject.fetchWithCondition(path, module, whereClause1);
			ModuleTestCasesExecuted.put(module, result1.get("Execute").size());
			
			//Counting the test cases that are present in the module sheet in TestCaseSelection.xlsx
			ModuleTotalTestCases.put(module, 0);
			List<String> whereClause3 = new ArrayList<String>();
			Map<String, List<String>> result3 = poiObject.fetchWithCondition(path, module, whereClause3);
			ModuleTotalTestCases.put(module, ModuleTotalTestCases.get(module)+result3.get("Execute").size());
			
			//Checking if there is at-least 1 test case marked Yes for execution in the module sheet of TestCaseSelection.xlsx sheet
			if(result1.get("Execute").size()==0){
				//In case there is no test case marked Yes for execution, adding the Number of test cases of that module to Overall count of test cases
				calculateTotalNumberOfTestCases(result3.get("Execute").size());
			}else if(result1.get("Execute").size()!=0){
					//When there is at-least 1 test case marked Yes for execution
				
					//Setting the variable for calculating total duration of module for current browser to 0
					totalDuration = 0;
					
					//Adding the module's total number of Executed test cases and total test cases per browser to the final count
					calculateTotalNumberOfExecutedTestCases(result1.get("Execute").size());
					calculateTotalNumberOfTestCases(result3.get("Execute").size());
					
					//Creating the Module and corresponding browser folder inside the module folder in Results
					String testCaseHTMLPath = "";
					String scenarioFolder = CreateScenarioFolder(resultsFolder, module);
					String browserFolder = CreateBrowserFolder(scenarioFolder, browser);
					
					//Creating a module Summary HTML file for the current browser inside the above folder of the browser
					String scenarioBrowserHTML = reportObject.CreateScenarioHTML(browserFolder,module, browser, Node);

					//Loop for iterating through the test cases marked Yes one by one in the current browser and current module
					for(int l=0; l<result1.get("Execute").size(); l++){
						try {
							//Assigning the error flag as False
							error.put(module+"_"+testCase, false);
							
							//Storing the Execution start time of the Test Case
							Calendar cal1 = Calendar.getInstance();
							startTime = cal1.getTimeInMillis();
							
							//Storing the test case and test case description values from the TestCaseSelection.xlsx sheet of module into variables
							testCase = result1.get("TestScript").get(l);
							testCaseDesc = result1.get("Description").get(l);
													
							//Storing the Number of Iterations and the start index in the Integer variables. Setting the default values to 1.
							try {
								totalIterations = (int)(Float.parseFloat(result1.get("NumberOfIterations").get(l)));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								totalIterations = 1;
							}
							
							try {
								startIteration = (int)(Float.parseFloat(result1.get("StartIndexForIterations").get(l)));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								startIteration = 1;
							}
							
							try {
								endIteration = startIteration+(totalIterations-1);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								endIteration = startIteration;
							}
							
							//Setting the path of Test Data/Business flow sheets to the variable 'dataPath'
							dataPath = homePath+"\\TestData\\"+module+".xlsx";
														
							//Creating a HTML file for the test case inside the corresponding module's current browser folder
							testCaseHTMLPath = reportObject.CreateTestCaseHTML(browserFolder, testCase, module);
							
							//Loop for repeating the test case execution based on the start and end iteration given for a test case
							for(int i=startIteration;i<=endIteration;i++){
								//Setting the error flag to False
								error.put(module+"_"+testCase, false);
								
								//Assigning the current Iteration that is currently executing
								currentIteration = i;
								reportObject.WriteIterationToHTML(testCaseHTMLPath, currentIteration, startIteration, error.get(module+"_"+testCase));											
								
								//Reading the sheet "TestSteps" in the test data workbook of the module and collecting all the steps of this test case into an array list
								List<String> whereClause2 = new ArrayList<String>();
								whereClause2.add("TestScript::"+testCase);
								Map<String, List<String>> result2 = poiObject.fetchWithCondition(dataPath, "TestSteps", whereClause2);
								
								//Loop for iterating through each step of the test case/test script
								for(int k=0; k<result2.get("TestScript").size(); k++){
									if(testCase.equalsIgnoreCase(result2.get("TestScript").get(k))){
										//Storing the value of the column "Keyword" from the sheet "TestSteps"
										keyword = result2.get("Keyword").get(k);
										
										//Storing the value of the column "ObjectID" from the sheet "TestSteps"
										Util utilObject = new Util();
										try{
											objectName = result2.get("ObjectID").get(k);
											objectID = utilObject.getObjectFromObjectMap(objectName, module);
										}catch(Exception e){
											// TODO Auto-generated catch block
										}
										
										//Storing the value of the column "KeyInData" from the sheet "TestSteps"
										try {
											//Directly storing the value present in the column "KeyInData" for current step (hard coded data)
											dataValue = result2.get("KeyInData").get(k);
											//When the value in column starts with text "getData=", then connecting with sheet "TestData" and reading the data in mentioned column and storing that data returned in the variable
											//When the value in column starts with text "getDataINI=", then connecting with OutputData.INI file and reading the data in mentioned Section and Key and storing that data returned in the variable
											if(dataValue.startsWith("getData=")){
												dataValue = utilObject.getData(dataValue.split("getData=")[1],driver, module, testCase, homePath, currentIteration, browser, passScreenshot, browserFolder);
											}else if(dataValue.startsWith("getDataINI=")){
												String parameters = dataValue.split("getDataINI=")[1].trim();
												dataValue = utilObject.getDataINI(parameters.split(";")[0].trim(),parameters.split(";")[1].trim());
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											
										}
										
										//Storing the value of pass screenshot column
										try{
											passScreenshot = result2.get("PassScreenshot").get(k);
											if(passScreenshot == null){
												passScreenshot="";
											}
										}catch(Exception e){
											// TODO Auto-generated catch block
											
										}
										
										//Storing the value of OnPassLogMsg column
										try {
											onPassLog = result2.get("OnPassLogMsg").get(k);
											if(onPassLog==null){
												onPassLog = "";
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											onPassLog = "";
										}
										
										//Storing the value of OnFailLogMsg column
										try {
											onFailLog = result2.get("OnFailLogMsg").get(k);
											if(onFailLog==null){
												onFailLog = "";
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											onFailLog = "";
										}
										
										//Checking if the error flag is False or it has been set to True by previous steps. Current step executed only if error flag is false
										if(error.get(module+"_"+testCase)==false){
											//Checking if the keyword value does not end with "#". If a keyword has "#" at end, that keyword is skipped
											if(!keyword.endsWith("#")){
												//Checking if keyword starts with "function=". If it starts with "function=", then framework searches for matching method in ReusableFunctions.ReusableFunctions.java and invokes that method
												if(keyword.startsWith("function=")){
													methodName = keyword.split("function=")[1].trim();
													ExecuteMethod(homePath, testCase, module, browser, objectID, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error.get(module+"_"+testCase), browserFolder);
												}else{
													//If keyword does not start with "function=", then framework searches for matching method in SupportLibraries.KeywordLibrary.java and invokes that method
													ExecuteKeyword(homePath, testCase, module, browser, objectID, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error.get(module+"_"+testCase), browserFolder);
												}
											}
											
										}
										
									}																					
								}
							}
							
							//Storing the Execution End Time of the Test Case (after all iterations are completed)
							Calendar cal2 = Calendar.getInstance();
							endTime = cal2.getTimeInMillis();
							duration = endTime-startTime;
							//Adding the durations of individual Test Cases to display in the Scenario (module) HTML file
							totalDuration=totalDuration+duration;
							
							//Method to add a row in the Scenario HTML file at the end of execution of a particular test case
							reportObject.AddRowToScenarioHTML(scenarioBrowserHTML, module, testCase, testCaseDesc, browser, duration, Node);
							
							//Method to Close the Test Case HTML file by adding the Test Case Summary at the end of the result file
							reportObject.closeTestCaseHTML(testCaseHTMLPath, testCase, module, duration, startIteration, endIteration, browser);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
							reportObject.Log("Exception", "Exception "+e+" occurred while executing the test case "+testCase,Status.FAIL, driver, testCase, module, browser, passScreenshot, browserFolder);
							
							//Storing the Execution End Time of the Test Case
							Calendar cal2 = Calendar.getInstance();
							endTime = cal2.getTimeInMillis();
							duration = endTime-startTime;
							
							//Adding the durations of individual Test Cases to display in the Scenario HTML file
							totalDuration=totalDuration+duration;
							
							//Method to add a row in the Scenario HTML file at the end of execution of a particular test case
							reportObject.AddRowToScenarioHTML(scenarioBrowserHTML, module, testCase, testCaseDesc, browser, duration, Node);
							
							//Method to Close the Test Case HTML file by adding the Test Case Summary at the end of the result file
							reportObject.closeTestCaseHTML(testCaseHTMLPath, testCase, module, duration, startIteration, endIteration, browser);
						}
					}
					//Method to close the Scenario HTML file at the end of execution of the Scenario for a browser
					reportObject.closeScenarioHTML(module, browser, scenarioBrowserHTML, totalDuration);
					reportObject.AddRowToSummary(module, browser, resultsFolder+"\\Summary.htm", totalDuration, dateNTime, "");
			
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Report.logDefects(resultsFolder);
	}

	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: ExecuteMethod
	 * Description: This method is to execute the method that matches with the Method name given in the Input sheet for a particular test case in the Scenario
	 * Return Type: Nothing
	 */
	public void ExecuteMethod(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, int currentIteration, boolean errorValue, String browserFolder){
		Report reportObject = new Report();
		try{
			Class<?> cls = Class.forName("ReusableFunctions.ReusableFunctions");
			Object obj = cls.newInstance();
	 
			Class<?>[] cArg = new Class[14];
			cArg[0] = String.class;
	        cArg[1] = String.class;
			cArg[2] = String.class;
			cArg[3] = String.class;
	        cArg[4] = String.class;
	        cArg[5] = String.class;
	        cArg[6] = String.class;
	        cArg[7] = String.class;
	        cArg[8] = String.class;
	        cArg[9] = WebDriver.class;
	        cArg[10] = String.class;
	        cArg[11] = Integer.class;
	        cArg[12] = Boolean.class;
	        cArg[13] = String.class;
	       
			Method method = cls.getDeclaredMethod(methodName, cArg);
			List<Object> strArr = new ArrayList<Object>();
			strArr.add(homePath);
			strArr.add(testCase);
			strArr.add(scenario);
			strArr.add(browser);
			strArr.add(objectId);
			strArr.add(objectName);
			strArr.add(dataValue);
			strArr.add(onPassLog);
			strArr.add(onFailLog);
			strArr.add(driver);
			strArr.add(passScreenshot);
			strArr.add(currentIteration);
			strArr.add(errorValue);
			strArr.add(browserFolder);
			method.invoke(obj, strArr.toArray());

		}catch(Exception e){
			e.printStackTrace();
			//Setting the error flag to true indicating an exception has occurred
			error.put(scenario+"_"+testCase, true);
			reportObject.Log("Unhandled Exception", "There was an Unhandled Exception while executing the method "+methodName+". Exception: "+e, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
		}
		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: ExecuteKeyword
	 * Description: This method is to execute the method that matches with the Keyword given in the Input sheet for a particular test case in the Scenario
	 * Return Type: Nothing
	 */
	public void ExecuteKeyword(String homePath, String testCase, String scenario, String browser, String objectId, String objectName, String dataValue, String onPassLog, String onFailLog, WebDriver driver, String passScreenshot, int currentIteration, boolean errorValue, String browserFolder){
		Report reportObject = new Report();
		
		try{
			Class<?> cls = Class.forName("SupportLibraries.KeywordLibrary");
			Object obj = cls.newInstance();
	 
			Class<?>[] cArg = new Class[14];
			cArg[0] = String.class;
	        cArg[1] = String.class;
			cArg[2] = String.class;
			cArg[3] = String.class;
	        cArg[4] = String.class;
	        cArg[5] = String.class;
	        cArg[6] = String.class;
	        cArg[7] = String.class;
	        cArg[8] = String.class;
	        cArg[9] = WebDriver.class;
	        cArg[10] = String.class;
	        cArg[11] = Integer.class;
	        cArg[12] = Boolean.class;
	        cArg[13] = String.class;
	       
			Method method = cls.getDeclaredMethod(keyword, cArg);
			
			List<Object> strArr = new ArrayList<Object>();
			strArr.add(homePath);
			strArr.add(testCase);
			strArr.add(scenario);
			strArr.add(browser);
			strArr.add(objectId);
			strArr.add(objectName);
			strArr.add(dataValue);
			strArr.add(onPassLog);
			strArr.add(onFailLog);
			strArr.add(driver);
			strArr.add(passScreenshot);
			strArr.add(currentIteration);
			strArr.add(errorValue);
			strArr.add(browserFolder);
			method.invoke(obj, strArr.toArray());

		}catch(Exception e){
			e.printStackTrace();
			//Setting the error flag to true indicating an exception has occurred
			error.put(scenario+"_"+testCase, true);
			
			if((!objectName.equalsIgnoreCase("")) || (!objectName.equalsIgnoreCase(null)))
				reportObject.Log("Unhandled Exception while performing action on Object "+objectName, "There was an Unhandled Exception while executing the keyword "+keyword+". Exception: "+e, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			else
				reportObject.Log("Unhandled Exception", "There was an Unhandled Exception while executing the keyword "+keyword+". Exception: "+e, Status.FAIL, driver, testCase, scenario, browser, passScreenshot, browserFolder);
			//Executing the method in case of exception in a keyword so that next test case can start fresh
			//methodName = "Logout";
			//ExecuteMethod(homePath, testCase, scenario, browser, objectId, objectName, dataValue, onPassLog, onFailLog, driver, passScreenshot, currentIteration, error.get(scenario+"_"+testCase), browserFolder);
			
		}
	}

	/**
	 * Author: Madhulika Mitra
	 * Method Name: CreateDateFolder
	 * Description: This method is to create the Execution folder with Current Date
	 * Return Type: String
	 */
	public String CreateDateFolder(String homePath){
		Util utilObject = new Util();
		Report reportObject = new Report();
		File f = new File(homePath+"\\Results");
		if(!f.exists()){
			f.mkdirs();
		}
		
		String date="Run_"+utilObject.getCurrentDate();
		
      resultsFolder = homePath+"\\Results\\"+date;
	f= new File(resultsFolder);
	if(!f.exists()){
		f.mkdir();
	}
		
		return resultsFolder;
	}
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: CreateExecutionFolder
	 * Description: This method is to create the Execution folder with Current Date and Time stamp
	 * Return Type: String
	 */
	public String CreateExecutionFolder(String homePath){
		Util utilObject = new Util();
		Report reportObject = new Report();
		File f;
		/*File f= new File(homePath+"\\Results");
		if(!f.exists()){
			f.mkdirs();
		}*/
		
		
		dateNTime = "Run_"+utilObject.getCurrentDate()+"_"+utilObject.getCurrentTime();
	
	
	//	resultsPath = homePath+"\\Results\\"+dateNTime;
		resultsPath = homePath+"\\"+dateNTime;
		f= new File(resultsPath);
		if(!f.exists()){
			f.mkdir();
		}
		reportObject.CreateSummaryFile(resultsPath);
		return resultsPath;
	}
	
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: CreateScenarioFolder
	 * Description: This method is to create the Scenario folder for the current executing scenario
	 * Return Type: String
	 */
	public String CreateScenarioFolder(String resultsPath, String scenario){
		
		File f = new File(resultsPath+"\\"+scenario);
		if(!f.exists()){
			f.mkdir();
		}
		return (resultsPath+"\\"+scenario);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: CreateBrowserFolder
	 * Description: This method is to create the current Browser folder within the scenario folder
	 * Return Type: String
	 */
	public String CreateBrowserFolder(String scenarioFolderPath, String browser){
		
		File f = new File(scenarioFolderPath+"\\"+browser);
		if(!f.exists()){
			f.mkdir();
		}
		return (scenarioFolderPath+"\\"+browser);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: SelectDriver
	 * Description: This method is to select the Type of Driver to be used for executing the script depending on the Browser and Grid Setup
	 * Return Type: WebDriver
	 */
	public WebDriver SelectDriver(String browser,String port){
	
		Util utilObject = new Util();
		WebDriver driver = null;
				
		try {
			//When current browser is Chrome 
			if(browser.equalsIgnoreCase("Chrome")){
				//When Grid Setup is Yes in Config.properties file
				if(utilObject.getValueFromConfigProperties("GridSetup").equalsIgnoreCase("Yes")){
					DesiredCapabilities caps = DesiredCapabilities.chrome();
					
					caps.setJavascriptEnabled(true);
				//	caps.setPlatform(Platform.WINDOWS);
					//caps.setCapability("nodeip", "192.168.0.124");
					
					
					if(port!=""){
					caps.setCapability("NodeName", port);
					}
				//	 caps.setCapability("port", 5558);
					// driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),caps);
					
					driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), caps);
				    driver.get(utilObject.getValueFromConfigProperties("URL"));
				}else {
					//When Grid Setup is No in Config.properties file
					System.setProperty("webdriver.chrome.driver", new File (".").getCanonicalPath()+"\\Jars\\chromedriver.exe");
					ChromeOptions options = new ChromeOptions();
					options.addArguments("test-type");
					driver = new ChromeDriver(options);
					driver.get(utilObject.getValueFromConfigProperties("URL"));
				}					
			}else if(browser.equalsIgnoreCase("IE")){
				DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		//		 caps.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				caps.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
				//	caps.setPlatform(Platform.LINUX);	
				caps.setCapability(InternetExplorerDriver.NATIVE_EVENTS,false);
		    	caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);

		    //	 caps.setCapability("port", 5558);
		   // 	caps.setCapability("nodeip", "192.168.0.124"); 
		    	
		    	if(port!=""){
		    	caps.setCapability("NodeName",port);
		    	}
		    	caps.setCapability("initialBrowserUrl",utilObject.getValueFromConfigProperties("URL"));
		    	
		    	caps.setJavascriptEnabled(true);
		    
				if(utilObject.getValueFromConfigProperties("GridSetup").equalsIgnoreCase("Yes")){
				
					//When Grid Setup is Yes in Config.properties file
				
					driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),caps);
				//	driver = new RemoteWebDriver(new URL("http://localhost:".concat(port).concat("/wd/hub")), caps);
					
			//		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
				}else {
					//When Grid Setup is No in Config.properties file
					System.setProperty("webdriver.ie.driver", new File (".").getCanonicalPath()+"\\Jars\\IEDriverServer.exe");
					
					driver = new InternetExplorerDriver(caps);
				//	driver.manage().deleteAllCookies();
					driver.get(utilObject.getValueFromConfigProperties("URL"));
				
				//	driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
				}						
			}else if(browser.equalsIgnoreCase("Firefox")){
				if(utilObject.getValueFromConfigProperties("GridSetup").equalsIgnoreCase("Yes")){
					//When Grid Setup is Yes in Config.properties file
					
					DesiredCapabilities caps = DesiredCapabilities.firefox();
					
					caps.setJavascriptEnabled(true);
				//	caps.setPlatform(Platform.WINDOWS);
					//caps.setCapability("nodeip", "192.168.0.124");
					if(port!=""){
					caps.setCapability("NodeName", port);
					}
				//	 caps.setCapability("port", 5558);
					// driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),caps);
					driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), caps);
				//	driver = new RemoteWebDriver(new URL("http://localhost:".concat(port).concat("/wd/hub")), caps);
					driver.manage().deleteAllCookies();
					
				    driver.get(utilObject.getValueFromConfigProperties("URL"));
				    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				}else {
					//When Grid Setup is No in Config.properties file
					driver = new FirefoxDriver();
					driver.get(utilObject.getValueFromConfigProperties("URL"));
					//driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				}
									
			}else if(utilObject.getValueFromConfigProperties("Browser").equalsIgnoreCase("Safari")){
				driver = new SafariDriver();
				driver.get(utilObject.getValueFromConfigProperties("URL"));
			}
									
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver;
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: calculateTotalNumberOfTestCases
	 * Description: This method is to calculate the total number of test cases across all modules and browsers
	 * Return Type: Nothing
	 */
	public void calculateTotalNumberOfTestCases(int browserTotalTCCount){
		totalTestCases = totalTestCases + browserTotalTCCount;
	}
			
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: calculateTotalNumberOfExecutedTestCases
	 * Description: This method is to calculate the total number of test cases executed (marked Yes) across all modules and browsers
	 * Return Type: Nothing
	 */
	public void calculateTotalNumberOfExecutedTestCases(int browserExecutedTCCount){
		totalTestCasesExecuted = totalTestCasesExecuted + browserExecutedTCCount;
	}
	public void eos(){
		
	}
}