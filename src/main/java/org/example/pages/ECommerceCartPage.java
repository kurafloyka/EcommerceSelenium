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
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], tr[class*='product'], div[class*='cart-item']")));
            List<WebElement> products = driver.findElements(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], tr[class*='product'], div[class*='cart-item']"));
            boolean hasProducts = products.size() > 0;
            LoggerUtils.logInfo("Sepette ürün var: " + hasProducts + " (Toplam: " + products.size() + ")");
            return hasProducts;
        } catch (Exception e) {
            LoggerUtils.logWarn("Sepet kontrolü yapılamadı: " + e.getMessage());
            return false;
        }
    }

    /**
     * İlk ürünü seçer ve kaldır butonuna tıklar
     */
    public void removeFirstProduct() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], tr[class*='product'], div[class*='cart-item']")));
            List<WebElement> products = driver.findElements(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], tr[class*='product'], div[class*='cart-item']"));
            
            if (products.size() > 0) {
                WebElement firstProduct = products.get(0);
                String removeButtonXpath = ".//button[contains(text(),'Kaldır')] | .//button[contains(text(),'Sil')] | .//a[contains(text(),'Kaldır')] | .//a[contains(text(),'Sil')] | .//button[contains(@class,'remove')] | .//button[contains(@class,'delete')]";
                WebElement removeButton = firstProduct.findElement(By.xpath(removeButtonXpath));
                clickElement(removeButton);
                LoggerUtils.logInfo("İlk ürün sepetten kaldırıldı");
                
                // Onay popup'ı için bekle
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LoggerUtils.logWarn("Thread sleep interrupted");
                }
            } else {
                throw new RuntimeException("Sepette ürün bulunamadı");
            }
        } catch (Exception e) {
            LoggerUtils.logError("Ürün sepetten kaldırılamadı: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Sepetin boş olduğunu kontrol eder
     */
    public boolean isCartEmpty() {
        try {
            try {
                Thread.sleep(2000); // Sayfa güncellemesi için bekle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggerUtils.logWarn("Thread sleep interrupted");
            }
            
            // Sepet boş mesajı veya ürün yoksa
            String emptyCartXpath = "//div[contains(text(),'Sepetiniz boş')] | //div[contains(text(),'sepetiniz boş')] | //div[contains(text(),'ürün bulunmamaktadır')]";
            try {
                WebElement emptyMessage = driver.findElement(By.xpath(emptyCartXpath));
                if (emptyMessage.isDisplayed()) {
                    LoggerUtils.logInfo("Sepet boş mesajı görüntülendi");
                    return true;
                }
            } catch (Exception e) {
                // Mesaj bulunamadı, ürün listesini kontrol et
            }
            
            List<WebElement> products = driver.findElements(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], tr[class*='product'], div[class*='cart-item']"));
            boolean isEmpty = products.size() == 0;
            LoggerUtils.logInfo("Sepet boş: " + isEmpty);
            return isEmpty;
        } catch (Exception e) {
            LoggerUtils.logWarn("Sepet boş kontrolü yapılamadı: " + e.getMessage());
            return false;
        }
    }
}

