package com.naz;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

/**
 * Test Given Search value in Google and assert Right hand side Title Values. 
 * 
 * 1. input = Junit Test, expected = Unit testing, status = pass
 * 2. input = Womens day, expected = Woman's Day Fail Test For report, status = fail
 * 3. input = Fathers Day, expected = Father, status = pass
 * 
 * And Generate Report using Extent 
 * 1. Start of test log
 * 2. capture of Screenshot after search in google
 * 3. Status of Test log using test watcher.
 * 
 * Sample test report are attached under src/test/resources for reference.
 * 
 * @author Nazneen
 *
 */
@RunWith(Parameterized.class)
public class TestGoogleSearch {

	private static WebDriver DRIVER;

	private static ExtentReports EXTENT;

	private static final String userPath = System.getProperty("user.dir") + "/test-output/";

	private static final String screenShotPath = System.getProperty("user.dir") + "/test-output/ScreenShot/";

	@Parameters(name = "{0}")
	public static Collection<Object[]> input() {
		// List of input passed as params to test
		return Arrays.asList(new Object[][] { { "Junit Test", "Unit testing" },
				{ "Womens day", "Woman's Day Fail Test For report" }, { "Fathers Day", "Father" } });
	}
	
	private ExtentTest testReport;

	@Parameter(0)
	public String searchString;

	@Parameter(1)
	public String searchResult;
	/**
	 * Rules to Watch the status to test to log into report.
	 */
	@Rule
	public TestWatcher testWatcher = new TestWatcher() {
		protected void failed(Throwable e, org.junit.runner.Description description) {
			testReport.fail("Test Failed for Google Search:" + searchString);
			// Adding Exception to Node to render as collapsable.
			testReport.createNode(searchString + " - Failed Stack trace").fail(e);
		};

		protected void succeeded(org.junit.runner.Description description) {
			testReport.pass("Test succeeded for Google Search:" + searchString);
		};
	};

	/**
	 * Set Up class. Initialize report and clean report directory. And Initialize
	 * chrome webdrivers.
	 * 
	 * @throws IOException
	 */
	@BeforeClass
	public static void setUpClass() throws IOException {
		// Clean report directory
		File reportPath = new File(userPath);
		if (!reportPath.exists()) {
			reportPath.mkdir();
		} else {
			FileUtils.cleanDirectory(reportPath);
		}

		// Setting up Driver
		System.setProperty("webdriver.chrome.driver",
				"C://Users//Riyas//Downloads//chromedriver_win32//chromedriver.exe");
		DRIVER = new ChromeDriver();

		// Setting up report
		EXTENT = new ExtentReports();
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(userPath + "GoogleExtentReport.html");
		EXTENT.attachReporter(htmlReporter);
	}

	/**
	 * Create Extent test based on Search string.
	 */
	@Before
	public void setUp() {
		testReport = EXTENT.createTest(searchString);
		testReport.info("Test Started for Google Search:" + searchString);
	}

	/**
	 * Wanted failed Womens day search for report showing.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_google_search() throws IOException {
		// Given
		DRIVER.get("https://www.google.com/");

		// When
		DRIVER.findElement(By.id("lst-ib")).sendKeys(searchString);
		DRIVER.findElement(By.id("lst-ib")).sendKeys(Keys.RETURN);

		// Then
		takeScreenShot();
		Assert.assertNotNull(DRIVER.findElement(By.id("resultStats")));
		Assert.assertEquals("Title Not Matching", searchString + " - Google Search", DRIVER.getTitle());
		List<WebElement> elements = DRIVER.findElements(By.xpath("//div[@id='rhs_title']//span"));
		boolean rhsTitleMatched = false;
		for (WebElement webElement : elements) {
			if (searchResult.equals(webElement.getText())) {
				rhsTitleMatched = true;
				break;
			}
		}
		Assert.assertTrue("RHS Title is not Matching", rhsTitleMatched);
	}

	/**
	 * Capture screens shot and add it to report.
	 * 
	 * @throws IOException
	 */
	private void takeScreenShot() throws IOException {
		String fileName = searchString + System.currentTimeMillis() + ".png";
		File scrFile = ((TakesScreenshot) DRIVER).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(screenShotPath + fileName));
		testReport.info("ScreenShot Captured for Google Search:" + searchString + "<br>",
				MediaEntityBuilder.createScreenCaptureFromPath("./ScreenShot/" + fileName).build());

	}

	/**
	 * Quit driver and Flush all test logs into report.
	 */
	@AfterClass
	public static void tearUpClass() {
		if (null != DRIVER)
			DRIVER.quit();
		if (null != EXTENT)
			EXTENT.flush();
	}

}
