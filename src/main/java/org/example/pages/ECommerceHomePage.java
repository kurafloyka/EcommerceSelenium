package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * E-ticaret ana sayfası için Page Object Model sınıfı
 */
public class ECommerceHomePage extends BasePage {

    @FindBy(css = "#app > div.stickyClass.n11getirLogo > div > div.search-box-wrapper.searchBox > div > input")
    private WebElement searchInput;


    @FindBy(css = ".new-search-icon")
    private WebElement searchButton;

    @FindBy(css = "a[href*='login'], a[href*='giris'], a[class*='login'], button[class*='login']")
    private WebElement loginLink;

    @FindBy(css = "a[href*='hesabim'], a[href*='account'], a[class*='account'], a[class*='user']")
    private WebElement accountLink;

    @FindBy(css = "a[href*='beğendiklerim'], a[href*='wishlist'], a[href*='favorites'], a[class*='wishlist']")
    private WebElement wishlistLink;

    @FindBy(css = "a[href*='sepet'], a[href*='cart'], a[class*='cart'], a[class*='basket']")
    private WebElement cartLink;

    @FindBy(css = "img[alt*='logo'], img[class*='logo'], a[class*='logo']")
    private WebElement logo;

    public ECommerceHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Ana sayfanın açıldığını kontrol eder
     */
    public boolean isHomePageDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
            LoggerUtils.logInfo("Ana sayfa görüntülendi");
            return true;
        } catch (Exception e) {
            LoggerUtils.logError("Ana sayfa görüntülenemedi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Login sayfasına gider
     */
    public void clickLogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='login'], a[href*='giris'], a[class*='login'], button[class*='login']")));
            WebElement login = driver.findElement(By.cssSelector("a[href*='login'], a[href*='giris'], a[class*='login'], button[class*='login']"));
            clickElement(login);
            LoggerUtils.logInfo("Login linkine tıklandı");
        } catch (Exception e) {
            LoggerUtils.logError("Login linki bulunamadı: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Arama kutusuna metin yazar ve arar
     */
    public void searchProduct(String searchText) {
        try {

            wait.until(ExpectedConditions.elementToBeClickable(searchInput));

           // wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='text'][placeholder*='ara'], input[type='search'], input[name*='search'], input[id*='search']")));
           // WebElement search = driver.findElement(By.cssSelector("input[type='text'][placeholder*='ara'], input[type='search'], input[name*='search'], input[id*='search']"));
            sendKeysToElement(searchInput, searchText);
            LoggerUtils.logInfo("Arama kutusuna yazıldı: " + searchText);



            wait.until(ExpectedConditions.elementToBeClickable(searchButton));
            //wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit'], button[class*='search'], button[class*='btn-search'], a[class*='search']")));
            //WebElement searchBtn = driver.findElement(By.cssSelector("button[type='submit'], button[class*='search'], button[class*='btn-search'], a[class*='search']"));
            clickElement(searchButton);



            LoggerUtils.logInfo("Ara butonuna tıklandı");
        } catch (Exception e) {
            LoggerUtils.logError("Arama işlemi başarısız: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Hesabım menüsünden Beğendiklerim'e gider
     */
    public void goToWishlist() {
        try {
            // Önce hesabım menüsüne tıkla
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='hesabim'], a[href*='account'], a[class*='account'], a[class*='user']")));
            WebElement account = driver.findElement(By.cssSelector("a[href*='hesabim'], a[href*='account'], a[class*='account'], a[class*='user']"));
            clickElement(account);
            
            try {
                Thread.sleep(1000); // Dropdown menü için bekle
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggerUtils.logWarn("Thread sleep interrupted");
            }
            
            // Beğendiklerim linkine tıkla
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='beğendiklerim'], a[href*='wishlist'], a[href*='favorites'], a[class*='wishlist']")));
            WebElement wishlist = driver.findElement(By.cssSelector("a[href*='beğendiklerim'], a[href*='wishlist'], a[href*='favorites'], a[class*='wishlist']"));
            clickElement(wishlist);
            LoggerUtils.logInfo("Beğendiklerim sayfasına gidildi");
        } catch (Exception e) {
            LoggerUtils.logError("Beğendiklerim sayfasına gidilemedi: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Sepet sayfasına gider
     */
    public void goToCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href*='sepet'], a[href*='cart'], a[class*='cart'], a[class*='basket']")));
            WebElement cart = driver.findElement(By.cssSelector("a[href*='sepet'], a[href*='cart'], a[class*='cart'], a[class*='basket']"));
            clickElement(cart);
            LoggerUtils.logInfo("Sepet sayfasına gidildi");
        } catch (Exception e) {
            LoggerUtils.logError("Sepet sayfasına gidilemedi: " + e.getMessage());
            throw e;
        }
    }
}

