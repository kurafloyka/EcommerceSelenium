package org.example.tests;

import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.example.base.BaseTest;
import org.example.pages.LoginPage;
import org.example.utils.ExcelUtils;
import org.example.utils.LoggerUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Login test senaryoları için test sınıfı
 * Page Object Model, TestNG, Assertion, Excel parametrik test ve raporlama örnekleri içerir
 */
@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Geçerli kullanıcı bilgileri ile login testi")
    @Description("Kullanıcı geçerli username ve password ile giriş yapabilmeli")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Successful Login")
    public void testSuccessfulLogin() {
        LoggerUtils.logInfo("Başarılı login testi başlatılıyor");
        extentTest.info("Başarılı login testi başlatılıyor");

        LoginPage loginPage = new LoginPage(driver);

        // Assertion: Login sayfasının görüntülendiğini kontrol et
        Assertions.assertThat(loginPage.isLoginPageDisplayed())
                .as("Login sayfası görüntülenmeli")
                .isTrue();

        extentTest.info("Login sayfası doğrulandı");

        // Login işlemi
        loginPage.login("tomsmith", "SuperSecretPassword!");

        // Assertion: Başarılı login sonrası welcome mesajının görüntülendiğini kontrol et
        String welcomeMessage = loginPage.getWelcomeMessage();
        Assertions.assertThat(welcomeMessage)
                .as("Welcome mesajı görüntülenmeli")
                .isNotNull()
                .isNotEmpty();

        extentTest.pass("Login başarılı - Welcome mesajı: " + welcomeMessage);
        LoggerUtils.logInfo("Test başarıyla tamamlandı");
    }

    @Test(priority = 2, description = "Geçersiz kullanıcı bilgileri ile login testi")
    @Description("Kullanıcı geçersiz username veya password ile giriş yapamamalı")
    @Severity(SeverityLevel.NORMAL)
    @Story("Failed Login")
    public void testFailedLogin() {
        LoggerUtils.logInfo("Başarısız login testi başlatılıyor");
        extentTest.info("Başarısız login testi başlatılıyor");

        LoginPage loginPage = new LoginPage(driver);

        // Assertion: Login sayfasının görüntülendiğini kontrol et
        Assertions.assertThat(loginPage.isLoginPageDisplayed())
                .as("Login sayfası görüntülenmeli")
                .isTrue();

        extentTest.info("Login sayfası doğrulandı");

        // Geçersiz bilgilerle login denemesi
        loginPage.login("invalid_user", "invalid_password");

        // Assertion: Hata mesajının görüntülendiğini kontrol et
        String flashMessage = loginPage.getFlashMessage();
        Assertions.assertThat(flashMessage)
                .as("Hata mesajı görüntülenmeli")
                .isNotNull()
                .contains("Your username is invalid!");

        extentTest.pass("Login başarısız - Hata mesajı doğrulandı: " + flashMessage);
        LoggerUtils.logInfo("Test başarıyla tamamlandı");
    }

    @Test(priority = 3, dataProvider = "loginData", description = "Excel'den parametrik login testi")
    @Description("Excel dosyasından okunan farklı kullanıcı bilgileri ile login testi")
    @Severity(SeverityLevel.NORMAL)
    @Story("Parametric Login")
    public void testParametricLogin(String username, String password, String expectedResult) {
        LoggerUtils.logInfo("Parametrik login testi başlatılıyor - Username: " + username);
        extentTest.info("Parametrik login testi - Username: " + username + ", Expected: " + expectedResult);

        LoginPage loginPage = new LoginPage(driver);

        // Assertion: Login sayfasının görüntülendiğini kontrol et
        Assertions.assertThat(loginPage.isLoginPageDisplayed())
                .as("Login sayfası görüntülenmeli")
                .isTrue();

        // Login işlemi
        loginPage.login(username, password);

        if ("SUCCESS".equals(expectedResult)) {
            // Başarılı login kontrolü
            String welcomeMessage = loginPage.getWelcomeMessage();
            Assertions.assertThat(welcomeMessage)
                    .as("Welcome mesajı görüntülenmeli")
                    .isNotNull()
                    .isNotEmpty();
            extentTest.pass("Login başarılı - Welcome mesajı: " + welcomeMessage);
        } else {
            // Başarısız login kontrolü
            String flashMessage = loginPage.getFlashMessage();
            Assertions.assertThat(flashMessage)
                    .as("Hata mesajı görüntülenmeli")
                    .isNotNull()
                    .isNotEmpty();
            extentTest.pass("Login başarısız - Hata mesajı: " + flashMessage);
        }

        LoggerUtils.logInfo("Parametrik test tamamlandı");
    }

    /**
     * Excel dosyasından test verilerini okur ve DataProvider olarak döner
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        List<Map<String, String>> testData = ExcelUtils.readTestData("LoginData");
        Object[][] data = new Object[testData.size()][3];

        for (int i = 0; i < testData.size(); i++) {
            Map<String, String> row = testData.get(i);
            data[i][0] = row.get("Username");
            data[i][1] = row.get("Password");
            data[i][2] = row.get("ExpectedResult");
        }

        return data;
    }
}

