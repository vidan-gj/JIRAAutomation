package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ReadPropertiesFile {

	public WebDriver driver;
	protected Properties prop = null;
	
	//protected File file = new File("D:/ApplicationData/Hudson/jobs/SeleniumAutomation-JiraFunctionalSuite/config.properties");
	//protected File file = new File("C:/Users/vgjoze01/workspace/JIRAAutomation(IDE-JUnit)/qa/data/config.properties");   <-- old way
	
	/*
	 * This approach of relative file loading will work if the file is located within the same package.
	 * Note: If you want to access it from inside static context, then use FileLoader.class instead of getClass().
	 */
	protected URL url = getClass().getResource("config.properties");
	protected File file = new File(url.getPath());
	protected final String path = "C:/Users/vgjoze01/Downloads/chromedriver_win32/chromedriver.exe";
	
	protected FileInputStream fileInput = null;
	
	public ReadPropertiesFile()  {
		prop = new Properties();
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public WebDriver getBrowser() {
		System.setProperty("webdriver.chrome.driver", path);
		return driver = new ChromeDriver();
		//return prop.getProperty("browser");
	}
	
	public String getChromeDriverPath() {
		return path;
	}
		
	public String getUrl() {
		return prop.getProperty("url");
	}
	
	public String getUrlProd() {
		return prop.getProperty("urlProd");
	}
		
	public String getUserName() {
		return prop.getProperty("username");
	}
	
	public String getPassword() {
		return prop.getProperty("password");
	}

	public String getTestData() {
		return prop.getProperty("testData");
	}
	
	public String getSheetName() {
		return prop.getProperty("sheetEnv");
	}
}
