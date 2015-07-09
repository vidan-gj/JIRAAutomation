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

public class ResolveINFTicketTest {
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
  Test is configured to run on Stage and Prod.
	 ***********************************************************************/

	@Test
	public void testResolveINFTicket() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		// switch to Login iframe
		//driver.switchTo().frame("gadget-0");

		// Log in
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(5000);

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String ticketNumber = ExcelUtility.getCellData(18, 1);

		System.out.println("Ticket: " + ticketNumber);


		// Search for ticket
		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);
		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		// Assert ticket is in OPEN Status
		assertEquals("OPEN", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());
		Thread.sleep(1000);

		// Resolve Ticket
		driver.findElement(By.cssSelector("#action_id_5 > span.trigger-label")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("issue-workflow-transition-submit")).click();
		Thread.sleep(5000);

		// Assert Resolved text has appeared
		assertEquals("Resolved:", driver.findElement(By.xpath("//div[@id='datesmodule']/div[2]/ul/li/dl[3]/dt")).getText());

		// Assert "Just now" is under Resolved date
		assertEquals("Just now", driver.findElement(By.cssSelector("#resolved-date > time.livestamp")).getText());

		driver.getPageSource().contains("Just now");


		// Put on Hold
		driver.findElement(By.cssSelector("#action_id_731 > span.trigger-label")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("issue-workflow-transition-submit")).click();
		Thread.sleep(5000);

		// Place in Open
		driver.findElement(By.cssSelector("#action_id_741 > span.trigger-label")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("issue-workflow-transition-submit")).click();
		Thread.sleep(5000);

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
