package unit;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import data.ExcelUtility;
import data.ReadPropertiesFile;

public class ScheduleHealthQueryTest {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private static Logger logger = Logger.getLogger(ScheduleHealthQueryTest.class);

	@Before
	public void setUp() throws Exception {
		// Load the Web Driver
		driver = data.getBrowser();
		// Maximize the browser's window
		driver.manage().window().maximize();
		// Implicit wait 
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// Initialize log4j properties file
		PropertyConfigurator.configure("log4j.properties"); 
	}

	ReadPropertiesFile data = new ReadPropertiesFile();
	String query = "project = EPP and issuetype = \"Environment Instance\"";


	/************** TEST INFO *********************************************
  Test is configured to run on Stage and Prod.
	 ***********************************************************************/

	@Test
	public void testScheduleHealthQuery() throws Exception {
		logger.info("---------------------------- TEST START ------------------------------------");
		logger.info("***************************** ScheduleHealthQueryTest ********************************");	  
		driver.get(data.getUrl() + "/secure/Dashboard.jspa"); 	

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Log in
		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(5000);

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String viewCol = ExcelUtility.getCellData(17, 6);

		// Issues Tab    
		driver.findElement(By.id("find_link")).click();
		Thread.sleep(2000);

		// Search for Issues
		driver.findElement(By.id("issues_new_search_link_lnk")).click();
		Thread.sleep(2000);

		// Enter query String and Search
		driver.findElement(By.id("advanced-search")).clear();

		driver.findElement(By.id("advanced-search")).sendKeys(query);

		// press ENTER
		driver.findElement(By.id("advanced-search")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		// Columns drop-down
		driver.findElement(By.xpath("//section[@id='content']/div/div[4]/div/div/div/div/div/div/div/div[2]/div/button")).click();
		Thread.sleep(3000);

		//driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();

		// Select Schedule Health
		driver.findElement(By.id("user-column-sparkler-input")).clear();
		driver.findElement(By.id("user-column-sparkler-input")).sendKeys(viewCol);
		Thread.sleep(3000);

		// One click on checkbox
		driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();

		// Done
		driver.findElement(By.cssSelector("input.aui-button.submit")).click();
		Thread.sleep(4000);

		//Assert the grey box is present
		assertEquals("", driver.findElement(By.cssSelector("#issuerow121903 > td.customfield_10950 > img")).getText());

		// Scan whole page and check if there are grey boxes
		driver.getPageSource().contains("/images/grey.png");

		// Scan whole page and check if there are red boxes
		driver.getPageSource().contains("/images/red.png");

		// Scan whole page and check if there are green boxes
		driver.getPageSource().contains("/images/green.png");



		assertEquals("", driver.findElement(By.cssSelector("#issuerow120232 > td.customfield_10950 > img")).getText());

		assertEquals("", driver.findElement(By.cssSelector("#issuerow116438 > td.customfield_10950 > img")).getText());

		assertEquals("", driver.findElement(By.cssSelector("#issuerow111346 > td.customfield_10950 > img")).getText());


		// -------------------------------------------- Revert Changes: --------------------------------------------------

		// Columns drop-down
		driver.findElement(By.xpath("//section[@id='content']/div/div[4]/div/div/div/div/div/div/div/div[2]/div/button")).click();
		Thread.sleep(3000);

		//driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();

		// Select Schedule Health
		driver.findElement(By.id("user-column-sparkler-input")).clear();
		driver.findElement(By.id("user-column-sparkler-input")).sendKeys(viewCol);
		Thread.sleep(3000);

		// One click on checkbox
		driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();

		// Done
		driver.findElement(By.cssSelector("input.aui-button.submit")).click();
		Thread.sleep(4000);

		// Log Out
		driver.findElement(By.xpath("//*[@id='header-details-user-fullname']/span/span/img")).click();

		driver.findElement(By.id("log_out")).click();
		logger.info("----------------------------- TEST END -------------------------------------");
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
