package tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ResolveIssueErrorMsg {
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

  @Test
  public void testResolveIssueErrorMessage() throws Exception {
    driver.get(baseUrl + "/secure/Dashboard.jspa");
    driver.findElement(By.cssSelector("img[alt=\"JIRA - Colo Stage\"]")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | gadget-0 | ]]
    driver.findElement(By.id("login-form-username")).clear();
    driver.findElement(By.id("login-form-username")).sendKeys("vgjoze01");
    driver.findElement(By.id("login-form-password")).clear();
    driver.findElement(By.id("login-form-password")).sendKeys("V!d@n12345");
    driver.findElement(By.id("login")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
    driver.findElement(By.id("quickSearchInput")).clear();
    driver.findElement(By.id("quickSearchInput")).sendKeys("WMB-8129");
    
    //driver.findElement(By.cssSelector("input.hidden")).click();
    
    driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
    
    driver.findElement(By.cssSelector("#action_id_5 > span.trigger-label")).click();
    new Select(driver.findElement(By.id("customfield_12654"))).selectByVisibleText("None");
    new Select(driver.findElement(By.id("customfield_12655"))).selectByVisibleText("None");
    new Select(driver.findElement(By.id("customfield_12656"))).selectByVisibleText("None");
    new Select(driver.findElement(By.id("customfield_12657"))).selectByVisibleText("None");
    new Select(driver.findElement(By.id("customfield_12658"))).selectByVisibleText("None");
    driver.findElement(By.id("issue-workflow-transition-submit")).click();
    driver.findElement(By.id("issue-workflow-transition-cancel")).click();
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
