package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Login sayfası için Page Object Model sınıfı
 */
public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(id = "flash")
    private WebElement flashMessage;

    @FindBy(css = ".subheader")
    private WebElement welcomeMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Kullanıcı adı ve şifre ile giriş yapar
     */
    public void login(String username, String password) {
        LoggerUtils.logInfo("Login işlemi başlatılıyor - Username: " + username);
        sendKeysToElement(usernameInput, username);
        sendKeysToElement(passwordInput, password);
        clickElement(loginButton);
        LoggerUtils.logInfo("Login butonu tıklandı");
    }

    /**
     * Flash mesajını alır (StaleElementReferenceException için güvenli)
     */
    public String getFlashMessage() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash")));
                WebElement element = driver.findElement(By.id("flash"));
                return element.getText();
            } catch (StaleElementReferenceException e) {
                attempts++;
                LoggerUtils.logWarn("Flash mesajı alınırken StaleElementReferenceException, tekrar deneniyor... (Deneme: " + attempts + ")");
                if (attempts >= 3) {
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return "";
    }

    /**
     * Welcome mesajını alır (StaleElementReferenceException için güvenli)
     */
    public String getWelcomeMessage() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".subheader")));
                WebElement element = driver.findElement(By.cssSelector(".subheader"));
                return element.getText();
            } catch (StaleElementReferenceException e) {
                attempts++;
                LoggerUtils.logWarn("Welcome mesajı alınırken StaleElementReferenceException, tekrar deneniyor... (Deneme: " + attempts + ")");
                if (attempts >= 3) {
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return "";
    }

    /**
     * Login sayfasında olup olmadığını kontrol eder
     */
    public boolean isLoginPageDisplayed() {
        try {
            waitForElementToBeVisible(usernameInput);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

