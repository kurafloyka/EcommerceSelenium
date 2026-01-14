package org.example.pages;

import org.example.utils.LoggerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * E-ticaret ana sayfası için Page Object Model sınıfı
 */
public class ECommerceHomePage extends BasePage {

    @FindBy(xpath = "//*[@placeholder='Ürün, kategori veya marka ara']")
private WebElement searchInput;

// searchButton'ı kaldırın veya doğru arama butonunu bulun (eğer varsa)
// Trendyol'da genellikle Enter tuşu ile arama yapılıyor, ayrı bir buton olmayabilir


    @FindBy(css = "#single-search-suggestion-container > div > div > input")
    private WebElement searchButton;

    @FindBy(css = "[class='newMobileLogoDesktop']")
    private WebElement iconImage;

    @FindBy(css = "div[class='searchSuggestionList']")
    private WebElement searchSuggestionsList;

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

    @FindBy(css = "a[data-testid='navigation-logo-component']")
    private WebElement trendyolLogo;

    @FindBy(css = "div[data-testid='user-menu']")
    private WebElement accountMenu;

    @FindBy(css = "div[role='search']")
    private WebElement searchBarInput;

    @FindBy(id = "login")
    private WebElement loginMenu;


    @FindBy(css = "div[class='modal-section-close']")
    private WebElement closePopupButton;




    public ECommerceHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Ana sayfanın açıldığını kontrol eder
     */
    public boolean isHomePageDisplayed() {
        try {

            wait.until(ExpectedConditions.elementToBeClickable(trendyolLogo));

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


    public void setClosePopupButton(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(closePopupButton));
            clickElement(closePopupButton);
            LoggerUtils.logInfo("Popup kapatıldı");
        } catch (Exception e) {
            LoggerUtils.logWarn("Popup kapatılamadı veya bulunamadı: " + e.getMessage());
        }
    }
    public void clickLogin() {
        try {


            wait.until(ExpectedConditions.visibilityOf(accountMenu));

            // Create an Actions instance
            Actions actions = new Actions(driver);

            // Perform the hover action
            actions.moveToElement(accountMenu).perform();

            clickElement(accountMenu);
            LoggerUtils.logInfo("Login linkine tıklandı");
        } catch (Exception e) {
            LoggerUtils.logError("Login linki bulunamadı: " + e.getMessage());
            throw e;
        }
    }

    public void searchProduct(String searchText) {
    try {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Sayfa tamamen yüklensin
        wait.until(d ->
                js.executeScript("return document.readyState").equals("complete")
        );
        LoggerUtils.logInfo("Sayfa yüklendi, arama işlemi başlatılıyor...");

        // Arama input'unun hazır olmasını bekle
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[@placeholder='Ürün, kategori veya marka ara']")
        ));
        
        // Element'i tekrar bul (stale element olmaması için)
        WebElement searchBox = driver.findElement(
            By.xpath("//*[@placeholder='Ürün, kategori veya marka ara']")
        );
        
        // Element'i görünür alana getir
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", searchBox);
        
        // Element'in tıklanabilir olmasını bekle
        wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        
        // JavaScriptExecutor ile focus yap
        js.executeScript("arguments[0].focus();", searchBox);
        
        // Input'u temizle ve metin yaz
        searchBox.clear();
        searchBox.sendKeys(searchText);
        LoggerUtils.logInfo("Arama kutusuna yazıldı: " + searchText);
        
        // API isteklerinin tamamlanması için kısa bekleme
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Enter tuşuna bas
        searchBox.sendKeys(Keys.ENTER);
        LoggerUtils.logInfo("Arama yapıldı: " + searchText);
        
        // Arama sonuçlarının yüklenmesini bekle
        waitForSearchResultsPage();
        
    } catch (Exception e) {
        LoggerUtils.logError("Arama işlemi başarısız: " + e.getMessage());
        throw e;
    }
}




    private void waitForSearchSuggestions() {

    }

    /**
     * Arama sonuçları sayfasının yüklenmesini bekler
     */
    private void waitForSearchResultsPage() {

    }

    /**
     * Hesabım menüsünden Beğendiklerim'e gider
     */
    public void goToWishlist() {

    }

    /**
     * Sepet sayfasına gider
     */
    public void goToCart() {

    }
}

