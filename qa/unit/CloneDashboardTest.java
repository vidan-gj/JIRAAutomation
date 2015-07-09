package unit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import data.ReadPropertiesFile;


public class CloneDashboardTest {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private static Logger logger = Logger.getLogger(CloneDashboardTest.class);

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

	String cloneDashboard = "COPY Dashboard - Test";
	String cloneDesc = "Please Ignore";
	ReadPropertiesFile data = new ReadPropertiesFile();

	@Test
	public void testCloneDashboard() throws Exception {
		logger.info("---------------------------- TEST START ------------------------------------");
		logger.info("***************************** CloneDashboardTest ********************************");  
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		// switch to Login iframe
		//driver.switchTo().frame("gadget-0");

		// Log in
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(5000);

		// select Dashboards tab
		driver.findElement(By.id("home_link")).click();

		// select SCRM & Automation Dashboard
		driver.findElement(By.id("dash_lnk_19510_lnk")).click();
		Thread.sleep(5000);

		// select Tools dropdown
		driver.findElement(By.linkText("Tools")).click();

		// select "Copy Dashboard"
		driver.findElement(By.id("copy_dashboard")).click();
		Thread.sleep(3000);

		// Enter name for Dashboard clone
		driver.findElement(By.name("portalPageName")).clear();
		driver.findElement(By.name("portalPageName")).sendKeys(cloneDashboard);
		Thread.sleep(3000);

		// enter description
		driver.findElement(By.name("portalPageDescription")).clear();
		driver.findElement(By.name("portalPageDescription")).sendKeys(cloneDesc);
		Thread.sleep(3000);

		// Save button
		driver.findElement(By.id("add-dashboard-submit")).click();
		Thread.sleep(5000);

		// Assert the dashboard name
		try {
			assertEquals("COPY Dashboard - Test", driver.findElement(By.cssSelector("div.aui-page-header-main > h1")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		Thread.sleep(4000);

		// Delete Clone Dashboard
		driver.findElement(By.linkText("Tools")).click();
		driver.findElement(By.id("delete_dashboard")).click();
		driver.findElement(By.id("delete-portal-page-submit")).click();
		Thread.sleep(4000);

		// Log out
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
