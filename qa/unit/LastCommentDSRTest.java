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

public class LastCommentDSRTest {
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
	public void testLastCommentDSR() throws Exception {
		driver.get(data.getUrl() + "/secure/Dashboard.jspa");

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Log in
		//driver.switchTo().frame("gadget-0");

		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
		driver.findElement(By.id("login")).click();
		Thread.sleep(5000);

		// | ---------------------------------------- EXCEL LOGIC: ------------------------------------------------ |

		ExcelUtility.setExcelFile(data.getTestData(), data.getSheetName());

		String ticketNumber = ExcelUtility.getCellData(15, 1); 	// pull ticket number from Excel data sheet: Stage

		System.out.println("ticket: " + ticketNumber);


		// Search ticket
		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);
		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);


		// TESTING:
		//  Actions action = new Actions(driver);
		//  WebElement wb = driver.findElement(By.id("commentauthor_526871_verbose")); 

		//action.moveToElement(wb).moveToElement(driver.findElement(By.id("delete_comment_526869"))).click().build().perform(); // <== GOOD

		// action.moveToElement(wb).moveToElement(driver.findElement(By.id("//*[starts-with(@id, 'delete_comment_')]"))).click().build().perform();

		// action.moveToElement(wb).moveToElement(driver.findElement(By.xpath("xpath=(//*[contains(@id, 'delete_comment_')])"))).click().build().perform();

		//Thread.sleep(4000);

		//driver.findElement(By.id("comment-delete-submit")).click(); 
		//Thread.sleep(4000);


		// Add a comment
		driver.findElement(By.xpath("//a[@id='footer-comment-button']/span[2]")).click();

		driver.findElement(By.id("comment")).clear();
		Thread.sleep(5000);    

		// generate a random number
		int rand = 0;
		for (int i = 0; i < 10; i++) {
			rand = (int) (Math.round(Math.random() * 8999) + 10000);
		}

		String randTest = "Test" + rand; 

		driver.findElement(By.id("comment")).sendKeys(randTest);

		driver.findElement(By.id("issue-comment-add-submit")).click();
		Thread.sleep(4000);


		// Issues > Search for Issues
		driver.findElement(By.id("find_link")).click();

		driver.findElement(By.id("issues_new_search_link_lnk")).click();
		Thread.sleep(3000);

		// Search DSR tickets
		driver.findElement(By.id("advanced-search")).clear();

		driver.findElement(By.id("advanced-search")).sendKeys("project = DSR and issuetype = \"Toolset and Automation Support\"");

		driver.findElement(By.id("advanced-search")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);

		// Columns drop-down
		driver.findElement(By.xpath("//section[@id='content']/div/div[4]/div/div/div/div/div/div/div/div[2]/div/button")).click();
		Thread.sleep(3000);

		// ??? neded??
		//driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();


		// Select Last Comment to View from list
		driver.findElement(By.id("user-column-sparkler-input")).clear();

		driver.findElement(By.id("user-column-sparkler-input")).sendKeys("Last Comments");
		Thread.sleep(3000);

		// select Last Comments check box
		driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();
		Thread.sleep(5000);

		// Done
		driver.findElement(By.cssSelector("input.aui-button.submit")).click();
		Thread.sleep(7000);

		// Verify comment is displayed
		//assertEquals("Gjozev, Vidan - 11/Feb/15 03:50 PM Test\n Gjozev, Vidan - 11/Feb/15 03:46 PM test1", driver.findElement(By.cssSelector("td.customfield_13850 > p")).getText());

		// capture complete comment
		String lastComment = driver.findElement(By.cssSelector("td.customfield_13850 > p")).getText();


		// Check if Last comment is in the String: if boolean = true -> PASS, if boolean = false -> FAIL
		boolean containsFile = lastComment.contains(randTest);

		// System.out.print("Last Comment Complete String: " + lastComment);

		//int intIndex = lastComment.indexOf(randTest);

		//System.out.print("intIndex of comment in string is: " + intIndex);

		// store it in an array with regex (split by space)
		//  String[] commentArray = lastComment.split(" ");

		// Logic to lookup the Last comment in the array
		/*  int index = -1;
    for (int i = 0; i < commentArray.length; i++) {
    	if (commentArray[i].equals(randTest)) {
    		index = i;
    		break;
    	}
    }*/

		//System.out.println("Last Comment is: " + commentArray[index]);
		Thread.sleep(3000);

		// *** REVERT CHANGES ***

		// Columns drop-down
		driver.findElement(By.xpath("//section[@id='content']/div/div[4]/div/div/div/div/div/div/div/div[2]/div/button")).click();
		Thread.sleep(3000);

		// Select Last Comment to View from list
		driver.findElement(By.id("user-column-sparkler-input")).clear();
		driver.findElement(By.id("user-column-sparkler-input")).sendKeys("Last Comments");
		Thread.sleep(3000);

		// select Last Comments check box
		driver.findElement(By.cssSelector("input[type=\"checkbox\"]")).click();
		Thread.sleep(5000);

		// Done
		driver.findElement(By.cssSelector("input.aui-button.submit")).click();
		Thread.sleep(4000);

		// Search ticket
		driver.findElement(By.id("quickSearchInput")).clear();
		driver.findElement(By.id("quickSearchInput")).sendKeys(ticketNumber);
		driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);

		// Delete the comment

		/*  Actions action2 = new Actions(driver);
    WebElement wb2 = driver.findElement(By.id("commentauthor_526873_verbose")); 

    action2.moveToElement(wb2).moveToElement(driver.findElement(By.id("delete_comment_526873"))).click().build().perform();

    Thread.sleep(4000);
    driver.findElement(By.id("comment-delete-submit")).click(); 
    Thread.sleep(4000);
		 */






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
