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
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']")));
            List<WebElement> products = driver.findElements(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']"));
            boolean hasProducts = products.size() > 0;
            LoggerUtils.logInfo("Beğendiklerim listesinde ürün var: " + hasProducts + " (Toplam: " + products.size() + ")");
            return hasProducts;
        } catch (Exception e) {
            LoggerUtils.logWarn("Beğendiklerim listesi kontrol edilemedi: " + e.getMessage());
            return false;
        }
    }

    /**
     * İlk ürünü seçer ve sepete ekler
     */
    public void addFirstProductToCart() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']")));
            List<WebElement> products = driver.findElements(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']"));
            
            if (products.size() > 0) {
                WebElement firstProduct = products.get(0);
                String addToCartXpath = ".//button[contains(text(),'Sepete Ekle')] | .//button[contains(text(),'Sepete ekle')] | .//a[contains(text(),'Sepete Ekle')] | .//button[contains(@class,'add-to-cart')]";
                WebElement addToCartButton = firstProduct.findElement(By.xpath(addToCartXpath));
                clickElement(addToCartButton);
                LoggerUtils.logInfo("İlk ürün sepete eklendi");
            } else {
                throw new RuntimeException("Beğendiklerim listesinde ürün bulunamadı");
            }
        } catch (Exception e) {
            LoggerUtils.logError("Ürün sepete eklenemedi: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

