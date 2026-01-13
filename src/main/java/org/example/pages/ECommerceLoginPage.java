package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * E-ticaret login sayfası için Page Object Model sınıfı
 */
public class ECommerceLoginPage extends BasePage {

    @FindBy(id = "login-email")
    private WebElement emailInput;

    @FindBy(id = "login-password-input")
    private WebElement passwordInput;

    @FindBy(css = "button[class='q-primary q-fluid q-button-medium q-button submit']")
    private WebElement loginButton;

    public ECommerceLoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Email ve şifre ile giriş yapar
     */
    public void login(String email, String password) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(emailInput));
            sendKeysToElement(emailInput, email);

            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            clickElement(loginButton);

            wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
            sendKeysToElement(passwordInput, password);

            //wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            clickElement(loginButton);
            LoggerUtils.logInfo("Login işlemi tamamlandı");
        } catch (Exception e) {
            LoggerUtils.logError("Login işlemi başarısız: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Login sayfasının görüntülendiğini kontrol eder
     */
    public boolean isLoginPageDisplayed() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(emailInput));

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

