package tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ResolveERMComplete {
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
  public void testResolveERMComplete() throws Exception {
    driver.get(baseUrl + "/secure/Dashboard.jspa");
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | gadget-0 | ]]
    driver.findElement(By.id("login-form-username")).clear();
    driver.findElement(By.id("login-form-username")).sendKeys("vgjoze01");
    driver.findElement(By.id("login-form-password")).clear();
    driver.findElement(By.id("login-form-password")).sendKeys("V!d@n12345");
    driver.findElement(By.id("login")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
    driver.findElement(By.id("quickSearchInput")).click();
    driver.findElement(By.id("quickSearchInput")).clear();
    driver.findElement(By.id("quickSearchInput")).sendKeys("ERM-1388");
    
    //driver.findElement(By.cssSelector("input.hidden")).click();
    
    driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
    
    driver.findElement(By.cssSelector("#action_id_21 > span.trigger-label")).click();
    driver.findElement(By.id("issue-workflow-transition-submit")).click();
    driver.findElement(By.id("issue-workflow-transition-cancel")).click();
    driver.findElement(By.linkText("WMB-Test")).click();
    driver.findElement(By.cssSelector("li[title=\"Test3 - Please Ignore\"] > a.splitview-issue-link > span.issue-link-key")).click();
    driver.findElement(By.cssSelector("#action_id_5 > span.trigger-label")).click();
    driver.findElement(By.id("issue-workflow-transition-submit")).click();
    driver.findElement(By.linkText("ERM-1388")).click();
    driver.findElement(By.cssSelector("#action_id_21 > span.trigger-label")).click();
    driver.findElement(By.id("issue-workflow-transition-submit")).click();
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
