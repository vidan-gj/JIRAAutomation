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


public class TicketChangeTypeTest {
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
  Prerequisites: 
    1. Test must start as Change Request type
       a. If test fails (for any reason), the test needs to manually be 
         changed back to Change Request type.
    2. Since the execution is fast, before any assertion, there is a 
     wait method for the corresponding element. This way the assertion
     will happen only if the element is displayed on the page.
  Test: 
    Start -> Change Request ->  Environment Configuration Request ->
    validate change -> Change request  
	 ***********************************************************************/

	@Test
	public void testTicketChangeType() throws Exception {
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

		String ticketNumber = ExcelUtility.getCellData(11, 1);

		System.out.println("ticket: " + ticketNumber);

		// Search for the ticket
		driver.findElement(By.id("quickSearchInput")).clear();

		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);

		// Select Edit tab
		driver.findElement(By.cssSelector("span.trigger-label")).click();
		Thread.sleep(3000);

		// Select Issue Type drop-down menu
		driver.findElement(By.xpath("//div[@id='issuetype-single-select']/span")).click();
		Thread.sleep(1000);

		// Click "Environment Configuration Request"
		driver.findElement(By.linkText("Environment Configuration Request")).click();
		Thread.sleep(1000);

		// Click Update button
		driver.findElement(By.id("edit-issue-submit")).click();
		Thread.sleep(9000);

		// Assert that the change has been successful
		assertEquals("Environment Configuration Request", driver.findElement(By.id("type-val")).getText());
		Thread.sleep(2000);

		// Edit
		driver.findElement(By.cssSelector("span.trigger-label")).click();

		driver.findElement(By.xpath("//div[@id='issuetype-single-select']/span")).click();
		Thread.sleep(2000);

		driver.findElement(By.linkText("Change Request")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("edit-issue-submit")).click();
		Thread.sleep(7000);

		assertEquals("Change Request", driver.findElement(By.id("type-val")).getText());

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
