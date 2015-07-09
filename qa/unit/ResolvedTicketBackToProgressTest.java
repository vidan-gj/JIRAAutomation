package unit;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import data.ExcelUtility;
import data.ReadPropertiesFile;


public class ResolvedTicketBackToProgressTest {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

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
  Test must be in Resolved status upon start.
  If the test fails (for any reason), before running it again, you need
  to manually set the status to: RESOLVED
	 ***********************************************************************/

	@Test
	public void testResolvedTicketBackToProgress() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());

		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());

		driver.findElement(By.id("login")).click();
		Thread.sleep(4000);

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String ticketNumber = ExcelUtility.getCellData(8, 1);

		System.out.println("ticket: " + ticketNumber);

		// Search for the ticket
		driver.findElement(By.id("quickSearchInput")).clear();

		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);

		/**
      ENTER FOOL-PROOF LOGIC TO CHECK FOR STATUS OF THE TICKET BEFORE RUNNING THE TEST:
      if the Status != Resolved then change it to Resolved for all cases
		 */

		//The ticket should be in Resolved status. This action selects "Back to In Progress"
		driver.findElement(By.cssSelector("#action_id_801 > span.trigger-label")).click();

		new Select(driver.findElement(By.id("customfield_10032"))).selectByVisibleText("Build Failed");
		driver.findElement(By.id("issue-workflow-transition-submit")).click();
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Build Failed".equals(driver.findElement(By.id("customfield_10032-val")).getText())) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		// Assert that Build Failed shows in Status when ticket is placed back into Progress
		assertEquals("Build Failed", driver.findElement(By.id("customfield_10032-val")).getText());

		// Wait 4 secs and Assert that In Progress is displayed
		Thread.sleep(4000);
		assertEquals("IN PROGRESS", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());

		// Resolve Issue
		driver.findElement(By.cssSelector("#action_id_5 > span.trigger-label")).click();
		driver.findElement(By.id("issue-workflow-transition-submit")).click();

		// Wait 4 secs and Assert that Resolved is displayed
		Thread.sleep(4000);
		assertEquals("RESOLVED", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());

		// Select Back to In Progress
		driver.findElement(By.cssSelector("#action_id_801 > span.trigger-label")).click();
		driver.findElement(By.id("issue-workflow-transition-submit")).click();

		// Resolve Issue
		driver.findElement(By.cssSelector("#action_id_5 > span.trigger-label")).click();
		driver.findElement(By.id("issue-workflow-transition-submit")).click();

		// Wait 4 secs and Assert that Resolved is displayed
		Thread.sleep(4000);  
		assertEquals("RESOLVED", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());

		// Log out
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
