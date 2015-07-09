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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import data.ReadPropertiesFile;


public class JiraReIndexingTest {
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
	public void testJiraReIndexing() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();

		driver.findElement(By.xpath("//a[@id='admin_menu']/span")).click();
		driver.findElement(By.id("admin_system_menu")).click();
		Thread.sleep(3000);

		driver.findElement(By.id("login-form-authenticatePassword")).clear();
		driver.findElement(By.id("login-form-authenticatePassword")).sendKeys(data.getPassword());
		driver.findElement(By.id("login-form-submit")).click();
		Thread.sleep(4000);

		driver.findElement(By.id("indexing")).click();

		// assert you are on the right page
		assertEquals("Re-Indexing", driver.findElement(By.cssSelector("h2")).getText());

		// select Re-Index button
		driver.findElement(By.id("indexing-submit")).click();

		// driver.findElement(By.id("fbCloseButton")).click();


		// Explicit Wait + Expected Condition
		WebDriverWait wait = new WebDriverWait(driver, 36000); // time measure is in seconds
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acknowledge_submit")));


		assertEquals("", driver.findElement(By.id("acknowledge_submit")).getText());

		assertEquals("Acknowledge", driver.findElement(By.id("acknowledge_submit")).getAttribute("value"));

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
