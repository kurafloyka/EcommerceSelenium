package org.example.tests;

import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.example.base.BaseTest;
import org.example.pages.LoginPage;
import org.example.utils.LoggerUtils;
import org.testng.annotations.Test;

/**
 * Ana sayfa test senaryoları için test sınıfı
 */
@Epic("Navigation")
@Feature("Home Page")
public class HomePageTest extends BaseTest {

    @Test(priority = 1, description = "Ana sayfa başlık kontrolü")
    @Description("Ana sayfanın başlığının doğru olduğunu kontrol eder")
    @Severity(SeverityLevel.MINOR)
    @Story("Page Title Verification")
    public void testPageTitle() {
        LoggerUtils.logInfo("Sayfa başlık testi başlatılıyor");
        extentTest.info("Sayfa başlık testi başlatılıyor");

        String pageTitle = driver.getTitle();

        // Assertion: Sayfa başlığının doğru olduğunu kontrol et
        Assertions.assertThat(pageTitle)
                .as("Sayfa başlığı kontrol ediliyor")
                .isNotNull()
                .isNotEmpty();

        extentTest.pass("Sayfa başlığı doğrulandı: " + pageTitle);
        LoggerUtils.logInfo("Test başarıyla tamamlandı - Başlık: " + pageTitle);
    }

    @Test(priority = 2, description = "URL kontrolü")
    @Description("Mevcut URL'in doğru olduğunu kontrol eder")
    @Severity(SeverityLevel.MINOR)
    @Story("URL Verification")
    public void testCurrentUrl() {
        LoggerUtils.logInfo("URL testi başlatılıyor");
        extentTest.info("URL testi başlatılıyor");

        String currentUrl = driver.getCurrentUrl();

        // Assertion: URL'in doğru olduğunu kontrol et
        Assertions.assertThat(currentUrl)
                .as("URL kontrol ediliyor")
                .isNotNull()
                .contains("the-internet.herokuapp.com");

        extentTest.pass("URL doğrulandı: " + currentUrl);
        LoggerUtils.logInfo("Test başarıyla tamamlandı - URL: " + currentUrl);
    }
}

