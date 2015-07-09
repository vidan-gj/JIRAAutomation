package tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResolveDefectTicket {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  
  WebElement unameElement;
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
	baseUrl = "https://jira-stage.dev.bscal.local/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    //unameElement = (new WebDriverWait(driver,10)).until(ExpectedConditions.presenceOfElementLocated(By.id("login-form-username")));
  }

  @Test
  public void testResolveDefectTicket() throws Exception {
    driver.get(baseUrl + "/secure/Dashboard.jspa");
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | gadget-0 | ]]
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    
    driver.switchTo().frame("gadget-0");
    
    driver.findElement(By.id("login-form-username")).clear();
    driver.findElement(By.id("login-form-username")).sendKeys("vgjoze01");
    
    
    driver.findElement(By.id("login-form-password")).clear();
    driver.findElement(By.id("login-form-password")).sendKeys("V!d@n12345");
    driver.findElement(By.id("login")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
    driver.findElement(By.id("quickSearchInput")).click();
    driver.findElement(By.id("quickSearchInput")).clear();
    driver.findElement(By.id("quickSearchInput")).sendKeys("WMB-8128");
    
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    
    //driver.findElement(By.cssSelector("input.hidden")).click();
    
    driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
    
    driver.findElement(By.cssSelector("#action_id_5 > span.trigger-label")).click();
    driver.findElement(By.id("issue-workflow-transition-submit")).click();
    driver.findElement(By.xpath("//div[@id='fixVersions-multi-select']/span")).click();
    driver.findElement(By.linkText("RetrvProdForSubGroup-0.0")).click();
    new Select(driver.findElement(By.id("customfield_12654"))).selectByVisibleText("Checking");
    new Select(driver.findElement(By.id("customfield_12655"))).selectByVisibleText("Missing");
    new Select(driver.findElement(By.id("customfield_12656"))).selectByVisibleText("Code");
    new Select(driver.findElement(By.id("customfield_12657"))).selectByVisibleText("Outsourced");
    new Select(driver.findElement(By.id("customfield_12658"))).selectByVisibleText("Refixed");
    driver.findElement(By.xpath("(//textarea[@id='comment'])[2]")).clear();
    driver.findElement(By.xpath("(//textarea[@id='comment'])[2]")).sendKeys("Testing");
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
