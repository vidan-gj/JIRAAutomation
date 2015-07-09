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
import org.testng.Reporter;

import data.ExcelUtility;
import data.ReadPropertiesFile;

public class TicketMoveToProjectTest {
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
  elementVal is fixed and exposes a flaw in Jira where when a ticket is 
  moved to another project (generating new ticket number), you can still 
  search with the previous ticket and get the new one. 
  This test will work as long as this pre-condition is valid. 
	 ***********************************************************************/

	@Test
	public void testTicketMoveToProject() throws Exception {

		Reporter.log("--------------------------------- Start of TicketMoveToProjectTest----------------------------------------------");


		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.switchTo().frame("gadget-0");


		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());

		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());

		driver.findElement(By.id("login")).click();

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String ticketNumber = ExcelUtility.getCellData(2, 1);

		System.out.println("ticket: " + ticketNumber);


		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		// More tab
		driver.findElement(By.cssSelector("span.dropdown-text")).click();

		// Select Move
		driver.findElement(By.cssSelector("#move-issue > span.trigger-label")).click();
		Thread.sleep(2000);

		// Select Informatica Interfaces
		driver.findElement(By.xpath("//div[@id='project-single-select']/span")).click();
		driver.findElement(By.linkText("Informatica Interfaces (INF)")).click();
		//driver.findElement(By.xpath("//div[@id='project-single-select']/span")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		// Next button
		driver.findElement(By.id("next_submit")).click();
		Thread.sleep(2000);

		/** THIS logic doesn't work:
    WebElement txBox1 = driver.findElement(By.id("customfield_10090"));
    txBox1.click();
    driver.findElement(By.linkText("SDLC Uplift and Environment Simplification")).click();
    Thread.sleep(2000);

    WebElement txBox2 = driver.findElement(By.id("customfield_10151"));
    txBox2.click();
    driver.findElement(By.linkText("SCRM")).click();
    Thread.sleep(2000);
		 */

		// THIS LOGIC WORKS:

		    new Select(driver.findElement(By.id("customfield_10090"))).selectByVisibleText("2012 - SA SG HPXR");
    Thread.sleep(2000);

    new Select(driver.findElement(By.id("customfield_10151"))).selectByVisibleText("SCRM");
    Thread.sleep(2000);


		driver.findElement(By.id("next_submit")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("move_submit")).click();
		Thread.sleep(5000);

		driver.findElement(By.cssSelector("span.dropdown-text")).click();
		driver.findElement(By.cssSelector("#move-issue > span.trigger-label")).click();
		driver.findElement(By.xpath("//div[@id='project-single-select']/span")).click();
		driver.findElement(By.linkText("SCRM Automation (SAU)")).click();
		Thread.sleep(3000);

		driver.findElement(By.id("next_submit")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("next_submit")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("move_submit")).click();
		Thread.sleep(4000);

		assertEquals("SCRM Automation", driver.findElement(By.id("project-name-val")).getText());

		//Select select = new Select(driver.findElement(By.xpath("//div[@id='project-single-select']/span")));
		//Select select = new Select(driver.findElement(By.id("recent-projects")));
		//select.selectByIndex(1);

		Reporter.log("--------------------------------- End of TicketMoveToProjectTest----------------------------------------------");
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
