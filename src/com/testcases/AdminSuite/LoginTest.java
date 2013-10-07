package com.testcases.AdminSuite;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.config.CreatePropertiesObjects;
import com.generatexlsreport.CreateXLReport;
import com.generatexlsreport.CreateXLReportBeta;
import com.logs.Logging;
import com.readxls.ReadingDataSheetOfTestCase;
import com.readxls.ReadingTestStepsWithRunmode;
import com.readxls.ReadingTestSuiteXLWithRunmode;

public class LoginTest{
public String testName = "LoginTest";
public String currentTestSuite = "AdminSuite";
@BeforeClass
	public void setTestCaseDataSetNumberToZero(){
		CreateXLReport.testCaseDataSetNumber = 0;
		
	}
	
	@AfterClass
	public void setTestCaseDataSetNumberToZeroAfterExecution(){
		CreateXLReport.testCaseDataSetNumber = 0;
	}
	
	@Test(dataProvider="getData")
	public void doLoginTest(Hashtable<String,String> data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		if(!ReadingTestStepsWithRunmode.getRunModeOfTestCase(testName, ReadingTestSuiteXLWithRunmode.currentTestSuiteXL.get(currentTestSuite))){
			Logging.log(String.format("Skipping the Test Case - %s as RUNMODE in TestCases sheet in %s is N" , testName, currentTestSuite));
			throw new SkipException("Skipping the Test Case as RUNMODE in TestCases sheet is N");
		}

		if(data==null){
			Logging.log(String.format("Skipping the Test Case - %s as data sheet has no data for this testcase" , testName));
			throw new SkipException("Skipping the Test Case as data sheet has no data for this testcase");
		}
			
		else{
			CreateXLReport.testCaseDataSetNumber++;
			if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_RUNMODE")).equals("N")){
				Logging.log("Skipping the test as iteration " + CreateXLReport.testCaseDataSetNumber +" has runmode = N for TestCase = "+testName);
				CreateXLReport.insertResultSetInTestStepsAsSkipped(testName,currentTestSuite, ReadingTestSuiteXLWithRunmode.currentTestSuiteXL.get(currentTestSuite));
				throw new SkipException("Skipping the testset as data sheet has RUNMODE=N");
			}
			
			ReadingTestStepsWithRunmode.executeTestStepsSerially(testName,currentTestSuite, ReadingTestSuiteXLWithRunmode.currentTestSuiteXL.get(currentTestSuite), data);
			
	}
	}
	
	@DataProvider
	public Object[][] getData(){
		
		return ReadingDataSheetOfTestCase.getData(testName, ReadingTestSuiteXLWithRunmode.currentTestSuiteXL.get(currentTestSuite));
	}
	
		
}

