package functional;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import data.ReadPropertiesFile;

public class UserLoginTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://jira-stage.dev.bscal.local/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  ReadPropertiesFile data = new ReadPropertiesFile();
  
  @Test
  public void testUserLogin() throws Exception {
	
	
    driver.get(baseUrl + "/secure/Dashboard.jspa");
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | gadget-0 | ]]
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    
    driver.switchTo().frame("gadget-0");
    
    driver.findElement(By.id("login-form-username")).clear();
    driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
    
    driver.findElement(By.id("login-form-password")).clear();
    driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
    
    driver.findElement(By.id("login")).click();
    
    Assert.assertEquals(driver.getTitle(), "SCRM Tools & Automation - JIRA - Colo Stage");
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
