package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * E-ticaret arama sonuçları sayfası için Page Object Model sınıfı
 */
public class ECommerceSearchPage extends BasePage {

    public ECommerceSearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Sol menüden kategori seçer (Telefon > Cep Telefonu)
     */
    public void selectCategory(String mainCategory, String subCategory) {
        try {
            // Ana kategoriyi bul ve tıkla
            String mainCategoryXpath = "//a[contains(text(),'" + mainCategory + "')] | //span[contains(text(),'" + mainCategory + "')]";
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mainCategoryXpath)));
            WebElement mainCat = driver.findElement(By.xpath(mainCategoryXpath));
            clickElement(mainCat);
            LoggerUtils.logInfo("Ana kategori seçildi: " + mainCategory);
            
            try {
                Thread.sleep(1000); // Alt kategori için bekle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggerUtils.logWarn("Thread sleep interrupted");
            }
            
            // Alt kategoriyi bul ve tıkla
            String subCategoryXpath = "//a[contains(text(),'" + subCategory + "')] | //span[contains(text(),'" + subCategory + "')]";
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(subCategoryXpath)));
            WebElement subCat = driver.findElement(By.xpath(subCategoryXpath));
            clickElement(subCat);
            LoggerUtils.logInfo("Alt kategori seçildi: " + subCategory);
        } catch (Exception e) {
            LoggerUtils.logError("Kategori seçimi başarısız: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Arama sonuçlarının görüntülendiğini kontrol eder
     */
    public boolean areSearchResultsDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']")));
            List<WebElement> products = driver.findElements(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']"));
            boolean hasResults = products.size() > 0;
            LoggerUtils.logInfo("Arama sonuçları bulundu: " + products.size() + " ürün");
            return hasResults;
        } catch (Exception e) {
            LoggerUtils.logError("Arama sonuçları bulunamadı: " + e.getMessage());
            return false;
        }
    }

    /**
     * Belirtilen sayfa numarasına tıklar (örn: 2. sayfa)
     */
    public void goToPage(int pageNumber) {
        try {
            String pageXpath = "//a[contains(text(),'" + pageNumber + "')] | //a[@data-page='" + pageNumber + "'] | //button[contains(text(),'" + pageNumber + "')]";
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(pageXpath)));
            WebElement pageLink = driver.findElement(By.xpath(pageXpath));
            clickElement(pageLink);
            LoggerUtils.logInfo(pageNumber + ". sayfaya gidildi");
        } catch (Exception e) {
            LoggerUtils.logError(pageNumber + ". sayfaya gidilemedi: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Belirtilen sayfanın aktif olduğunu kontrol eder
     */
    public boolean isPageActive(int pageNumber) {
        try {
            String activePageXpath = "//a[contains(@class,'active') and contains(text(),'" + pageNumber + "')] | //span[contains(@class,'active') and contains(text(),'" + pageNumber + "')] | //li[contains(@class,'active')]//a[contains(text(),'" + pageNumber + "')]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(activePageXpath)));
            WebElement activePage = driver.findElement(By.xpath(activePageXpath));
            boolean isActive = activePage.isDisplayed();
            LoggerUtils.logInfo(pageNumber + ". sayfa aktif: " + isActive);
            return isActive;
        } catch (Exception e) {
            LoggerUtils.logWarn(pageNumber + ". sayfa aktif kontrolü başarısız: " + e.getMessage());
            return false;
        }
    }

    /**
     * Belirtilen sıradaki ürünü tıklar (örn: 5. ürün)
     */
    public void clickProductByIndex(int index) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']")));
            List<WebElement> products = driver.findElements(By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product']"));
            
            if (products.size() >= index) {
                WebElement product = products.get(index - 1); // Index 0'dan başlar
                WebElement productLink = product.findElement(By.cssSelector("a, h3 a, h2 a, .title a"));
                clickElement(productLink);
                LoggerUtils.logInfo(index + ". ürün tıklandı");
            } else {
                throw new RuntimeException("Yeterli ürün bulunamadı. Toplam ürün: " + products.size() + ", İstenen: " + index);
            }
        } catch (Exception e) {
            LoggerUtils.logError(index + ". ürün tıklanamadı: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

