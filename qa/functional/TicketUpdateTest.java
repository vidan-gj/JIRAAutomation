package functional;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.reporters.jq.TimesPanel;

import com.thoughtworks.selenium.Wait;

import data.ReadPropertiesFile;

public class TicketUpdateTest {
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
  public void testTicketUpdate() throws Exception {
    driver.get(baseUrl + "/secure/Dashboard.jspa");
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | gadget-0 | ]]
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    
    driver.switchTo().frame("gadget-0");
    
    driver.findElement(By.id("login-form-username")).clear();
    driver.findElement(By.id("login-form-username")).sendKeys(data.getUserName());
    
    driver.findElement(By.id("login-form-password")).clear();
    driver.findElement(By.id("login-form-password")).sendKeys(data.getPassword());
    
    driver.findElement(By.id("login")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
    
    driver.findElement(By.id("quickSearchInput")).clear();
    driver.findElement(By.id("quickSearchInput")).sendKeys("ERM-1392");
    
 
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    
    /* **DEPRICATED**
    WebDriverWait wait = new WebDriverWait(driver, 5);
    WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.hidden")));
    searchButton.click();
 
    searchButton.sendKeys(Keys.ENTER); */
    
    // using same element, sending "ENTER" Key instead of click method
    driver.findElement(By.id("quickSearchInput")).sendKeys(Keys.ENTER);
    
    driver.findElement(By.cssSelector("#assign-issue > span.trigger-label")).click();
    driver.findElement(By.id("assignee-field")).sendKeys("Mahalingam, Kesava");
    driver.findElement(By.cssSelector("a.aui-list-item-link.aui-iconised-link > span")).click();
    driver.findElement(By.id("assign-issue-submit")).click();
    driver.findElement(By.xpath("//a[@id='footer-comment-button']/span[2]")).click();
    driver.findElement(By.id("comment")).clear();
    driver.findElement(By.id("comment")).sendKeys("Testing5");
    driver.findElement(By.id("issue-comment-add-submit")).click();
    driver.findElement(By.cssSelector("#changehistory-tabpanel > strong")).click();
    driver.findElement(By.id("assign-to-me")).click();
    driver.findElement(By.cssSelector("img[alt=\"Gjozev, Vidan\"]")).click();
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
