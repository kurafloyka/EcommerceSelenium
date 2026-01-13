package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Tüm Page Object sınıflarının extend edeceği base page sınıfı
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        PageFactory.initElements(driver, this);
    }

    /**
     * Element'in görünür olmasını bekler
     */
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Element'in tıklanabilir olmasını bekler
     */
    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Element'e tıklar
     */
    protected void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
        LoggerUtils.logInfo("Element tıklandı: " + element.toString());
    }

    /**
     * Element'e metin yazar
     */
    protected void sendKeysToElement(WebElement element, String text) {
        waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
        LoggerUtils.logInfo("Metin yazıldı: " + text);
    }

    /**
     * Element'in metnini alır (StaleElementReferenceException için retry mekanizması ile)
     */
    protected String getElementText(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                waitForElementToBeVisible(element);
                return element.getText();
            } catch (StaleElementReferenceException e) {
                attempts++;
                LoggerUtils.logWarn("StaleElementReferenceException yakalandı, tekrar deneniyor... (Deneme: " + attempts + ")");
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
        return element.getText();
    }

    /**
     * Sayfa başlığını alır
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Mevcut URL'i alır
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}

