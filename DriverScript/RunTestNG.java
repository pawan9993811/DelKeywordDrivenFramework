package DriverScript;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;

public class RunTestNG {

	public static void main(String args[]){
		
		try{
	TestNG runner=new TestNG();
	List<String> suitefiles=new ArrayList<String>();
	String path=new File (".").getCanonicalPath();

	suitefiles.add(path+"\\testNg.xml");
	runner.setTestSuites(suitefiles);
	runner.run();
		}
		catch(Exception e){
			
		}
	}
}