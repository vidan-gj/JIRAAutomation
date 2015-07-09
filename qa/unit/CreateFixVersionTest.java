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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import data.ExcelUtility;
import data.ReadPropertiesFile;

public class CreateFixVersionTest {
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
	public void testCreateFixVersion() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(5000);

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String ticketNumber = ExcelUtility.getCellData(12, 1); 	// pull ticket number from Excel data sheet: Stage
		String fixVersion = ExcelUtility.getCellData(12, 4);	// pull fix Version name from Excel data sheet: Stage

		System.out.println("ticket: " + ticketNumber);


		// select Admin drop-down
		driver.findElement(By.xpath("//a[@id='admin_menu']/span")).click();

		//select Projects
		driver.findElement(By.id("admin_project_menu")).click();
		Thread.sleep(5000);

		// select INF Project
		driver.findElement(By.id("view-project-10040")).click();

		// select Versions
		driver.findElement(By.id("view_project_versions")).click();

		// create new Fix Version
		driver.findElement(By.name("name")).clear();

		driver.findElement(By.name("name")).sendKeys(fixVersion);	// pass fixVersion param. variable

		Thread.sleep(2000);

		driver.findElement(By.cssSelector("input.aui-button")).click();
		Thread.sleep(6000);


		// go to Ticket
		driver.findElement(By.id("quickSearchInput")).clear();

		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);	// pass ticket number param. variable

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(6000);

		// enter the newly created Fix Version
		driver.findElement(By.cssSelector("span.trigger-label")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("fixVersions-textarea")).sendKeys(fixVersion);
		Thread.sleep(2000);
		driver.findElement(By.id("fixVersions-textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		// Update the ticket
		driver.findElement(By.id("edit-issue-submit")).click();
		Thread.sleep(5000);

		// assert that the ticket has been updated with the new Fix Verison
		assertEquals("TestVersion", driver.findElement(By.linkText("TestVersion")).getText());

		/* -----------------------------------------------------------------------------------------------    */

		// Revert the changes:
		driver.findElement(By.id("edit-issue")).click();
		Thread.sleep(3000);

		// delete the Fix version
		driver.findElement(By.cssSelector("em.item-delete")).click();
		Thread.sleep(3000);

		// Update the ticket
		driver.findElement(By.id("edit-issue-submit")).click();
		Thread.sleep(5000);

		// assert that the Fix Version has beem removed form the ticket
		assertEquals("None", driver.findElement(By.id("fixfor-val")).getText());

		try {
			assertEquals("None", driver.findElement(By.id("fixfor-val")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		// go to Admin menu
		driver.findElement(By.xpath("//a[@id='admin_menu']/span")).click();
		Thread.sleep(3000);

		// select Projects
		driver.findElement(By.id("admin_project_menu")).click();
		Thread.sleep(4000);

		// select INF
		driver.findElement(By.id("view-project-10040")).click();
		Thread.sleep(3000);

		// select Versions
		driver.findElement(By.id("view_project_versions")).click();
		Thread.sleep(3000);

		// driver.findElement(By.cssSelector("span.project-config-icon.project-config-icon-drop")).click();
		// Thread.sleep(4000);
		// driver.findElement(By.cssSelector("span.project-config-icon.project-config-icon-manage")).click();
		// Thread.sleep(6000);
		// driver.findElement(By.cssSelector("li.aui-list-item.active > a.aui-list-item-link.project-config-operations-delete")).click();

		WebElement el1 = driver.findElement(By.id("version-34800-row")); // move to element > perform
		WebElement el2 = driver.findElement(By.cssSelector("span.project-config-icon.project-config-icon-drop")); // click
		WebElement el3 = driver.findElement(By.cssSelector("li.aui-list-item.active > a.aui-list-item-link.project-config-operations-delete")); // click

		Actions a1 = new Actions(driver);
		Actions a2 = a1.moveToElement(el1);
		a2.perform();
		el2.click();
		el3.click();



		Thread.sleep(9000);


		/**
    driver.findElement(By.cssSelector("span.project-config-icon.project-config-icon-drop")).click();
    Thread.sleep(6000);
    driver.findElement(By.cssSelector("li.aui-list-item.active > a.aui-list-item-link.project-config-operations-delete")).click();
		 */

		driver.findElement(By.id("submit")).click();

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
