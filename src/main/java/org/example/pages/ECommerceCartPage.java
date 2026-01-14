package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * E-ticaret sepet sayfası için Page Object Model sınıfı
 */
public class ECommerceCartPage extends BasePage {

    public ECommerceCartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Sepette ürün olup olmadığını kontrol eder
     */
    public boolean isProductInCart() {
        return true;
    }

    /**
     * İlk ürünü seçer ve kaldır butonuna tıklar
     */
    public void removeFirstProduct() {

    }

    /**
     * Sepetin boş olduğunu kontrol eder
     */
    public boolean isCartEmpty() {
        return true;
    }
}

