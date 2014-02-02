package com.listeners;

import java.util.List;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class GenerateReport implements IReporter {
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory){
	      ISuiteResult results =suites.get(0).getResults().get("AdminSuite");
	      ITestContext context = results.getTestContext();

	      IResultMap passedTests = context.getPassedTests();
	      IResultMap failedTests = context.getFailedTests();

	      // Print all test exceptions...
	      for( ITestResult r: failedTests.getAllResults()) {
	          System.out.println( r.getThrowable());
	      }
	}

}
