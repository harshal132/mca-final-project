package com.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.pages.SearchProduct;


import io.github.bonigarcia.wdm.WebDriverManager;


public class ProductPageValidation {
	WebDriver driver;
	SearchProduct search;
	WebDriverWait wait;
	JavascriptExecutor js;
	ExtentReports reports;
	ExtentSparkReporter reporter;
	
  //TC_ProductPage_001
  @Test(dataProvider="productnavigation")
  public void ProductNavigation(String productName) {
	  ExtentTest productNavigationTest = reports.createTest("Product Navigation & Title Verification For "+productName);
	  driver.get("https://demo.opencart.com/");
	  js.executeScript("window.scrollBy(0,350)");
	  waitForSomeTime(4);
	  search.scrollAndOpenProduct(productName);
	  waitForSomeTime(2);
	  try {
		  Assert.assertEquals(driver.getTitle(),productName);
		  productNavigationTest.log(Status.PASS, "Verified Title for "+productName);
	  }
	  catch(AssertionError  e) {
		  System.out.println("Incorrect title found for "+productName);
		  productNavigationTest.fail("Incorrect Title displayed for product = "+productName);
	  }
  }
  
	private void waitForSomeTime(int i) {
	try {
		Thread.sleep(i*1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

//TC_ProductPage_002
  @Test(dataProvider="productnavigation")
  public void verifyDescription(String productName) throws InterruptedException
  {
	  ExtentTest verifyDescriptionTest = reports.createTest("Verify Product Description For "+productName);
	  driver.get("https://demo.opencart.com/");
	  js.executeScript("window.scrollBy(0,350)");
	  waitForSomeTime(4);
	  search.scrollAndOpenProduct(productName);
	  waitForSomeTime(2);
	  js.executeScript("window.scrollBy(0,350)");
	  String result=search.CheckDescriptionVisible();
	  if(!result.contains("active"))
	  {
		  try {
			  Assert.fail("Description Field is Not Active"); 
		  }
		  catch(AssertionError r) {
			  System.out.println("Product description not available for "+productName);
			  verifyDescriptionTest.fail("Product description not available for "+productName);
		  }
	  }
	  else {
		  verifyDescriptionTest.pass("Product description verified for "+productName);
	  }
	  
  }
  	
  @Test(dataProvider="productnavigation")
  public void verifySpecification(String productName) throws InterruptedException {
	  ExtentTest verifySpecificationTest = reports.createTest("Verify Product Specification For "+productName);
	  driver.get("https://demo.opencart.com/");
	  js.executeScript("window.scrollBy(0,350)");
	  waitForSomeTime(4);
	  search.scrollAndOpenProduct(productName);
	  waitForSomeTime(2);
	  js.executeScript("window.scrollBy(0,350)");
	  boolean result=search.CheckSpecsAvailable();
	  if(!result) {
		  verifySpecificationTest.fail("Product specifications not displayed for "+productName);
		  System.out.println("Product specifications not displayed for "+productName);
	  }
	  else {
		  verifySpecificationTest.pass("Product specifications displayed for "+productName);
	  }
  }
	
  	@Ignore
	@Test(dataProvider="productnavigation")
	public void VerifyReviews(String productName) throws InterruptedException
	{
		search.SearchProductFunc(productName);
		search.OpenProductDetails();
		search.CheckReviews();
		String result=search.ShowReviews();
		if(result.contains("There are no reviews for this product."))
		{
			Assert.fail("Reviews are not available");
		}	
	}
	
  	@Ignore
	@Test(dataProvider="productnavigation")
	public void ReviewFunc(String productName) throws InterruptedException
	{
		search.SearchProductFunc(productName);
		search.OpenProductDetails();
		search.CheckReviews();
		search.EnterReviewerName("Harshal Chavan");
		search.Enter_Review("Product is good. Fast Delivery Etc etc");
		search.SendRatings();
		search.SendReview();
		if(!search.CheckSuccessAlert())
		{
			Assert.fail("Success Message is not displayed");
		}
	}
	
	
  	@Ignore
	@Test
	public void VerifyFields() throws InterruptedException
	{
		search.SearchProductFunc("HP LP3065");
		search.OpenProductDetails();
		search.CheckReviews();
		search.EnterReviewerName("Harsh3938");
		search.Enter_Review("Product is good. Fast Delivery Etc etc Maza aa gaya bhai full maje le raha hai me");
		search.SendRatings();
		search.SendReview();
		if(search.CheckSuccessAlert())
		{
			Assert.fail("First Name Field is Accepting Numeric Data");
		}
	}
	
	
  	@Ignore
	@Test(dataProvider="productnavigation")
	public void VerifyAddToCart(String productName) throws InterruptedException
	{
		search.SearchProductFunc(productName);
		search.OpenProductDetails();
		search.AddProdCart();
		if(!search.CheckAddCartSuccess())
		{
			Assert.fail("Product is not added to the cart");
		}
	}
	
  	@Ignore
	@Test 
	public void verifyMandatoryFields() throws InterruptedException
	{
		search.SearchProductFunc("HP LP3065");
		search.OpenProductDetails();
		search.CheckReviews();
		search.EnterReviewerName("Harsh3938");
		search.SendRatings();
		search.SendReview();
		if(!search.CheckReviewMissing())
		{
			Assert.fail("Review Missing is not Displayed");
		}
	}
	
	
  	@Ignore
	@Test
	public void CartAddDate() throws InterruptedException
	{
		search.SearchProductFunc("HP LP3065");
		search.OpenProductDetails();
		search.EnterDeliveryDate("2012-01-01");
		search.AddProdCart();
		if(search.CheckAddCartSuccess())
		{
			Assert.fail("Website is accepting previous delivery date");
		}
		else 
		{
			Assert.assertTrue(true);
		}
	}
	
  	@Ignore
	@Test
	public void verifyQuantity() throws InterruptedException
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		now.plusWeeks(2);
		String futureDate= dtf.format(now);
		search.SearchProductFunc("HP LP3065");
		search.OpenProductDetails();
		search.EnterDeliveryDate(futureDate); // 2030-11-11
		search.EnterProdQuantity("0");
		search.AddProdCart();
		if(search.CheckAddCartSuccess())
		{
			Assert.fail("Success Message is displayed Even without valid quantity");
		}
	}

  @BeforeClass
  public void beforeClass(){
	  
	  //System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
	  WebDriverManager.chromedriver().setup();
	  driver=new ChromeDriver();
	  driver.get("https://demo.opencart.com");
	  //driver.navigate().to("https://demo.opencart.com");
	  driver.manage().window().maximize();
	  //implicit wait syntax
	  driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	  wait=new WebDriverWait(driver,30);
	  js = (JavascriptExecutor) driver;
	  search=new SearchProduct(driver);
	  reports = new ExtentReports();
	  reporter = new ExtentSparkReporter("Test Report.html");
	  reports.attachReporter(reporter);
	  PageFactory.initElements(driver,search);
  }
  
  @DataProvider(name="productnavigation")
  public Object[][] dataSource() throws IOException
  {
	  FileInputStream file = new FileInputStream(new File("ProductNavigation.xlsx")); 
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet sheet = wb.getSheetAt(0);
		int rowcount=sheet.getLastRowNum();
		Object[][] values=new Object[rowcount+1][1];
		//getWindowHandle() - String id
		//List<WebElement> abcd = driver.findElements(By.xpath(""));
		//int rows = abcd.size();
		//getWindowHandles() - Set
		for(int i=0;i<=rowcount;i++)
		{
			Row  row=sheet.getRow(i);
			values[i][0]=row.getCell(0).toString();
		}
		wb.close();
	  return values;
	  
  }

  @AfterClass
  public void afterClass() throws InterruptedException {
	  driver.quit();
	  reports.flush();
  }

}

