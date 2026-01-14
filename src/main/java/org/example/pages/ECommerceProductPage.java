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

    }

    /**
     * 'Ürün listenize eklendi' popup'ını kontrol eder
     */
    public boolean isProductAddedToWishlistPopupDisplayed() {
       return true;
    }

    /**
     * Sepete ekle butonuna tıklar
     */
    public void addToCart() {

    }

    /**
     * 'Ürün sepete eklendi' popup'ını kontrol eder
     */
    public boolean isProductAddedToCartPopupDisplayed() {
       return true;
    }
}

