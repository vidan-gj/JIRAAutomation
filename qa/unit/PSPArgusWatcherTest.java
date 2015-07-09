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

public class PSPArgusWatcherTest {
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

	@Test
	public void testPSPArgusWatcher() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");
		Thread.sleep(3000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.switchTo().frame("gadget-0");

		// Log In    
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String ticketNumber = ExcelUtility.getCellData(5, 1);

		System.out.println("ticket: " + ticketNumber);


		// Search for ticket
		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);
		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);

		// assert OPEN Status of the ticket prior to running test
		assertEquals("OPEN", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());


		// select Approve & Assign Ticket tab
		driver.findElement(By.cssSelector("#action_id_811 > span.trigger-label")).click();
		Thread.sleep(4000);

		// assert Status has changed to "Assigned"
		assertEquals("ASSIGNED", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());

		// assert Assignee has changed to: "Argus Health Blueshield of CA"
		assertEquals("Argus Health Blueshield of CA", driver.findElement(By.id("issue_summary_assignee_svcArgusBSC")).getText());

		// store watcher value
		String watcher = driver.findElement(By.id("issue_summary_assignee_svcArgusBSC")).getText();

		/*    // View Watchers
    driver.findElement(By.id("watcher-data")).click();
    Thread.sleep(2000);*/

		// More tab
		driver.findElement(By.cssSelector("span.dropdown-text")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("manage-watchers")).click();
		Thread.sleep(3000);

		// assert "Argus Health Blueshield of CA" has been added to the Watcher list
		assertEquals("Argus Health Blueshield of CA", driver.findElement(By.id("watcher_link_svcArgusBSC")).getText());
		Thread.sleep(3000);
		driver.findElement(By.id("back-lnk")).click();


		// REVERT CHANGES:

		// select Put On Hold
		driver.findElement(By.cssSelector("#action_id_831 > span.trigger-label")).click();
		Thread.sleep(3000);

		// select for Assignee: Unassigned
		driver.findElement(By.xpath("//div[@id='assignee-single-select']/span")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Unassigned")).click();

		// set On Hold Reason: "Decision Pending"
		driver.findElement(By.id("customfield_10141")).click();
		Thread.sleep(2000);


		// select the first operator using "select by value"
		Select select = new Select(driver.findElement(By.id("customfield_10141")));
		select.selectByValue("10280");
		Thread.sleep(3000);

		/*
		 * OR
		 * new Select(driver.findElement(By.id("customfield_10141"))).selectByValue("10280");
		 */

		driver.findElement(By.id("customfield_10141")).sendKeys(Keys.ENTER);


		// select Put On Hold button
		driver.findElement(By.id("issue-workflow-transition-submit")).click();
		Thread.sleep(6000);

		// select Place in Open tab
		driver.findElement(By.cssSelector("#action_id_741 > span.trigger-label")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("issue-workflow-transition-submit")).click();
		Thread.sleep(10000);


		driver.findElement(By.cssSelector("span.dropdown-text")).click();
		driver.findElement(By.cssSelector("#manage-watchers > span.trigger-label")).click();
		Thread.sleep(4000);
		driver.findElement(By.name("stopwatch_svcArgusBSC")).click();
		Thread.sleep(3000);
		driver.findElement(By.name("remove")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("back-lnk")).click();




		/* // view Watchers
    driver.findElement(By.id("watcher-data")).click();
    Thread.sleep(3000);

    // remove Argus watcher
    driver.findElement(By.cssSelector("span.remove-recipient.item-delete")).click();
    Thread.sleep(3000);


    driver.findElement(By.xpath("//span[@id='status-val']/span")).click();

    driver.findElement(By.xpath("//span[@id='status-val']/span")).click();
    Thread.sleep(4000);
		 */




		// assert Status: OPEN
		assertEquals("OPEN", driver.findElement(By.xpath("//span[@id='status-val']/span")).getText());

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
