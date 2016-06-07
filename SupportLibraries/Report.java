package SupportLibraries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;



//import ReusableFunctions.DefectImageUtil;
//import ReusableFunctions.DefectImageUtil;
import ReusableFunctions.DefectLogger;



public class Report {
	public String summaryHTML = "";
	public int failInc=1;
	public String testCaseHTML = "";
	public String screenshotPassPath = "";
	public String screenshotFailPath = "";
	public String customScreenshotPath = "";
	static HashMap<String, Integer> testCaseStepNo = new HashMap<String, Integer>();
	static HashMap<String, Integer> TotalTestCasesPassed = new HashMap<String, Integer>();
	static HashMap<String, Integer> TotalTestCasesFailed = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleTCPassCount = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleTCFailCount = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleTCpassInc = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleTCfailInc = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleTChighlightInc = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleTCcustomInc = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleBrowserPassCount = new HashMap<String, Integer>();
	static HashMap<String, Integer> ModuleBrowserFailCount = new HashMap<String, Integer>();
	static Map<String,Map<String,String>> testCaseData= new HashMap<String,Map<String,String>>();
	public enum Status  {PASS,FAIL,DONE,done, pass, highlight};
	static LinkedHashSet<String> scenarios= new LinkedHashSet<String>();
	static List<String> scenarioScript= new ArrayList<String>();
	public HSSFWorkbook workbook = new HSSFWorkbook();
	  public HSSFSheet sheet; 
		static String matrixSummaryHtml="";
		private static final Logger logger = Logger.getLogger(Report.class.getName());
		
		private static List<String> scenarioDefectList = new ArrayList<String>();
		private static Map<Integer, List<Map<String, String>>> testcaseDefectList = new HashMap<Integer, List<Map<String, String>>>();
		private static String defectLogFolder="";
	
