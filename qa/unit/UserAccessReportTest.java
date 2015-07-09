package unit;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import data.ExcelUtility;
import data.ReadPropertiesFile;

public class UserAccessReportTest {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private static Logger logger = Logger.getLogger(UserAccessReportTest.class);
	
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


	/************** TEST INFO *********************************************
  	Test is configured to run on Stage and Prod.
	 ***********************************************************************/



	@Test
	public void testUserAccessReport() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.switchTo().frame("gadget-0");

		// Log In
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(4000);

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String user = ExcelUtility.getCellData(19, 6);

		System.out.println("ticket: " + user);

		// Go to Projects List
		driver.get(data.getUrl() + "/browse/ERM/?selectedTab=com.atlassian.jira.jira-projects-plugin:reports-panel");
		Thread.sleep(3000);

		// Select User Access Report
		driver.findElement(By.cssSelector("a[title=\"This User-Access report can be used to find user access to various projects. <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(a) Select one or more Project/s to list users access for the selected project/s <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(b) Enter User IDs (LAN ID) to list all the project membership for the user/s [for multiple users seperate the User IDs using comma (,)]. <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Note: If you leave this field blank, all users in the selected project/s will be considered for reporting\"]")).click();
		Thread.sleep(2000);

		/**
     ---------------   LOGIC FOR MULTIPLE ELEMENT SELECTION (select 1 element + hold key (shift) + select 2nd element)   ------------------
		 */

		// Select SCRM Projects
		// first project
		driver.findElement(By.xpath("//*[@id='configure-report']/fieldset/fieldset/div[1]/fieldset/select/option[51]")).click();

		// second project
		//WebElement table = driver.findElement(By.xpath("/html/body/div/section/div/div/section/form/table/tbody/tr[4]/td[2]/select"));

		WebElement secondElement = driver.findElement(By.xpath("//*[@id='configure-report']/fieldset/fieldset/div[1]/fieldset/select/option[52]"));

		Actions selectTwo = new Actions(driver);

		Action holdShiftAndSelect = selectTwo.keyDown(Keys.SHIFT).moveToElement(secondElement).click().build();

		holdShiftAndSelect.perform();

		Thread.sleep(2000);
		// ---------------------------------------------------------------------------------------------------------------------------------------	
		// Enter User Info
		logger.info(user);
		driver.findElement(By.name("lanIDList")).sendKeys(user);

		Thread.sleep(2000);

		// Next button
		driver.findElement(By.id("next_submit")).click();
		Thread.sleep(2000);

		// Validate user
		assertEquals("kmahal01", driver.findElement(By.xpath("//section[@id='content']/div/div/section/div/font/table/tbody/tr[3]/td[2]")).getText());

		// Validate Project Names
		assertEquals("SCRM", driver.findElement(By.xpath("//section[@id='content']/div/div/section/div/font/table/tbody/tr[3]/td[3]")).getText());
		assertEquals("SCRM Automation", driver.findElement(By.xpath("//section[@id='content']/div/div/section/div/font/table/tbody/tr[5]/td[3]")).getText());

		// Validate Jira column
		assertEquals("SCRM", driver.findElement(By.xpath("//section[@id='content']/div/div/section/div/font/table/tbody/tr[3]/td[5]")).getText());
		assertEquals("SAU", driver.findElement(By.xpath("//section[@id='content']/div/div/section/div/font/table/tbody/tr[5]/td[5]")).getText());

		// Log Out
		driver.findElement(By.xpath("//*[@id='header-details-user-fullname']/span/span/img")).click();

		driver.findElement(By.id("log_out")).click();
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
