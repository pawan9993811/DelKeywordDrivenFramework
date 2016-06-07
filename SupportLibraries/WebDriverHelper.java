package SupportLibraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class WebDriverHelper {
	//The static WebDriver object
	public static WebDriver driver = null;
	//public static WebDriverBackedSelenium selenium = null;
	public static long timeout = 6;
	public static String mainWindowHandle = null;
	
	
	/**
	 * Method Name: SelectDriver
	 * Description: This method is to select the Type of Driver to be used for executing the script depending on the Browser given in Properties.INI file
	 */
	public static void SelectDriver(){
		
		try {
			if(Util.getValueFromConfigProperties("Browser").equalsIgnoreCase("Chrome")){
				System.setProperty("webdriver.chrome.driver", new File (".").getCanonicalPath()+"\\Jars\\chromedriver.exe");
				driver = new ChromeDriver();
			}else if(Util.getValueFromConfigProperties("Browser").equalsIgnoreCase("IE")){
				DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
				caps.setCapability(InternetExplorerDriver.NATIVE_EVENTS,false);
		    	caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		    	caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS,true);
		    	System.setProperty("webdriver.ie.driver", new File (".").getCanonicalPath()+"\\Jars\\IEDriverServer.exe");
				driver = new InternetExplorerDriver(caps);
			}else if(Util.getValueFromConfigProperties("Browser").equalsIgnoreCase("Firefox")){
				//final String firebugPath = "C:\\Users\\sanupati\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\jegii6p5.default\\extensions\\firebug@software.joehewitt.com.xpi";
				  // FirefoxProfile profile = new FirefoxProfile();
				   //profile.addExtension(new File(firebugPath));
				   // Add more if needed
				 //driver = new FirefoxDriver(profile);
				driver = new FirefoxDriver();
			}else if(Util.getValueFromConfigProperties("Browser").equalsIgnoreCase("Safari")){
				driver = new SafariDriver();
			}
				if(driver != null){
					mainWindowHandle = driver.getWindowHandle();
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public static void waitForXPath(String objectXpathID){
		try {
			int count=1;
			while(driver.findElements(By.xpath(objectXpathID)).size()==0){
				count++;
				Thread.sleep(3000);
				if(count==5)
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	/*
	public static void waitForLinkText(String linkText){
		try {
			int count=1;
			while(driver.findElements(By.linkText(linkText)).size()==0){
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
	*/
}
