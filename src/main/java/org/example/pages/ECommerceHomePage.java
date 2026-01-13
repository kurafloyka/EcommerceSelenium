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


    @FindBy(css = "i[class='new-search-icon']")
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

    /**
     * Arama kutusuna metin yazar ve arar
     * API isteklerinin tamamlanmasını bekler
     */
    public void searchProduct(String searchText) {
        try {
            // Arama input'unun hazır olmasını bekle
            //wait.until(ExpectedConditions.elementToBeClickable(searchInput));
            // LoggerUtils.logInfo("Arama kutusu hazır");

            // Arama kutusuna JavaScriptExecutor ile metin yaz
            //wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[data-test-id='search-bar-input']")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value = arguments[1];", searchInput, searchText);
            // Input event'lerini tetikle (bazı siteler için gerekli)
            js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", searchInput);
            js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", searchInput);
            LoggerUtils.logInfo("Arama kutusuna JavaScriptExecutor ile yazıldı: " + searchText);

           

            wait.until(ExpectedConditions.elementToBeClickable(searchInput));
            sendKeysToElement(searchInput, searchText);
            wait.until(ExpectedConditions.elementToBeClickable(searchInput));

            // Arama butonuna tıkla
           // clickElement(searchButton);
            sendKeysToElement(searchButton, String.valueOf(Keys.ENTER));
            LoggerUtils.logInfo("Ara butonuna tıklandı");

       

        } catch (Exception e) {
            LoggerUtils.logError("Arama işlemi başarısız: " + e.getMessage());
            throw e;
        }
    }




    private void waitForSearchSuggestions() {
        try {
            // Arama önerileri için yaygın selector'lar
            By[] suggestionSelectors = {
                    By.cssSelector("div[class*='suggestion'], div[class*='autocomplete'], ul[class*='suggestion'], ul[class*='autocomplete']"),
                    By.cssSelector("[class*='search-suggestion'], [class*='search-result'], [class*='suggestion-list']"),
                    By.xpath("//div[contains(@class,'suggestion')] | //ul[contains(@class,'suggestion')] | //div[contains(@class,'autocomplete')]")
            };

            for (By selector : suggestionSelectors) {
                try {
                    List<WebElement> suggestions = driver.findElements(selector);
                    if (!suggestions.isEmpty()) {
                        // Öneriler görünür olduğunda veya en az bir öneri yüklendiğinde
                        wait.until(ExpectedConditions.visibilityOf(suggestions.get(0)));
                        LoggerUtils.logInfo("Arama önerileri yüklendi: " + suggestions.size() + " öneri");
                        // Öneriler yüklendikten sonra kısa bir bekleme (API'nin tamamlanması için)
                        Thread.sleep(300);
                        return;
                    }
                } catch (Exception e) {
                    // Bu selector ile öneri bulunamadı, diğerini dene
                    continue;
                }
            }
            LoggerUtils.logInfo("Arama önerileri bulunamadı (normal olabilir)");
        } catch (Exception e) {
            LoggerUtils.logWarn("Arama önerileri kontrolü sırasında hata: " + e.getMessage());
        }
    }

    /**
     * Arama sonuçları sayfasının yüklenmesini bekler
     */
    private void waitForSearchResultsPage() {
        try {
            // URL değişikliğini bekle (arama sonuçları sayfasına yönlendirme)
            String currentUrl = driver.getCurrentUrl();
            wait.until(driver -> {
                String newUrl = driver.getCurrentUrl();
                return !newUrl.equals(currentUrl) || newUrl.contains("search") || newUrl.contains("arama") || newUrl.contains("q=");
            });

            // Arama sonuçlarının görünür olmasını bekle
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div[class*='product'], div[class*='item'], li[class*='product'], article[class*='product'], div[class*='result']")
            ));

            LoggerUtils.logInfo("Arama sonuçları sayfası yüklendi");
        } catch (Exception e) {
            LoggerUtils.logWarn("Arama sonuçları sayfası kontrolü sırasında hata: " + e.getMessage());
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

