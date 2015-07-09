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

public class CloneTicketTest {
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

	/************** TEST INFO ***************************************************
  The test creates a clone ticket from an existing parent ticket.
  Validates that the clone has been successfully created, and deletes the same.

	 ****************************************************************************/

	@Test
	public void testCloneTicket() throws Exception {
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

		String ticketNumber = ExcelUtility.getCellData(10, 1);

		System.out.println("ticket: " + ticketNumber);

		// Search for the ticket
		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);
		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		// select More tab
		driver.findElement(By.cssSelector("span.icon.drop-menu")).click();

		// select Clone 
		driver.findElement(By.cssSelector("#clone-issue > span.trigger-label")).click();
		Thread.sleep(3000);

		/**
		 * random() generates a number between 0.0000 and 0.99999
		 * we want this number to be between 10000 and 99999
		 * 
		 * random() * 89999 + 10000
		 * 
		 * lower limit:
		 * round(0 * 89999) + 10000 = 10000 OK!
		 * 
		 * higher limit:
		 * round(0.99999 * 89999) + 10000 = 99999 OK!
		 */       



		int rand = 0;
		for (int i = 0; i < 10; i++) {
			rand = (int) (Math.round(Math.random() * 8999) + 10000);
		}


		// clear text box
		driver.findElement(By.id("summary")).clear();

		// apply new clone ticket name
		driver.findElement(By.id("summary")).sendKeys("CLONE-Test" + rand);

		// select Create button
		driver.findElement(By.id("assign-issue-submit")).click();
		Thread.sleep(9000);

		// assert parent ticket
		assertEquals("Testing: Clone Ticket Test", driver.findElement(By.cssSelector("span.link-summary")).getText());


		try {
			assertEquals("Testing - Please Ignore: CloneTicketTest", driver.findElement(By.cssSelector("span.link-summary")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		// Revert changes:
		driver.findElement(By.id("opsbar-operations_more")).click();

		driver.findElement(By.cssSelector("#delete-issue > span.trigger-label")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("delete-issue-submit")).click();
		Thread.sleep(4000);
		driver.quit();

		/*    // Log Out
    driver.navigate().back();
    Thread.sleep(3000);
    driver.findElement(By.xpath("//*[@id='header-details-user-fullname']/span/span/img")).click();
    driver.findElement(By.id("log_out")).click();*/
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
