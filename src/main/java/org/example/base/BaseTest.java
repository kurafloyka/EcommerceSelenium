package org.example.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.utils.ConfigReader;
import org.example.utils.ExcelUtils;
import org.example.utils.LoggerUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Tüm test sınıflarının extend edeceği base test sınıfı
 * WebDriver yönetimi, Extent Reports ve Allure entegrasyonu burada yapılır
 */
public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extentReports;
    protected ExtentTest extentTest;
    private long testStartTime;
    private String testName;

    @BeforeSuite
    public void setUpSuite() {
        LoggerUtils.logInfo("Test Suite başlatılıyor...");
        
        // Extent Reports yapılandırması
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        sparkReporter.config().setDocumentTitle("Selenium Test Raporu");
        sparkReporter.config().setReportName("Test Execution Report");
        
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
        extentReports.setSystemInfo("Base URL", ConfigReader.getBaseUrl());
    }

    @BeforeMethod
    public void setUp(ITestResult result) {
        testName = result.getMethod().getMethodName();
        testStartTime = System.currentTimeMillis();
        
        LoggerUtils.logInfo("Test başlatılıyor: " + testName);
        
        // Extent Test oluştur
        extentTest = extentReports.createTest(testName);
        
        // WebDriver başlat
        initializeDriver();
        
        // WebDriver ayarları
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        
        // Base URL'e git
        driver.get(ConfigReader.getBaseUrl());
        
        extentTest.info("Browser başlatıldı: " + ConfigReader.getBrowser());
        extentTest.info("URL açıldı: " + ConfigReader.getBaseUrl());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        long executionTime = System.currentTimeMillis() - testStartTime;
        String status = result.getStatus() == ITestResult.SUCCESS ? "PASS" : "FAIL";
        String errorMessage = null;

        if (result.getStatus() == ITestResult.FAILURE) {
            errorMessage = result.getThrowable().getMessage();
            extentTest.fail("Test başarısız: " + errorMessage);
            LoggerUtils.logError("Test başarısız: " + testName + " - " + errorMessage);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.pass("Test başarılı");
            LoggerUtils.logInfo("Test başarılı: " + testName);
        } else {
            extentTest.skip("Test atlandı");
            LoggerUtils.logWarn("Test atlandı: " + testName);
        }

        // Test sonucunu Excel'e yaz
        ExcelUtils.writeTestResult(testName, status, executionTime, errorMessage);

        // WebDriver'ı kapat
        if (driver != null) {
            try {
                driver.quit();
                LoggerUtils.logInfo("Browser kapatıldı");
            } catch (Exception e) {
                // quit() exception fırlattığında genellikle driver zaten kapatılmıştır
                // Exception genellikle output stream kapatılırken veya server timeout'unda oluşur
                String errorMsg = e.getMessage();
                if (errorMsg != null) {
                    if (errorMsg.contains("Session ID is null")) {
                        LoggerUtils.logInfo("Browser zaten kapatılmış (Session ID null)");
                    } else if (errorMsg.contains("Timed out waiting for driver server to stop")) {
                        // Driver server timeout - driver genellikle zaten kapatılmıştır
                        LoggerUtils.logInfo("Browser kapatıldı (driver server timeout - normal)");
                    } else if (errorMsg.contains("Output closed")) {
                        // Output stream kapatıldı - driver zaten kapatılmıştır
                        LoggerUtils.logInfo("Browser kapatıldı (output stream closed - normal)");
                    } else {
                        LoggerUtils.logWarn("Browser kapatılırken hata oluştu (driver muhtemelen zaten kapatıldı): " + errorMsg);
                    }
                } else {
                    LoggerUtils.logWarn("Browser kapatılırken bilinmeyen bir hata oluştu");
                }
            } finally {
                driver = null;
            }
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        LoggerUtils.logInfo("Test Suite tamamlandı");
        extentReports.flush();
    }

    /**
     * Browser tipine göre WebDriver başlatır
     */
    private void initializeDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                
                // Cookie ayarları - Cookie'leri otomatik kabul et
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.cookies", 1); // 1 = Allow all cookies
                prefs.put("profile.block_third_party_cookies", false);
                prefs.put("profile.cookie_controls_mode", 0); // 0 = Allow all cookies
                chromeOptions.setExperimentalOption("prefs", prefs);
                
                // Cookie consent bypass için ek argümanlar
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--allow-running-insecure-content");
                chromeOptions.addArguments("--disable-features=CookieDeprecationLabels");
                
                // User agent ayarı (bot algılamasını azaltmak için)
                chromeOptions.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                
                driver = new ChromeDriver(chromeOptions);
                LoggerUtils.logInfo("Chrome driver başlatıldı - Cookie ayarları aktif");
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions);
                break;
            default:
                throw new IllegalArgumentException("Desteklenmeyen browser: " + browser);
        }
        
        LoggerUtils.logInfo("WebDriver başlatıldı: " + browser);
    }
}

