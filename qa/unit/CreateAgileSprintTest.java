package unit;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import data.ExcelUtility;
import data.ReadPropertiesFile;


public class CreateAgileSprintTest {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private static Logger logger = Logger.getLogger(CreateAgileSprintTest.class);

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
	public void testCreateAgileSprint() throws Exception {
		logger.info("---------------------------- TEST START ------------------------------------");
		logger.info("***************************** CreateAgileSprintTest ********************************");
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

		String ticketNumber = ExcelUtility.getCellData(16, 1);

		String ticketNumber2 = ExcelUtility.getCellData(16, 2);

		String boardName = ExcelUtility.getCellData(16, 6); 


		// Agile drop-down
		driver.findElement(By.id("greenhopper_menu")).click();
		Thread.sleep(1000);

		// ...more
		driver.findElement(By.id("ghx-manageviews-mlink_lnk")).click();
		Thread.sleep(3000);

		// Select desired Board (declared on top)
		driver.findElement(By.linkText(boardName)).click();
		Thread.sleep(3000);

		driver.findElement(By.id("plan-toggle")).click();
		Thread.sleep(3000);

		// Create New Sprint
		driver.findElement(By.cssSelector("button.js-add-sprint.aui-button")).click();
		Thread.sleep(3000);

		// Drag and Drop logic:
		// Ticket 1 move:

		WebElement ticket1 = driver.findElement(By.linkText(ticketNumber));

		WebElement location1 = driver.findElement(By.cssSelector("div.ghx-helper.ghx-description"));

		Actions builder = new Actions(driver);

		Action dragAndDrop = builder.clickAndHold(ticket1).moveToElement(location1).release(location1).build();

		dragAndDrop.perform();    
		Thread.sleep(3000);

		// Ticket2 move:

		/* WebElement ticket2 = driver.findElement(By.linkText(ticketNumber2));

    Actions builder2 = new Actions(driver);

    Action dragAndDrop2 = builder2.clickAndHold(ticket2).moveToElement(location1).release(location1).build();

    dragAndDrop2.perform();
    Thread.sleep(3000);
		 */

		// right click -> Add to new Sprint
		/*  driver.findElement(By.id("ghx-issue-ctx-action-send-to-sprint-1423")).click();
    driver.findElement(By.id("ghx-issue-ctx-action-send-to-sprint-1423")).click();
		 */

		// Start Sprint
		driver.findElement(By.linkText("Start Sprint")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("button.button-panel-button.aui-button")).click();
		Thread.sleep(3000);

		// Complete Sprint
		driver.findElement(By.cssSelector("span.ghx-icon.ghx-icon-drop")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("ghx-complete-sprint")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("button.button-panel-button.aui-button")).click();
		Thread.sleep(3000);

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
