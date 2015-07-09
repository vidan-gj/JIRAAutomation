package unit;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import data.ReadPropertiesFile;

public class AdminTaskTest {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private static Logger logger = Logger.getLogger(AdminTaskTest.class);

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
	public void testAdminTask() throws Exception {
		logger.info("---------------------------- TEST START ------------------------------------");
		logger.info("***************************** AdminTaskTest ********************************");  

		driver.get(data.getUrl() + "/secure/Dashboard.jspa");
		Thread.sleep(5000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(5000);

		// assert Project List
		driver.findElement(By.xpath("//a[@id='admin_menu']/span")).click();
		driver.findElement(By.id("admin_project_menu")).click();
		Thread.sleep(3000);
		assertEquals("Project list", driver.findElement(By.cssSelector("h2")).getText());

		// open User Management tab
		driver.findElement(By.xpath("//section[@id='content']/nav/div/div/ul/li[3]/a/span")).click();

		// enter user password again for Admin authentication
		driver.findElement(By.id("login-form-authenticatePassword")).clear();
		driver.findElement(By.id("login-form-authenticatePassword")).sendKeys(data.getPassword());

		// select Login button
		driver.findElement(By.id("login-form-submit")).click();

		// select User Directories
		driver.findElement(By.id("user_directories")).click();

		// assert Synchronise before sync test
		assertEquals("Synchronise", driver.findElement(By.linkText("Synchronise")).getText());
		Thread.sleep(3000);

		// select "Synchronise" link
		driver.findElement(By.linkText("Synchronise")).click();
		Thread.sleep(15000);

		// assert that sync test is complete
		//assertEquals("Synchronise", driver.findElement(By.linkText("Synchronise")).getText());

		// open Add-ons tab
		driver.findElement(By.xpath("//section[@id='content']/nav/div/div/ul/li[5]/a/span")).click();

		// open Application Links
		driver.findElement(By.id("ual")).click();
		Thread.sleep(5000);

		// Confluence -> Edit --------------------------------------------------------------------
		driver.findElement(By.linkText("Edit")).click();
		driver.findElement(By.id("menu-applinkDetailsPage")).click();
		Thread.sleep(3000);

		// assert Application Name = "GRID - Stage"
		try {
			assertEquals("GRID - Stage", driver.findElement(By.id("applicationName")).getAttribute("value"));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		// OPEN Outgoing Authentication
		driver.findElement(By.id("menu-outgoing-authentication-page")).click();
		Thread.sleep(3000);

		// switch to outer frame
		driver.switchTo().frame("outgoing-auth");

		// switch to inner frame
		driver.switchTo().frame("trustedAppsAuthenticationProviderPluginModule");

		driver.findElement(By.id("os_username")).clear();
		driver.findElement(By.id("os_username")).sendKeys(data.getUserName());
		driver.findElement(By.id("os_password")).clear();
		driver.findElement(By.id("os_password")).sendKeys(data.getPassword());
		driver.findElement(By.id("loginButton")).click();
		Thread.sleep(3000);

		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(data.getPassword());
		driver.findElement(By.id("authenticateButton")).click();
		Thread.sleep(4000);

		assertEquals("Configured", driver.findElement(By.cssSelector("span.field-value.status-configured")).getText());

		// Open OAuth tab

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("outgoing-auth");

		driver.findElement(By.cssSelector("#config-tab-1 > strong")).click();
		Thread.sleep(3000);

		// Switch to inner frame
		driver.switchTo().frame("OAuthAuthenticatorProviderPluginModule");

		Thread.sleep(3000);
		assertEquals("Not Configured", driver.findElement(By.cssSelector("span.field-value.status-not-configured")).getText());

		// Open Basic Access tab

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("outgoing-auth");

		// Basic Access tab
		driver.findElement(By.cssSelector("#config-tab-2 > strong")).click();

		Thread.sleep(3000);

		// Switch to inner iframe
		driver.switchTo().frame("BasicAuthenticationProviderPluginModule");

		/*  
    driver.findElement(By.id("basicUsername")).clear();
    driver.findElement(By.id("basicUsername")).sendKeys(data.getUserName());
    driver.findElement(By.id("password1")).clear();
    driver.findElement(By.id("password1")).sendKeys(data.getPassword());
    driver.findElement(By.id("password2")).clear();
    driver.findElement(By.id("password2")).sendKeys(data.getPassword());
    driver.findElement(By.cssSelector("input.button")).click();
    Thread.sleep(5000);
		 */

		// Assert "Configured" appears after successful login
		assertEquals("Not Configured", driver.findElement(By.cssSelector("span.field-value.status-not-configured")).getText());


		// OPEN Incoming Authentication

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Incoming Authentication
		driver.findElement(By.id("menu-incoming-authentication-page")).click();
		//driver.findElement(By.cssSelector("strong")).click();

		// switch to outer frame
		driver.switchTo().frame("incoming-auth");

		// switch to inner frame
		driver.switchTo().frame("trustedAppsAuthenticationProviderPluginModule");
		Thread.sleep(3000);


		assertEquals("Configured", driver.findElement(By.cssSelector("span.field-value.status-configured")).getText());

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("incoming-auth");

		// OAuth tab
		driver.findElement(By.cssSelector("#config-tab-1 > strong")).click();
		Thread.sleep(3000);

		// Switch to inner frame
		driver.switchTo().frame("OAuthAuthenticatorProviderPluginModule");

		Thread.sleep(3000);
		assertEquals("Not Configured", driver.findElement(By.cssSelector("span.field-value.status-not-configured")).getText());

		// Open Basic Access tab

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("incoming-auth");

		driver.findElement(By.cssSelector("#config-tab-2 > strong")).click();

		Thread.sleep(3000);

		// Switch to inner iframe
		driver.switchTo().frame("BasicAuthenticationProviderPluginModule");

		/*driver.findElement(By.id("basicUsername")).clear();
    driver.findElement(By.id("basicUsername")).sendKeys(data.getUserName());
    driver.findElement(By.id("password1")).clear();
    driver.findElement(By.id("password1")).sendKeys(data.getPassword());
    driver.findElement(By.id("password2")).clear();
    driver.findElement(By.id("password2")).sendKeys(data.getPassword());
    driver.findElement(By.cssSelector("input.button")).click();
		 */
		Thread.sleep(5000);

		// Assert "Configured" appears after successful login
		assertEquals("Not Configured", driver.findElement(By.cssSelector("span.field-value.status-not-configured")).getText());

		Thread.sleep(4000);

		// Switch to default window (exit all frames)
		driver.switchTo().defaultContent();

		driver.findElement(By.linkText("Close")).click();
		// ---------------------------------------------------------------------------------------------//    
		// FishEye -> Edit
		driver.findElement(By.xpath("(//a[contains(text(),'Edit')])[2]")).click();
		driver.findElement(By.id("menu-applinkDetailsPage")).click();
		Thread.sleep(3000);

		// Assert Application Name = "fisheye-stage"
		try {
			assertEquals("fisheye-stage", driver.findElement(By.id("applicationName")).getAttribute("value"));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		// OPEN Outgoing Authentication
		driver.findElement(By.id("menu-outgoing-authentication-page")).click();
		Thread.sleep(3000);

		// Switch to outer frame
		driver.switchTo().frame("outgoing-auth");
		// Switch to inner frame
		driver.switchTo().frame("trustedAppsAuthenticationProviderPluginModule");
		Thread.sleep(3000);

		// ** modify code from here **

		// Log in as Admin User
		driver.findElement(By.linkText("or log in as a user with Admin privileges")).click();

		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(data.getUserName());
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(data.getPassword());
		driver.findElement(By.cssSelector("button.aui-button.aui-button-primary")).click();

		Thread.sleep(5000);



		// OPEN OAuth tab

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("outgoing-auth");

		driver.findElement(By.cssSelector("#config-tab-1 > strong")).click();
		Thread.sleep(3000);

		// Switch to inner frame
		driver.switchTo().frame("OAuthAuthenticatorProviderPluginModule");

		Thread.sleep(3000);
		assertEquals("Not Configured", driver.findElement(By.cssSelector("span.field-value.status-not-configured")).getText());

		// OPEN Basic Access tab

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("outgoing-auth");

		driver.findElement(By.cssSelector("#config-tab-2 > strong")).click();

		Thread.sleep(3000);

		// Switch to inner iframe
		driver.switchTo().frame("BasicAuthenticationProviderPluginModule");

		//    driver.findElement(By.id("basicUsername")).clear();
		//    driver.findElement(By.id("basicUsername")).sendKeys(data.getUserName());
		//    driver.findElement(By.id("password1")).clear();
		//    driver.findElement(By.id("password1")).sendKeys(data.getPassword());
		//    driver.findElement(By.id("password2")).clear();
		//    driver.findElement(By.id("password2")).sendKeys(data.getPassword());
		//    driver.findElement(By.cssSelector("input.button")).click();
		//    Thread.sleep(5000);

		// Assert "Configured" appears after successful login
		assertEquals("Configured", driver.findElement(By.cssSelector("span.field-value.status-configured")).getText());

		// OPEN Incoming Authentication


		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Incoming Authentication
		driver.findElement(By.id("menu-incoming-authentication-page")).click();
		//driver.findElement(By.cssSelector("strong")).click();

		// switch to outer frame
		driver.switchTo().frame("incoming-auth");

		// switch to inner frame
		driver.switchTo().frame("trustedAppsAuthenticationProviderPluginModule");
		Thread.sleep(3000);


		assertEquals("Configured", driver.findElement(By.cssSelector("span.field-value.status-configured")).getText());

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("incoming-auth");

		// OAuth tab
		driver.findElement(By.cssSelector("#config-tab-1 > strong")).click();
		Thread.sleep(3000);

		// Switch to inner frame
		driver.switchTo().frame("OAuthAuthenticatorProviderPluginModule");

		Thread.sleep(3000);
		assertEquals("Not Configured", driver.findElement(By.cssSelector("span.field-value.status-not-configured")).getText());

		// Open Basic Access tab

		// Switch to default window (exit all iframes)
		driver.switchTo().defaultContent();

		// Switch to outer frame
		driver.switchTo().frame("incoming-auth");

		driver.findElement(By.cssSelector("#config-tab-2 > strong")).click();

		Thread.sleep(3000);

		// Switch to inner iframe
		driver.switchTo().frame("BasicAuthenticationProviderPluginModule");

		/*    driver.findElement(By.id("basicUsername")).clear();
    driver.findElement(By.id("basicUsername")).sendKeys(data.getUserName());
    driver.findElement(By.id("password1")).clear();
    driver.findElement(By.id("password1")).sendKeys(data.getPassword());
    driver.findElement(By.id("password2")).clear();
    driver.findElement(By.id("password2")).sendKeys(data.getPassword());
    driver.findElement(By.cssSelector("input.button")).click();*/
		Thread.sleep(5000);

		// Assert "Configured" appears after successful login
		assertEquals("Configured", driver.findElement(By.cssSelector("span.field-value.status-configured")).getText());

		Thread.sleep(4000);

		// Switch to default window (exit all frames)
		driver.switchTo().defaultContent();

		driver.findElement(By.linkText("Close")).click();


		/** FOR PROD TESTING
    driver.findElement(By.xpath("//a[@id='admin_menu']/span")).click();
    driver.findElement(By.id("admin_project_menu")).click();
    driver.findElement(By.id("view-project-10161")).click();
    assertEquals("https://fisheye.bsc.bscal.com/browse/edf/", driver.findElement(By.linkText("https://fisheye.bsc.bscal.com/browse/edf/")).getText());
		 */

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
