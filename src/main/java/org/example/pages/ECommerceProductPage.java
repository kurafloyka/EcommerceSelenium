package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * E-ticaret ürün detay sayfası için Page Object Model sınıfı
 */
public class ECommerceProductPage extends BasePage {

    public ECommerceProductPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Beğen butonuna tıklar
     */
    public void clickLikeButton() {
        try {
            String likeButtonXpath = "//button[contains(@class,'like')] | //button[contains(@class,'favorite')] | //button[contains(@class,'wishlist')] | //a[contains(@class,'like')] | //a[contains(@class,'favorite')] | //span[contains(@class,'like')]";
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(likeButtonXpath)));
            WebElement likeButton = driver.findElement(By.xpath(likeButtonXpath));
            clickElement(likeButton);
            LoggerUtils.logInfo("Beğen butonuna tıklandı");
        } catch (Exception e) {
            LoggerUtils.logError("Beğen butonu bulunamadı veya tıklanamadı: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 'Ürün listenize eklendi' popup'ını kontrol eder
     */
    public boolean isProductAddedToWishlistPopupDisplayed() {
        try {
            String popupXpath = "//div[contains(text(),'Ürün listenize eklendi')] | //div[contains(text(),'listeye eklendi')] | //div[contains(text(),'beğendiklerime eklendi')] | //div[contains(@class,'success')] | //div[contains(@class,'notification')]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(popupXpath)));
            WebElement popup = driver.findElement(By.xpath(popupXpath));
            boolean isDisplayed = popup.isDisplayed();
            LoggerUtils.logInfo("Beğendiklerime ekleme popup'ı görüntülendi: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            LoggerUtils.logWarn("Beğendiklerime ekleme popup'ı bulunamadı: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sepete ekle butonuna tıklar
     */
    public void addToCart() {
        try {
            String addToCartXpath = "//button[contains(text(),'Sepete Ekle')] | //button[contains(text(),'Sepete ekle')] | //a[contains(text(),'Sepete Ekle')] | //button[contains(@class,'add-to-cart')] | //button[contains(@class,'add-cart')]";
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(addToCartXpath)));
            WebElement addToCartButton = driver.findElement(By.xpath(addToCartXpath));
            clickElement(addToCartButton);
            LoggerUtils.logInfo("Sepete ekle butonuna tıklandı");
        } catch (Exception e) {
            LoggerUtils.logError("Sepete ekle butonu bulunamadı: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 'Ürün sepete eklendi' popup'ını kontrol eder
     */
    public boolean isProductAddedToCartPopupDisplayed() {
        try {
            String popupXpath = "//div[contains(text(),'Ürün sepete eklendi')] | //div[contains(text(),'sepete eklendi')] | //div[contains(text(),'Sepete eklendi')] | //div[contains(@class,'success')] | //div[contains(@class,'notification')]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(popupXpath)));
            WebElement popup = driver.findElement(By.xpath(popupXpath));
            boolean isDisplayed = popup.isDisplayed();
            LoggerUtils.logInfo("Sepete ekleme popup'ı görüntülendi: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            LoggerUtils.logWarn("Sepete ekleme popup'ı bulunamadı: " + e.getMessage());
            return false;
        }
    }
}

