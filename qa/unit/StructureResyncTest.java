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

import data.ReadPropertiesFile;

public class StructureResyncTest {
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
	public void testStructureResync() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(4000);

		// Structure tab
		driver.findElement(By.id("wi-structure-link")).click();

		// Manage Structures
		driver.findElement(By.id("wi-structure-manage_lnk")).click();

		// My Structures
		driver.findElement(By.cssSelector("#s-manage-tab-my > strong")).click();
		Thread.sleep(3000);


		// Settings
		driver.findElement(By.linkText("Settings")).click();
		Thread.sleep(2000);

		// Resync
		driver.findElement(By.linkText("Resync")).click();
		Thread.sleep(2000);

		// Start Resync
		driver.findElement(By.id("submit")).click();


		Alert alert = driver.switchTo().alert();
		alert.accept();


		Thread.sleep(5000);
		assertEquals("Finished", driver.findElement(By.cssSelector("div.checkbox")).getText());
		Thread.sleep(4000);
		
		// Log out
		driver.findElement(By.xpath("//*[@id='header-details-user-fullname']/span/span/img")).click();
		Thread.sleep(3000);
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
