package unit;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import net.sf.cglib.core.ClassNameReader;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.ClickAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.reporters.jq.TimesPanel;

import com.thoughtworks.selenium.Wait;

import data.ExcelUtility;
import data.ReadPropertiesFile;

public class TicketUpdateTest {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private static Logger logger = Logger.getLogger(TicketUpdateTest.class);

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
	public void testTicketUpdate() throws Exception {
		logger.info("---------------------------- TEST START ------------------------------------");
		logger.info("***************************** TicketUpdateTest ********************************");  

		driver.get(data.getUrl() + "/secure/Dashboard.jspa");
		logger.info("Open the Home Page");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(5000);
		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());

		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());

		driver.findElement(By.id("login")).click();
		logger.info("User Login Successful");
		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		/*    // Tell the code about the location of Excel file
    ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

    String ticketNumber = ExcelUtility.getCellData(1, 1);

    System.out.println("ticket: " + ticketNumber);

    driver.findElement(By.id("quickSearchInput")).clear();
    driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);  // pulls value from excel

    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    // Using same element, sending "ENTER" Key instead of click method
    driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
    Thread.sleep(6000);

		 */   
		/* **DEPRICATED**
    WebDriverWait wait = new WebDriverWait(driver, 5);
    WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.hidden")));
    searchButton.click();
    searchButton.sendKeys(Keys.ENTER); 
		 */
		//********** CREATE ERM TICKET LOGIC: **************

		// Create button
		driver.findElement(By.id("create_link")).click();
		Thread.sleep(6000);

		// Select ERM Project
		driver.findElement(By.id("project-field")).click();
		Thread.sleep(6000);
		driver.findElement(By.id("project-field")).sendKeys("Enterprise Release Management");
		Thread.sleep(3000);
		driver.findElement(By.id("project-field")).sendKeys(Keys.ENTER);
		Thread.sleep(10000);

		// Wait method until Summary fields is displayed
		WebDriverWait wait = new WebDriverWait(driver, 15);
		Thread.sleep(15000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("summary")));

		String className = this.getClass().getName();

		// Enter Summary details
		driver.findElement(By.id("summary")).click();
		driver.findElement(By.id("summary")).clear();
		driver.findElement(By.id("summary")).sendKeys("Testing: " + className);
		Thread.sleep(6000);

		//driver.findElement(By.id("assign-to-me-trigger")).click();

		// Enter Description details
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Test: " + className);

		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("create-issue-submit"))));

		// Create Issue
		driver.findElement(By.id("create-issue-submit")).click();
		Thread.sleep(5000);

		String newTicketString = driver.findElement(By.xpath("(//*[contains(text(), '" + "ERM-" + "')])")).getText();
		System.out.println("Ticket Created: " + newTicketString);

		String ticketNumber = newTicketString.substring(0, 8);
		logger.info("A new ticket has been created: " + ticketNumber);

		// Enter ticket number
		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(6000);

		// Change Assignee to yourself
		driver.findElement(By.id("assign-to-me")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("opsbar-operations_more")));
		Thread.sleep(12000);
		logger.info("Assigne field has been changed");

		// Assign ticket to user B
		driver.findElement(By.cssSelector("#assign-issue > span.trigger-label")).click();
		driver.findElement(By.id("assignee-field")).sendKeys("Mahalingam, Kesava");
		driver.findElement(By.cssSelector("a.aui-list-item-link.aui-iconised-link > span")).click();
		driver.findElement(By.id("assign-issue-submit")).click();
		logger.info("Ticket is assigned to another user");

		// wait for page to load
		Thread.sleep(10000);

		// Change the Release Type
		driver.findElement(By.cssSelector("span.trigger-label")).click();
		new Select(driver.findElement(By.id("customfield_10010"))).selectByVisibleText("Non Impactful Change");

		driver.findElement(By.id("edit-issue-submit")).click();

		// Add wait time for "Non Impactful Change"
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("Non Impactful Change".equals(driver.findElement(By.id("customfield_10010-val")).getText())) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
		// Assert that the Release Type has changed
		assertEquals("Non Impactful Change", driver.findElement(By.id("customfield_10010-val")).getText());
		logger.info("Release Type has been changed");


		// Add a comment
		driver.findElement(By.xpath("//a[@id='footer-comment-button']/span[2]")).click();
		driver.findElement(By.id("comment")).clear();
		driver.findElement(By.id("comment")).sendKeys("Testing");
		driver.findElement(By.id("issue-comment-add-submit")).click();
		Thread.sleep(5000);
		logger.info("A comment has been added");

		// Check History Tab
		driver.findElement(By.cssSelector("#changehistory-tabpanel > strong")).click();
		Thread.sleep(4000);
		logger.info("History Tab reflects most recent changes");

		// Assert in the History Tab that the Change is displayed
		driver.getPageSource().contains("Non Impactful Change [ 15752 ]");

		// Delete the ticket
		driver.findElement(By.id("opsbar-operations_more")).click();
		driver.findElement(By.id("delete-issue")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("delete-issue-submit")).click();
		Thread.sleep(8000);

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