	  /**
	      * Author: Santosh Kumar Anupati
	      * Method Name: CreateSummaryFile
	      * Description: This method is to create the Overall Summary HTML file and create the headers in the file
	      * Return Type: Nothing
	      */
	      public void CreateSummaryFile(String resultsFolder){
	            Util utilObject = new Util();
	            summaryHTML = resultsFolder+"\\Summary.htm";
	            File f = new File(summaryHTML);
	            if(!f.exists()){
	                  try {
	                        f.createNewFile();
	                  } catch (Exception e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                  }
	            
	                  BufferedWriter bw;
	                  try {
	                        bw = new BufferedWriter(new FileWriter(f));
	                        
	                        if(ExecuteScripts.executionType.equalsIgnoreCase("Parallel")){
	                        	 bw.write("<html><head><title>Execution Summary</title></head><body><center><font size=5><b>Test Execution Summary</b><br>" +
		                                    "<table border=1 width=1200><tr><td bgcolor=#F8F8FF align = left width = 500><font color = black size=3><b>"+"Project:</b> "+utilObject.getValueFromConfigProperties("ProjectName")+"</td>" +
		                                    "<td bgcolor=#F8F8FF align = right width = 500><font color = black size=3><b>Release: </b>"+utilObject.getValueFromConfigProperties("Release")+"</td></tr></table>" +
		                                    "<table border=1 width=1200><tr bgcolor=white><font size=4 color=black><b>"+
		                                   "<table border=0 width=1200><tr></table><br><table border=1 width=1200><tr bgcolor=black><td colspan=7><font size=5 face=candara color=black><b>");
		             
		                        bw.write("<tr><td bgcolor=#8B4513 width=350><b><font size =2 color=white face = verdana><center>Business Module</td>" +
		                        "<td bgcolor=#8B4513 width=300><b><font color=white size =2 face = verdana><center>Browser</td>" +
		                        "<td bgcolor=#8B4513 width=300><b><font color=white size =2 face = verdana><center>VM Name</td>" +
		                        "<td bgcolor=#8B4513 width=500><b><font color=white size =2 face = verdana><center>Test Cases Executed</td>" +
		                        "<td bgcolor=#8B4513 width=500><b><font color=white size =2 face = verdana><center>Test Cases Passed</td>" +
		                        "<td bgcolor=#8B4513 width=500><b><font color=white size =2 face = verdana><center>Test Cases Failed</td>" +
		                        "<td bgcolor=#8B4513 width=200><b><font color=white size =2 face = verdana><center>Execution Time</td><tr>");
	                        }else{
	                        	bw.write("<html><head><title>Execution Summary</title></head><body><center><font size=5><b>Test Execution Summary</b><br>" +
	                					"<font size=4><div style=\"float:right;margin-right:165px;\"><b>Execution Performed By:</b> "+System.getProperty("user.name")+"</div><br><br>" +
	                					"<table border=1 width=1000><tr><td bgcolor=#F8F8FF align = left width = 500><font color = black size=3><b>"+"Project:</b> "+utilObject.getValueFromConfigProperties("ProjectName")+"</td>" +
	                					"<td bgcolor=#F8F8FF align = right width = 500><font color = black size=3><b>Release: </b>"+utilObject.getValueFromConfigProperties("Release")+"</td></tr></table>" +
	                					"<table border=1 width=1000><tr bgcolor=white><font size=4 color=black><b>"+
	                			 		"<table border=0 width=1000><tr></table><br><table border=1 width=1000><tr bgcolor=black><td colspan=5><font size=5 face=candara color=black><b>");
	                       
	                			bw.write("<tr><td bgcolor=#8B4513 width=350><b><font size =2 color=white face = verdana><center>Business Module</td>" +
	                			"<td bgcolor=#8B4513 width=300><b><font color=white size =2 face = verdana><center>Browser</td>" +
	                			"<td bgcolor=#8B4513 width=500><b><font color=white size =2 face = verdana><center>Test Cases Executed</td>" +
	                       		"<td bgcolor=#8B4513 width=500><b><font color=white size =2 face = verdana><center>Test Cases Passed</td>" +
	                       		"<td bgcolor=#8B4513 width=500><b><font color=white size =2 face = verdana><center>Test Cases Failed</td>" +
	                       		"<td bgcolor=#8B4513 width=200><b><font color=white size =2 face = verdana><center>Execution Time</td><tr>");
	                        }
	                       
	                   
	                  bw.close();
	                  }catch(Exception e){
	                        e.printStackTrace();
	                  }
	                  TotalTestCasesPassed.put("dummy",0);
	                  TotalTestCasesFailed.put("dummy",0);
	            }
	      }

	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: AddRowToSummary
	 * Description: This method is to add a row to the Summary HTML file at the end of execution of a particular scenario
	 * Return Type: Nothing
	 */
       public void AddRowToSummary(String scenario, String browser, String summaryHTML, Long totalDuration, String dateNTime, String vmName){
               
         //Adding the rows to the Summary HTML file with details of the Scenario that is finished executing
         try {
               File f = new File(summaryHTML);
         
               FileWriter bw;
               try {
                     bw = new FileWriter(f,true);
                     String text = "";
                     if(ExecuteScripts.executionType.equalsIgnoreCase("Parallel")){
                           text = "<tr><td bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center>"+"<a target=blank href=..\\"+dateNTime+"\\"+scenario+"\\"+browser+"\\"+scenario+"_"+browser+"_Summary.htm"+">"+scenario+"</a></td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+browser+"</td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+vmName+"</td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+Integer.toString(ExecuteScripts.ModuleTestCasesExecuted.get(scenario))+"</td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+Integer.toString(ModuleBrowserPassCount.get(scenario+"_"+browser))+"</td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+Integer.toString(ModuleBrowserFailCount.get(scenario+"_"+browser))+"</td>" +
                                       "<td bgcolor=#F8F8FF width=200><font size =2 color=black face = verdana><center>"+returnTime(totalDuration)+"</td>";
                     }else{
                           text = "<tr><td bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center>"+"<a target=blank href=..\\"+dateNTime+"\\"+scenario+"\\"+browser+"\\"+scenario+"_"+browser+"_Summary.htm"+">"+scenario+"</a></td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+browser+"</td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+Integer.toString(ExecuteScripts.ModuleTestCasesExecuted.get(scenario))+"</td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+Integer.toString(ModuleBrowserPassCount.get(scenario+"_"+browser))+"</td>" +
                                       "<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+Integer.toString(ModuleBrowserFailCount.get(scenario+"_"+browser))+"</td>" +
                                       "<td bgcolor=#F8F8FF width=200><font size =2 color=black face = verdana><center>"+returnTime(totalDuration)+"</td>";
                     }
                     
                     bw.write(text);
                                   
                     bw.close();
                     ExecuteScripts.overallDuration = ExecuteScripts.overallDuration + totalDuration;
               } catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
               }
         }catch(Exception e){
               e.printStackTrace();
         }
         
   }

	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: CloseSummary
	 * Description: This method is to add a row at the end of the Summary HTML file at the end of entire execution
	 * Return Type: Nothing
	 */
	public void CloseSummary(String resultsFolder){
		defectLogFolder=resultsFolder;
		System.out.println("The defect folder "+ defectLogFolder);
		Report.logDefects(defectLogFolder);
		//Adding the rows to the Summary HTML file with details of the Scenario that is finished executing
		try {
			File f = new File(resultsFolder+"\\Summary.htm");
			if(f.exists()){
				FileWriter bw;
				try {
					bw = new FileWriter(f,true);
					String text = "";
					if(ExecuteScripts.executionType.equalsIgnoreCase("Parallel")){
						text = "</table><br><table border=1 width=1200><tr bgcolor=black><td colspan=5><font size=5 face=candara color=black><b><tr><td bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center>Total Test Cases: <b>"+Integer.toString(ExecuteScripts.totalTestCases)+"<td bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center>Total Test Cases Executed: <b>"+Integer.toString(ExecuteScripts.totalTestCasesExecuted)+"</b></td>" +
								"<td bgcolor=#F8F8FF width=500><font size =2 face = verdana font color=green><center>Total Passed: <b>"+Integer.toString(TotalTestCasesPassed.get("dummy"))+"</b></td>" +
								"<td bgcolor=#F8F8FF width=500><font size =2 face = verdana font color=red><center>Total Failed: <b>"+Integer.toString(TotalTestCasesFailed.get("dummy"))+"</b></td>" +
								"<td bgcolor=#F8F8FF width=200><font size =2 color=black face = verdana><center><b>"+returnTime(ExecuteScripts.overallDuration)+"</b></td>";
					}else{
						text = "</table><br><table border=1 width=1000><tr bgcolor=black><td colspan=5><font size=5 face=candara color=black><b><tr><td bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center>Total Test Cases: <b>"+Integer.toString(ExecuteScripts.totalTestCases)+"<td bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center>Total Test Cases Executed: <b>"+Integer.toString(ExecuteScripts.totalTestCasesExecuted)+"</b></td>" +
								"<td bgcolor=#F8F8FF width=500><font size =2 face = verdana font color=green><center>Total Passed: <b>"+Integer.toString(TotalTestCasesPassed.get("dummy"))+"</b></td>" +
								"<td bgcolor=#F8F8FF width=500><font size =2 face = verdana font color=red><center>Total Failed: <b>"+Integer.toString(TotalTestCasesFailed.get("dummy"))+"</b></td>" +
								"<td bgcolor=#F8F8FF width=200><font size =2 color=black face = verdana><center><b>"+returnTime(ExecuteScripts.overallDuration)+"</b></td>";
					}
					
					bw.write(text);
					
				//	String[] url=matrixSummaryHtml.split("\\\\");								
					
					StringBuffer matrixFileLink = new StringBuffer();
					matrixFileLink.append("</table><br>");
					matrixFileLink.append("<table border=1 width=1000><tr bgcolor=black><td colspan=4><font size=5 face=candara color=black><b></td></tr>");
				//	matrixFileLink.append("<tr><td colspan=4 bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center><a target=blank href=../"+url[url.length-2]+"/"+url[url.length-1]+">Click Here to view the Matrix Report</a>");
					if(areThereAnyDefectsToLog()){
						//matrixFileLink.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						matrixFileLink.append("<tr><td colspan=4 bgcolor=#F8F8FF width=350><font size =2 color=black face = verdana><center><a href='");
						matrixFileLink.append("."+File.separatorChar +DefectLogger.getDefectLogFileName(defectLogFolder));
						matrixFileLink.append("'>Download defect log</a>");
						matrixFileLink.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						matrixFileLink.append("<a href='");
					//	matrixFileLink.append("."+File.separatorChar +DefectImageUtil.getZipFileName(defectLogFolder));
						matrixFileLink.append("'>Download defect screenshots</a>");	
				//	}
					matrixFileLink.append("</td></tr>");
					matrixFileLink.append("</table>");
					bw.write(matrixFileLink.toString());
					bw.close();
			   	  
					bw.close();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: CreateTestCaseHTML
	 * Description: This method is to create the TestCase HTML file and create the headers in the file
	 * Return Type: String
	 */
	public String CreateTestCaseHTML(String browserFolder, String testcase, String scenario){
		ModuleTCPassCount.put(scenario+"_"+testcase,0);
		ModuleTCFailCount.put(scenario+"_"+testcase,0);
		testCaseStepNo.put(scenario+"_"+testcase, 1);
		
		Util utilObject = new Util();
		testCaseHTML = browserFolder+"\\"+testcase+".htm";
		File f = new File(testCaseHTML);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedWriter bw;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy");
		try {
			bw = new BufferedWriter(new FileWriter(f));
			bw.write("<!DOCTYPE html PUBLIC "+"\""+"-//W3C//DTD XHTML 1.0 Transitional//EN"+"\" \""+ "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"+"\">\n");
			bw.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
			bw.write("<html><head><title>Test Execution Summary</title>\n");
			bw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\""+ "/>\n");
			bw.write("<style type=\"text/css\" media=\"screen\""+">\n");
			bw.write("@import \"../../../SupportLibraries/filtergrid.css\""+";\n");
			bw.write("</style>\n");
			bw.write("<script language=\"javascript\" type=\"text/javascript\" src=\"../../../SupportLibraries/tablefilter.js\"></script>\n");
			
			bw.write("</head><body><center><font size=4><b>Test Case Execution Results</b><br>" +
					"<br><center><table border=0 width=1000><tr></table>"+
					"<table border=1 width=1000><tr><td bgcolor=#F8F8FF align = left width = 500><font color = black size=3><b>"+"Project:</b> "+utilObject.getValueFromConfigProperties("ProjectName")+"</td>" +
					"<td bgcolor=#F8F8FF align = right width = 500><font color = black size=3><b>Release:</b> "+utilObject.getValueFromConfigProperties("Release")+"</td></tr></table>" +
					"<table border=1 width=1000><tr bgcolor=white><font size=4 color=black><b>"+
					"<table border=1 width=1000><tr><td bgcolor=#8B4513 align = left width = 800><font color = white size=4><b>"+"Test Case: "+testcase+"</b></td>" +
					"<td bgcolor=#8B4513 align = right width = 200><font color = white size=4><b>"+sdf.format(cal.getTime()).toString()+"</b></td></tr></table>" +
					"<br><table border=1 width=1000><tr bgcolor=white><font size=4 face=candara color=black><b>");
       
			bw.write("<tr><td bgcolor=#8B4513 width=80><b><font size =2 color=white face = verdana><center>Step No</td>" +
					"<td bgcolor=#8B4513 width=800><b><font size =2 color=white face = verdana><center>Step Name</td>" +
       		"<td bgcolor=#8B4513 width=580><b><font size =2 color=white face = verdana><center>Description</td>" +
       		"<td bgcolor=#8B4513 width=60><b><font size =2 color=white face = verdana><center>Step Status</td>" +
       		"<td bgcolor=#8B4513 width=120><b><font size =2 color=white face = verdana><center>Time of Execution</td><tr>");
              
           	bw.close();
		}catch(Exception e){
			
		}
		ModuleTCpassInc.put(scenario+"_"+testcase, 1);
		ModuleTCfailInc.put(scenario+"_"+testcase, 1);
		ModuleTChighlightInc.put(scenario+"_"+testcase, 1);
		ModuleTCcustomInc.put(scenario+"_"+testcase, 1);
		return testCaseHTML;
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: Log
	 * Description: This method is to write a step to the HTML result file
	 * Return Type: Nothing 
	 */
	public void Log(String stepName, String stepDesc, Status status, WebDriver driver, String testCase, String scenario, String browser, String passScreenshot, String browserFolder){
		Util utilObject = new Util();
		String testCaseHTML = browserFolder + "\\" + testCase+".htm";
		
		String screenshotsFolder = "";
		
	
		try {
			File f = new File(testCaseHTML);
		
			FileWriter bw;
			try {
				bw = new FileWriter(f,true);
				
				String text = "<tr><td bgcolor=#F8F8FF width=100><b><font size =2 color=black face = verdana><center>"+(testCaseStepNo.get(scenario+"_"+testCase))+"</td>" +
						"<td word-wrap: break-word; bgcolor=#F8F8FF width=300><b><font size =2 color=black face = verdana><center>"+stepName+"</td>"+
						"<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+stepDesc+"</td>";
				
				testCaseStepNo.put(scenario+"_"+testCase,(testCaseStepNo.get(scenario+"_"+testCase)+1));
				
				if(status.equals(Status.PASS)){
					
					ModuleTCPassCount.put(scenario+"_"+testCase, (ModuleTCPassCount.get(scenario+"_"+testCase))+1);
					//pass++;
					text = text+"<td bgcolor=#F8F8FF width=100><font size =2 color=green face = verdana><b><center>PASS</td>";
				}else if(status.equals(Status.pass)){
					//pass++;
					
					ModuleTCPassCount.put(scenario+"_"+testCase, (ModuleTCPassCount.get(scenario+"_"+testCase))+1);
					screenshotsFolder = createScreenshotFolder(browserFolder);
					createScreenshotPassFolder(screenshotsFolder);
					if(utilObject.getValueFromConfigProperties("SnapshotForAllPass").equalsIgnoreCase("Yes")){
						utilObject.CaptureScreenshot(screenshotPassPath+"\\"+testCase+"_"+ModuleTCpassInc.get(scenario+"_"+testCase)+".png", driver);
						text = text+"<td bgcolor=#F8F8FF width=100><b><center><a href=../../"+scenario+"/"+browser+"/Screenshots/Passed/"+testCase+"_"+ModuleTCpassInc.get(scenario+"_"+testCase)+".png"+"><font size =2 color=green face = verdana>PASS</a></td>";
						ModuleTCpassInc.put(scenario+"_"+testCase, (ModuleTCpassInc.get(scenario+"_"+testCase)+1));
					}else if(utilObject.getValueFromConfigProperties("SnapshotForAllPass").equalsIgnoreCase("No")){
						if(passScreenshot.equalsIgnoreCase("Yes")){
							utilObject.CaptureScreenshot(screenshotPassPath+"\\"+testCase+"_"+ModuleTCpassInc.get(scenario+"_"+testCase)+".png", driver);
							text = text+"<td bgcolor=#F8F8FF width=100><b><center><a href=../../"+scenario+"/"+browser+"/Screenshots/Passed/"+testCase+"_"+ModuleTCpassInc.get(scenario+"_"+testCase)+".png"+"><font size =2 color=green face = verdana>PASS</a></td>";
							ModuleTCpassInc.put(scenario+"_"+testCase, (ModuleTCpassInc.get(scenario+"_"+testCase)+1));
						}else{
							text = text+"<td bgcolor=#F8F8FF width=100><font size =2 color=green face = verdana><b><center>PASS</td>";
						}
					}
					
				}else if(status.equals(Status.FAIL)){
					ModuleTCFailCount.put(scenario+"_"+testCase, (ModuleTCFailCount.get(scenario+"_"+testCase))+1);
					screenshotsFolder = createScreenshotFolder(browserFolder);
					createScreenshotFailFolder(screenshotsFolder);
					captureDefect(scenario, testCase, testCaseStepNo+"~"+stepName, stepDesc, testCase+"_"+failInc+".png");
					utilObject.CaptureScreenshot(screenshotFailPath+"\\"+testCase+"_"+ModuleTCfailInc.get(scenario+"_"+testCase)+".png", driver);
					text = text+"<td bgcolor=#F8F8FF width=100><b><center><a href=../../"+scenario+"/"+browser +"/Screenshots/Failed/"+testCase+"_"+ModuleTCfailInc.get(scenario+"_"+testCase)+".png"+"><font size =2 color=red face = verdana>FAIL</a></td>";
					ModuleTCfailInc.put(scenario+"_"+testCase, (ModuleTCfailInc.get(scenario+"_"+testCase)+1));
				}else if(status.equals(Status.DONE)){
					text = text+"<td bgcolor=#F8F8FF width=100><font  size =2 color=black face = verdana><b><center>DONE</td>";
				}else if(status.equals(Status.done)){
					screenshotsFolder = createScreenshotFolder(browserFolder);
					createCustomScreenshotFolder(screenshotsFolder);
					utilObject.CaptureScreenshot(customScreenshotPath+"\\"+testCase+"_Custom_"+ModuleTCcustomInc.get(scenario+"_"+testCase)+".png", driver);
					text = text+"<td bgcolor=#F8F8FF width=100><b><center><a href=../../"+scenario+"/"+browser +"/Screenshots/Custom/"+testCase+"_Custom_"+ModuleTCcustomInc.get(scenario+"_"+testCase)+".png"+"><font size =2 color=black face = verdana>DONE</a></td>";
					ModuleTCcustomInc.put(scenario+"_"+testCase, (ModuleTCcustomInc.get(scenario+"_"+testCase)+1));
				}else if(status.equals(Status.highlight)){
					screenshotsFolder = createScreenshotFolder(browserFolder);
					createCustomScreenshotFolder(screenshotsFolder);
					utilObject.CaptureScreenshot(customScreenshotPath+"\\"+testCase+"_Highlight_"+ModuleTChighlightInc.get(scenario+"_"+testCase)+".png", driver);
					text = text+"<td bgcolor=#F8F8FF width=100><b><center><a href=../../"+scenario+"/"+browser +"/Screenshots/Custom/"+testCase+"_Highlight_"+ModuleTChighlightInc.get(scenario+"_"+testCase)+".png"+"><font size =2 color=brown face = verdana>DONE</a></td>";
					ModuleTChighlightInc.put(scenario+"_"+testCase, (ModuleTChighlightInc.get(scenario+"_"+testCase)+1));
				}
				
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
				text = text+"<td bgcolor=#F8F8FF width=200><font size =2 color=black face = verdana><center>"+sdf.format(cal.getTime())+"</td>";
				bw.write(text);
		   	   
				bw.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: WriteIterationToHTML
	 * Description: This method is to write the current iteration to the HTML result file
	 * Return Type: Nothing 
	 */
	public void WriteIterationToHTML(String testCaseHTMLPath, int currentIteration, int startIteration, boolean error){
		
		File f = new File(testCaseHTMLPath);
		
		FileWriter bw;
		try {
			bw = new FileWriter(f,true);
			String text = "";
			if(currentIteration==startIteration){
				text = "</table><center><table border=1 width=1000><tr bgcolor=white><font size=4 face=verdana color=black><b>" +
						"<tr><td bgcolor=#F8F8FF width=1000><font size=3 face = verdana font color=black><center>Iteration: <b>"+Integer.toString(currentIteration)+"</b></td></tr></table>" +
						"<center><table id=Iteration"+currentIteration+" border=1 width=1000><tr bgcolor=white><font size=4 face=verdana color=black><b>";
			}else{
				text = "</table><br><center><table border=1 width=1000><tr bgcolor=white><font size=4 face=verdana color=black><b>" +
						"<tr><td bgcolor=#F8F8FF width=1000><font size=3 face = verdana font color=black><center>Iteration: <b>"+Integer.toString(currentIteration)+"</b></td></tr></table>" +
						"<center><table id=Iteration"+currentIteration+" border=1 width=1000><tr bgcolor=white><font size=4 face=verdana color=black><b>";
			}
			if(error==false)
				bw.write(text);
			bw.close();
		}catch(Exception e){
			
		}
	
	}
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: closeTestCaseHTML
	 * Description: This method is to add the Summary details of the Test Case at the end of the Test Case HTML result file
	 * Return Type: Nothing
	 */
	public void closeTestCaseHTML(String testCaseHTML, String testcase, String scenario, Long duration, int startIteration, int endIteration, String browser){
		
		File f = new File(testCaseHTML);
		
		FileWriter bw;
		try {
			bw = new FileWriter(f,true);
			
			String text = "</table><br><table border=1 width=1000><tr bgcolor=white><font size=4 face=verdana color=black><b>" +
					"<tr><td bgcolor=#F8F8FF width=300><font size=2 face = verdana font color=black><center>Total Steps: <b>"+Integer.toString((testCaseStepNo.get(scenario+"_"+testcase))-1)+"</b></td>" +
					"<td bgcolor=#F8F8FF width=300><font size=2 face = verdana font color=green><center>Steps Passed: <b>"+Integer.toString(ModuleTCPassCount.get(scenario+"_"+testcase))+"</b></td>"+
					"<td bgcolor=#F8F8FF width=300><font size=2 face = verdana font color=red><center>Steps Failed: <b>"+Integer.toString(ModuleTCFailCount.get(scenario+"_"+testcase))+"</b></td>"+
					"<td bgcolor=#F8F8FF width=300><font size=2 face = verdana font color=black><center>Execution Time: <b>"+returnTime(duration)+"</b></td></tr></table>";
			bw.write(text);
			String filterCode = "";
			for(int z=startIteration;z<=endIteration;z++){
				filterCode = "<script language=\"javascript\" type=\"text/javascript\""+">\n"+"//<![CDATA[\n"+"var tableFilters = {"+"col_0: \"select\",\ncol_3: \"select\",\n}\n"+"setFilterGrid(\"Iteration"+z+"\",0,tableFilters);\n"+"//]]>\n"+"</script>\n";
				bw.write(filterCode);
			}
			
			bw.close();
		}catch(Exception e){
			
		}
		testCaseStepNo.put(scenario+"_"+testcase, 1);
		if(ModuleTCFailCount.get(scenario+"_"+testcase)>0)
			ModuleBrowserFailCount.put(scenario+"_"+browser, (ModuleBrowserFailCount.get(scenario+"_"+browser))+1);
		else if(ModuleTCPassCount.get(scenario+"_"+testcase)>0 && ModuleTCFailCount.get(scenario+"_"+testcase)==0)
			ModuleBrowserPassCount.put(scenario+"_"+browser, (ModuleBrowserPassCount.get(scenario+"_"+browser))+1);
		ModuleTCPassCount.put((scenario+"_"+testcase),0);
		ModuleTCFailCount.put((scenario+"_"+testcase),0);
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: CreateScenarioHTML
	 * Description: This method is to create the Scenario HTML file and create the header in the file
	 * Return Type: String
	 */ 
	public String CreateScenarioHTML(String browserFolder, String scenario, String browser,String Node){
		Util utilObject = new Util();
		String scenarioBrowserHTML = "";
		scenarioBrowserHTML = browserFolder+"\\"+scenario+"_"+browser+"_Summary.htm";
		File f = new File(scenarioBrowserHTML);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(f,true));
				if(ExecuteScripts.executionType.equalsIgnoreCase("Standalone")){
					bw.write("<html><head><title>Scenario Execution Summary</title></head><body><center><font size=4><b>"+scenario+" Execution Summary<br><center>" +
							"<font size=4><div style=\"float:right;margin-right:165px;\"><b>Execution Performed by:</b> "+System.getProperty("user.name")+"</div><br><br>"+
							 		"<table border=0 width=1000><tr></table><br><table border=1 width=1000><tr bgcolor=white><font size=4 color=black><b>");
				}else{
					bw.write("<html><head><title>Scenario Execution Summary</title></head><body><center><font size=4><b>"+scenario+" Execution Summary<br><center>" +
							"<font size=4><div style=\"float:right;margin-right:165px;\"><b>Execution Performed on:</b> "+Node+"</div><br><br>"+
							 		"<table border=0 width=1000><tr></table><br><table border=1 width=1000><tr bgcolor=white><font size=4 color=black><b>");
				}
				bw.write("<table border=1 width=1000><tr><font size=4 color = black><td bgcolor=#F8F8FF align = left width = 500><b>"+"Project:</b> "+utilObject.getValueFromConfigProperties("ProjectName")+"</td>" +
						"<td bgcolor=#F8F8FF align = right width = 500><b>Release:</b> "+utilObject.getValueFromConfigProperties("Release")+"</td></tr></table>" +
						"<table border=1 width=1000><tr bgcolor=white><font size=4 color=black><b>");
				
				bw.write("<tr><td bgcolor=#8B4513 width=300><b><font size =2 color=white face = verdana><center>Test Case</td>" +
						"<td bgcolor=#8B4513 width=500><b><font size =2 color=white face = verdana><center>Description</td>" +
	       		"<td bgcolor=#8B4513 width=100><b><font size =2 color=white face = verdana><center>Status</td>" +
	       		"<td bgcolor=#8B4513 width=100><b><font size =2 color=white face = verdana><center>Execution Time</td><tr>");
	              
	    	    //bw.write("<tr><td width=300><font size=2 face=Verdana>");
	           	bw.close();
			
			}catch(Exception e){
				
			}
			ModuleBrowserPassCount.put(scenario+"_"+browser, 0);
			ModuleBrowserFailCount.put(scenario+"_"+browser, 0);
		}
		
		return scenarioBrowserHTML;
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: AddRowToScenarioHTML
	 * Description: This method is to add a row to the Scenario HTML file at the end of execution of each test case
	 * Return Type: Nothing
	 */
	public void AddRowToScenarioHTML(String scenarioHTML, String scenario, String testcase, String testCaseDesc, String browser, Long duration,String node){
		Report R1=new Report();
		String exe_status="";
		
		
			File f = new File(scenarioHTML);
		     
			FileWriter bw;
			
			try {
				
				bw = new FileWriter(f,true);
				String text="";
				text = "<tr><td bgcolor=#F8F8FF width=300><font size =2 color=black face = verdana><center>"+"<b><a target=blank href=../../"+scenario+"/"+browser+"/"+testcase+".htm>"+testcase+"</a></td>" +
						"<td bgcolor=#F8F8FF width=500><font size =2 color=black face = verdana><center>"+testCaseDesc+"</td>";
				
				if(ModuleTCFailCount.get(scenario+"_"+testcase)>0){
					exe_status="FAIL";
					
					text = text+"<td bgcolor=#F8F8FF width=100><b><font size =2 color=red face = verdana><center>"+"FAIL"+"</td>";}
				else if(ModuleTCPassCount.get(scenario+"_"+testcase)==0 && ModuleTCFailCount.get(scenario+"_"+testcase)==0){
					exe_status="DONE";
					text = text+"<td bgcolor=#F8F8FF width=100><b><font size =2 color=black face = verdana><center>"+"DONE"+"</td>";
				}
					
				else{
					exe_status="PASS";
					text = text+"<td bgcolor=#F8F8FF width=100><b><font size =2 color=green face = verdana><center>"+"PASS"+"</td>";
				}
				text = text+"<td bgcolor=#F8F8FF width=100><font size =2 color=black face = verdana><center>"+returnTime(duration)+"</td></tr>";
				bw.write(text);
			
				bw.close();
				}
				
				
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: closeScenarioHTML
	 * Description: This method is to Close the Scenario HTML file by adding the Execution summary of the scenario at the end of the file
	 * Return Type: Nothing
	 */
	public void closeScenarioHTML(String scenario, String browser, String scenarioBrowserHTML, Long totalDuration){
				
	TotalTestCasesPassed.put("dummy", (TotalTestCasesPassed.get("dummy"))+ModuleBrowserPassCount.get(scenario+"_"+browser));
		TotalTestCasesFailed.put("dummy", TotalTestCasesFailed.get("dummy")+ModuleBrowserFailCount.get(scenario+"_"+browser));
		
		File f = new File(scenarioBrowserHTML);
		
		FileWriter bw;
		try {
			bw = new FileWriter(f,true);
			
			String text = "</table><br><table border=1 width=1000><tr bgcolor=white><font size=4 face=verdana color=black><b>" +
					"<tr><td bgcolor=#F8F8FF width=200><font size=2 face = verdana font color=black><center>Total Test Cases: <b>"+ExecuteScripts.ModuleTotalTestCases.get(scenario).toString()+"</b></td>" +
					"<td bgcolor=#F8F8FF width=200><font size=2 face = verdana font color=black><center>Test Cases Executed: <b>"+ExecuteScripts.ModuleTestCasesExecuted.get(scenario).toString()+"</b></td>" +
					"<td bgcolor=#F8F8FF width=200><font size=2 face = verdana font color=green><center>Test Cases Passed: <b>"+ModuleBrowserPassCount.get(scenario+"_"+browser).toString()+"</b></td>"+
					"<td bgcolor=#F8F8FF width=200><font size=2 face = verdana font color=red><center>Test Cases Failed: <b>"+ModuleBrowserFailCount.get(scenario+"_"+browser).toString()+"</b></td>"+
					"<td bgcolor=#F8F8FF width=200><font size=2 face = verdana font color=black><center>Execution Time: <b>"+returnTime(totalDuration)+"</b></td></tr></table>";
			bw.write(text);
			bw.close();
		}catch(Exception e){
			
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: returnTime
	 * Description: This method is to return the Execution time of the test case in the String format 'hh: mm: ss'
	 * Return Type: String
	 */
	public static String returnTime(Long duration){
		
		    String format = String.format("%%0%dd", 2);  
		    duration = duration / 1000;
		    String seconds = String.format(format, duration % 60);  
		    String minutes = String.format(format, (duration % 3600) / 60);  
		    String hours = String.format(format, duration / 3600);  
		    String time =  hours + " : " + minutes + " : " + seconds;
		    return time;
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: createScreenshotFolder
	 * Description: This method is to create the Screenshot folder whenever there is a Failed step in a test case or a screenshot is to be captured for an important Passed
	 * 				step
	 * Return Type: String
	 */
	public String createScreenshotFolder(String browserFolder){
		
		String screenshotFolder = browserFolder+"\\"+"Screenshots";
		File f = new File(screenshotFolder);
		if(!f.exists()){
			f.mkdir();
		}
		return screenshotFolder;
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: createScreenshotPassFolder
	 * Description: This method is to create the 'Passed' folder inside the Screenshot folder whenever there is a screenshot captured for any important step that is PASS
	 * Return Type: Nothing
	 */
	public void createScreenshotPassFolder(String screenshotsFolder){
		
		screenshotPassPath =screenshotsFolder+"\\Passed";
		File f = new File(screenshotPassPath);
		if(!f.exists()){
			f.mkdir();
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: createScreenshotFailFolder
	 * Description: This method is to create the 'Failed' folder inside the Screenshot folder whenever there is a Failed step in a test case
	 * Return Type: Nothing
	 */
	public void createScreenshotFailFolder(String screenshotsFolder){
		
		screenshotFailPath = screenshotsFolder+"\\Failed";
		File f = new File(screenshotFailPath);
		if(!f.exists()){
			f.mkdir();
		}
	}
	
	/**
	 * Author: Santosh Kumar Anupati
	 * Method Name: createCustomScreenshotFolder
	 * Description: This method is to create the Screenshot folder whenever there is a Failed step in a test case or a screenshot is to be captured for an important Passed
	 * 				step
	 * Return Type: Nothing
	 */
	public void createCustomScreenshotFolder(String screenshotsFolder){
		customScreenshotPath = screenshotsFolder+"\\Custom";
		File f = new File(customScreenshotPath);
		if(!f.exists()){
			f.mkdir();
		}
	}
	
	
	/**
	 * Author: Madhulika Mitra
	 * Method Name: create a excel report
	 * Description: This method is to create a final excel report
	 * Return Type: Nothing
	 */
		
		public void excelUpdate(String testcase,String browser,String testCaseDesc,String node, String status ){
		
			//check if there exists a excel with a particular name(date and time stamp) 
			   //if yes open it
			   //else create it
			try{
			Util utilObject = new Util();
			Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			 FileOutputStream fileOut=null;

			 String homePath = new File (".").getCanonicalPath();
		     String date="Run_"+utilObject.getCurrentDate();
				
			String location = homePath+"\\Results\\"+date;
			String filename = location+"\\OverallDailyReport.xls" ;
		
		 try {
	        	
	            
	            File lFile = new File(filename);
	             if(!lFile.exists()){
	           
	            	HSSFWorkbook workbook = new HSSFWorkbook();
	            		   sheet = workbook.createSheet("FirstSheet"); 
	                
	            HSSFRow rowhead = sheet.createRow((short)0);
	            rowhead.createCell(0).setCellValue("Time of Run");
	            rowhead.createCell(1).setCellValue("Business Module");
	            rowhead.createCell(2).setCellValue("Description");
	            rowhead.createCell(3).setCellValue("Browser");
	            rowhead.createCell(4).setCellValue("Node");
	            rowhead.createCell(5).setCellValue("Test Status");
	            fileOut = new FileOutputStream(filename);
	            workbook.write(fileOut);
	            
	        //    System.out.println("Your excel file has been generated!");
	       	// fileOut.close();
	            }

	            //	System.out.println("Your excel file already exists!");
	            	FileInputStream file = new FileInputStream(new File(filename));
	            
	            	HSSFWorkbook workbook = new HSSFWorkbook(file);
	    
	    			HSSFSheet sheet = workbook.getSheet("FirstSheet");
	    			int rows=sheet.getPhysicalNumberOfRows();
	    		//	   System.out.println("the no of rows :"+ rows);
	    			            	
	    			               HSSFRow row = sheet.createRow((short)(rows));
	    			               row.createCell(0).setCellValue(currentTimestamp);
	    		//	System.out.println("Run");
	    			//               System.out.println(currentTimestamp);
	    				            row.createCell(1).setCellValue(testcase);
	    				//            System.out.println(testcase);
	    				            row.createCell(2).setCellValue(testCaseDesc);
	    			//System.out.println(testCaseDesc);
	    				            row.createCell(3).setCellValue(browser);
	    				//            System.out.println(browser);
	    				            row.createCell(4).setCellValue(node);
	    			//System.out.println(node);
	    				            row.createCell(5).setCellValue(status);
	    				//            System.out.println(status);
	    			          
	    			               fileOut = new FileOutputStream(filename);
	    			               workbook.write(fileOut);
	    			               
	    					 }
	    				            catch ( Exception ex ) {
	    				            	
	    				  //          System.out.println(ex);
	    				        }
	    					 fileOut.close();
	    				        }
	    					
	    					 catch ( Exception ex ) {
	    			         	
	    				    //        System.out.println(ex);
	    				        }

	    					 }
		private static void captureDefect(String scenarioName, String testCaseName, String stepName, String stepDesc, String screenShotName){
			String scenarioTestCase = scenarioName +"~"+testCaseName;
		
			int keyIndex = getKeyIndex(scenarioTestCase);
			addTestStepDetails(keyIndex, stepName, stepDesc, screenShotName);			
		}
		
		private static int getKeyIndex(String scenarioTestCase){
			int keyIndex = scenarioDefectList.indexOf(scenarioTestCase);
			
			if(keyIndex == -1){
				/*Means this scenario test case doesn't exists in the list */
				scenarioDefectList.add(scenarioTestCase);
			
				keyIndex = scenarioDefectList.size() - 1;
			}	
			return keyIndex;
		}
		
		private static void addTestStepDetails(int keyIndex, String stepName, String stepDesc, String screenShotName){
			List<Map<String, String>> testCase = testcaseDefectList.get(keyIndex);
			if(testCase == null){
				testCase = new ArrayList<Map<String, String>>();
			}
			
			Map<String, String> testStepDetails = getTestCaseStepDetails(stepName, stepDesc, screenShotName);
			testCase.add(testStepDetails);
			testcaseDefectList.put(keyIndex, testCase);
		}
		
		private static Map<String, String> getTestCaseStepDetails(String stepName, String stepDesc, String screenShotName){
			Map<String, String> testCaseStepDetails = new HashMap<String, String>();
			String[] noName = stepName.split("~");
			testCaseStepDetails.put("no", noName[0]);
			testCaseStepDetails.put("name", noName[1]);
			testCaseStepDetails.put("desc", stepDesc);
			testCaseStepDetails.put("img", screenShotName);
			return testCaseStepDetails;
		}
		
		public static void logDefects(String browserFolder){
			
		//	DefectLogger defectLogger = new DefectLogger(scenarioDefectList,testcaseDefectList, defectLogFolder);
			DefectLogger defectLogger = new DefectLogger(scenarioDefectList,testcaseDefectList, browserFolder);
			defectLogger.createLog();	
		//	DefectImageUtil imageUtil = new DefectImageUtil(defectLogFolder);
		//	imageUtil.createFailedImageZipFile();
		}
		
		private static boolean areThereAnyDefectsToLog(){
			if(scenarioDefectList != null && scenarioDefectList.size() > 0){
				return true;
			}
			return false;
		}
		
		public static String createRegressionSummaryFolder() throws IOException{
			String regSummaryfolder = Util.getRootPath() + File.separatorChar + "Results";
			String timeStamp = "Run_"+Util.getCurrentDate()+"_"+Util.getCurrentTime();
			regSummaryfolder = regSummaryfolder + File.separatorChar + timeStamp;
			File f= new File(regSummaryfolder);
			if(!f.exists()){
				f.mkdir();
			}
			
			defectLogFolder = regSummaryfolder;
		//	System.out.println("The defect folder in CRS" +defectLogFolder);
			return regSummaryfolder;
		}

	    			}
	    				
            	
    			



	     
	                
	                 
	            
	           
	            	
	    		
	  
	            	 
		 
	           
    

