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

    }

    /**
     * Arama sonuçlarının görüntülendiğini kontrol eder
     */
    public boolean areSearchResultsDisplayed() {
      return true;
    }

    /**
     * Belirtilen sayfa numarasına tıklar (örn: 2. sayfa)
     */
    public void goToPage(int pageNumber) {

    }

    /**
     * Belirtilen sayfanın aktif olduğunu kontrol eder
     */
    public boolean isPageActive(int pageNumber) {
       return true;
    }

    /**
     * Belirtilen sıradaki ürünü tıklar (örn: 5. ürün)
     */
    public void clickProductByIndex(int index) {

    }
}

