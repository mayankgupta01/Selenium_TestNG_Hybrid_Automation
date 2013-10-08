package com.createTestNG;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.logs.Logging;

public class CreateTestNGXML {
	
	public static XmlSuite createTestNGXMLForCurrentSuite(String currentTestSuite, ArrayList<String> testCases){
		XmlSuite currentSuite = new XmlSuite();
		currentSuite.setName(currentTestSuite);
		XmlTest addTests = new XmlTest(currentSuite);
		XmlClass xcTest = null;
		addTests.setName(currentTestSuite + "Tests");
		
		List<XmlClass> addClasses = new ArrayList<XmlClass>();
		for( int i = 0; i < testCases.size(); i++){
			xcTest = new XmlClass("com.testcases." + currentTestSuite + "." + testCases.get(i));
			//addClasses.add(new XmlClass(System.getProperty("user.dir")+"\\src\\com\\testcases\\" + currentTestSuite + "\\" + testCases.get(i) + ".java"));
			addClasses.add(xcTest);
			
		}
		addTests.setXmlClasses(addClasses);
		return currentSuite;
	}
	
	public static void runTestNGXMLForAllSuites(ArrayList<XmlSuite> currentSuite) throws EmptyStackException{
		//List<XmlSuite> suites = new ArrayList<XmlSuite>();
		//suites.add(currentSuite);
		if(currentSuite == null){
			Logging.log("There are no test cases in testNG suite");
			throw new EmptyStackException();
		}
		else{
		TestNG tng = new TestNG();
		tng.setXmlSuites(currentSuite);
		tng.run();
		}
	}
}
