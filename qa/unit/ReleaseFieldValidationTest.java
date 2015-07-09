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

public class ReleaseFieldValidationTest {
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

	 ***********************************************************************/

	@Test
	public void testReleaseFieldValidation() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());

		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());

		driver.findElement(By.id("login")).click();
		Thread.sleep(5000);

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String ticketNumber = ExcelUtility.getCellData(13, 1); 	// pull ticket number from Excel data sheet: Stage
		String ticketNumber2 = ExcelUtility.getCellData(13, 2);
		String fixVersion = ExcelUtility.getCellData(13, 4);	// pull fix Version name from Excel data sheet: Stage
		String releaseContent = ExcelUtility.getCellData(13, 5);

		System.out.println("ticket: " + ticketNumber);


		driver.findElement(By.id("quickSearchInput")).clear();

		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);

		// wait for page to load
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// click Enter on the Search box
		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);

		// Edit
		driver.findElement(By.cssSelector("span.trigger-label")).click();
		Thread.sleep(5000);

		//driver.findElement(By.xpath("//div[@id='fixVersions-multi-select']/span")).click();


		//driver.findElement(By.xpath("//div[@id='fixVersions-multi-select']/span")).sendKeys("INTF_CAPS-1.6");

		driver.findElement(By.id("fixVersions-textarea")).clear();

		driver.findElement(By.id("fixVersions-textarea")).sendKeys(fixVersion);
		Thread.sleep(2000);
		driver.findElement(By.id("fixVersions-textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		//driver.findElement(By.linkText("INTF_CAPS-1.6")).click();

		// Save Change
		driver.findElement(By.id("edit-issue-submit")).click();
		Thread.sleep(5000);

		driver.findElement(By.id("quickSearchInput")).clear();


		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber2);

		// wait for page to load
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// click Enter on the Search box
		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(7000);

		driver.findElement(By.id("edit-issue")).click();
		Thread.sleep(7000);



		// Enter Release Content
		driver.findElement(By.id("customfield_11050-textarea")).sendKeys(releaseContent);
		Thread.sleep(2000);

		driver.findElement(By.id("customfield_11050-textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);

		// Save Change
		driver.findElement(By.id("edit-issue-submit")).click();
		Thread.sleep(12000);


		driver.findElement(By.linkText(releaseContent)).click();
		Thread.sleep(3000);
		//assertEquals("INF-9040", driver.findElement(By.linkText("INF-15279")).getText());
		//assertEquals("INF-2251", driver.findElement(By.linkText("INF-15536")).getText());


		driver.findElement(By.id("quickSearchInput")).clear();

		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);
		Thread.sleep(2000);

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);

		//assertEquals("ERM-1391: Testing - Please Ignore (7/Dec/14)", driver.findElement(By.id("customfield_11051-field")).getText());

		// **REVERT ALL CHANGES**

		// Edit
		driver.findElement(By.cssSelector("span.trigger-label")).click();

		// remove Fix Version
		driver.findElement(By.cssSelector("em.item-delete")).click();

		// Save Changes
		driver.findElement(By.id("edit-issue-submit")).click();

		// Open ERM ticket
		driver.findElement(By.id("quickSearchInput")).clear();

		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber2);

		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);

		// Edit
		driver.findElement(By.cssSelector("span.trigger-label")).click();
		Thread.sleep(5000);

		// remove Release Content
		driver.findElement(By.cssSelector("#customfield_11050-multi-select > div.representation > ul.items > li.item-row > em.item-delete")).click();

		// Save changes
		driver.findElement(By.id("edit-issue-submit")).click();
		Thread.sleep(6000);
		
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
