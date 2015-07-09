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


public class ResolvedTicketDeployIssueTest {
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
  Workflow > Report Deploy Issue: creates a deployment issue assigned to
  Ravi Nishant. 
  Remember to periodically manually delete all dep. tickets created under
  the main INF ticket (INF-14020).
	 ***********************************************************************/

	@Test
	public void testResolvedTicketDeployIssue() throws Exception {
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

		String ticketNumber = ExcelUtility.getCellData(4, 1);

		System.out.println("ticket: " + ticketNumber);


		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);

		// Assertion of RESOLVED status prior to filing a Deploy Issue
		Thread.sleep(3000);
		assertEquals("RESOLVED", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());

		// Workflow dropdown
		driver.findElement(By.cssSelector("#opsbar-transitions_more > span.dropdown-text")).click();
		Thread.sleep(2000);

		// Report Deploy Issue
		driver.findElement(By.cssSelector("#action_id_861 > span.trigger-label")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("issue-workflow-transition-submit")).click();
		Thread.sleep(5000);


		// Check History Tab
		driver.findElement(By.cssSelector("#changehistory-tabpanel > strong")).click();

		/** Assert Deployment ticket creation is successful
		 *   [@id="changehistorydetails_1390854"]/span/time
		 *   #changehistorydetails_1390854 > span > time
		 */
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
