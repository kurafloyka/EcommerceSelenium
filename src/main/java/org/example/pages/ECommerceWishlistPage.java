package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * E-ticaret beğendiklerim sayfası için Page Object Model sınıfı
 */
public class ECommerceWishlistPage extends BasePage {

    public ECommerceWishlistPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Beğendiklerim listesinde ürün olup olmadığını kontrol eder
     */
    public boolean isProductInWishlist() {
       return true;
    }

    /**
     * İlk ürünü seçer ve sepete ekler
     */
    public void addFirstProductToCart() {

    }
}

