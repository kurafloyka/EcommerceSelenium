package org.example.tests;

import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.example.base.BaseTest;
import org.example.pages.*;
import org.example.utils.ConfigReader;
import org.example.utils.ExcelUtils;
import org.example.utils.LoggerUtils;
import java.util.List;
import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * E-ticaret test senaryoları için test sınıfı
 * 16 adımlı kapsamlı e-ticaret test senaryosu
 */
@Epic("E-Commerce")
@Feature("Shopping Flow")
public class ECommerceTest extends BaseTest {

    @BeforeMethod
    @Override
    public void setUp(org.testng.ITestResult result) {
        // E-ticaret URL'ini kullan
        super.setUp(result);
        
    }

    /**
     * Excel dosyasından e-ticaret test verilerini okur ve DataProvider olarak döner
     */
    @DataProvider(name = "ecommerceData")
    public Object[][] getECommerceData() {
        LoggerUtils.logInfo("Excel dosyasından test verileri okunuyor...");
        List<Map<String, String>> testData = ExcelUtils.readTestData("ECommerceData");

        if (testData == null || testData.isEmpty()) {
            throw new RuntimeException("Excel dosyasından test verisi okunamadı veya veri bulunamadı!");
        }

        LoggerUtils.logInfo("Excel'den " + testData.size() + " satır test verisi okundu");

        Object[][] data = new Object[testData.size()][7];

        for (int i = 0; i < testData.size(); i++) {
            Map<String, String> row = testData.get(i);

            // Veri kontrolü ve loglama
            LoggerUtils.logInfo("Test verisi " + (i + 1) + ": Email=" + row.get("Email") +
                ", SearchKeyword=" + row.get("SearchKeyword"));

            data[i][0] = row.get("Email");
            data[i][1] = row.get("Password");
            data[i][2] = row.get("SearchKeyword");
            data[i][3] = row.get("MainCategory");
            data[i][4] = row.get("SubCategory");
            data[i][5] = Integer.parseInt(row.get("PageNumber"));
            data[i][6] = Integer.parseInt(row.get("ProductIndex"));
        }

        LoggerUtils.logInfo("DataProvider hazırlandı, " + data.length + " test verisi döndürülüyor");
        return data;
    }

    @Test(priority = 1, dataProvider = "ecommerceData", description = "E-ticaret sitesi tam akış testi")
    @Description("Ana sayfa açılışı, login, arama, beğendiklerim, sepet işlemleri - Excel'den veri okunur")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Complete Shopping Flow")
    public void testCompleteShoppingFlow(String email, String password, String searchKeyword,
                                         String mainCategory, String subCategory,
                                         int pageNumber, int productIndex) {
        LoggerUtils.logInfo("E-ticaret tam akış testi başlatılıyor");
        extentTest.info("E-ticaret tam akış testi başlatılıyor");

        ECommerceHomePage homePage = new ECommerceHomePage(driver);

        // Adım 1: Herhangi uygun bir e-ticaret sitesi açılacak ve anasayfanın açıldığını onaylayacak
        LoggerUtils.logInfo("Adım 1: Ana sayfa kontrolü");
        extentTest.info("Adım 1: Ana sayfa kontrolü");
        Assertions.assertThat(homePage.isHomePageDisplayed())
                .as("Ana sayfa görüntülenmeli")
                .isTrue();
        extentTest.pass("Ana sayfa başarıyla açıldı");

        // Adım 2: Login ekranını açıp, bir kullanıcı ile login olacak
        LoggerUtils.logInfo("Adım 2: Login işlemi");
        extentTest.info("Adım 2: Login işlemi");
        homePage.clickLogin();
        ECommerceLoginPage loginPage = new ECommerceLoginPage(driver);
        Assertions.assertThat(loginPage.isLoginPageDisplayed())
                .as("Login sayfası görüntülenmeli")
                .isTrue();
        loginPage.login(email, password);
        try {
            Thread.sleep(2000); // Login sonrası sayfa yüklenmesi için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("Login işlemi tamamlandı");

        // Adım 3: Ekranın üstündeki Search alanına arama kelimesi yazıp 'Ara' butonuna tıklayacak
        LoggerUtils.logInfo("Adım 3: Arama işlemi");
        extentTest.info("Adım 3: Arama işlemi - '" + searchKeyword + "'");
        homePage = new ECommerceHomePage(driver);
        homePage.searchProduct(searchKeyword);
        try {
            Thread.sleep(2000); // Arama sonuçları için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("'" + searchKeyword + "' araması yapıldı");

        // Adım 4: Sol menüden kategori seçimi yapılacak
        LoggerUtils.logInfo("Adım 4: Kategori seçimi");
        extentTest.info("Adım 4: Kategori seçimi - " + mainCategory + " > " + subCategory);
        ECommerceSearchPage searchPage = new ECommerceSearchPage(driver);
        searchPage.selectCategory(mainCategory, subCategory);
        try {
            Thread.sleep(2000); // Kategori filtreleme için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("Kategori seçimi tamamlandı");

        // Adım 5: Gelen sayfada arama sonuçları bulunduğunu onaylayacak
        LoggerUtils.logInfo("Adım 5: Arama sonuçları kontrolü");
        extentTest.info("Adım 5: Arama sonuçları kontrolü - '" + searchKeyword + "'");
        Assertions.assertThat(searchPage.areSearchResultsDisplayed())
                .as("'" + searchKeyword + "' için arama sonuçları görüntülenmeli")
                .isTrue();
        extentTest.pass("Arama sonuçları başarıyla görüntülendi");

        // Adım 6: Arama sonuçlarından belirtilen sayfaya tıklayacak ve açılan sayfada sayfanın şu an gösterimde olduğunu onaylayacak
        LoggerUtils.logInfo("Adım 6: " + pageNumber + ". sayfaya geçiş");
        extentTest.info("Adım 6: " + pageNumber + ". sayfaya geçiş ve kontrol");
        searchPage.goToPage(pageNumber);
        try {
            Thread.sleep(2000); // Sayfa yüklenmesi için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assertions.assertThat(searchPage.isPageActive(pageNumber))
                .as(pageNumber + ". sayfa aktif olmalı")
                .isTrue();
        extentTest.pass(pageNumber + ". sayfa başarıyla açıldı ve aktif");

        // Adım 7: Üstten belirtilen sıradaki ürünü tıklayacak
        LoggerUtils.logInfo("Adım 7: " + productIndex + ". ürün seçimi");
        extentTest.info("Adım 7: " + productIndex + ". ürün seçimi");
        searchPage.clickProductByIndex(productIndex);
        try {
            Thread.sleep(2000); // Ürün detay sayfası için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass(productIndex + ". ürün seçildi");

        // Adım 8: Ürün detayında 'Beğen' butonuna tıklayacak
        LoggerUtils.logInfo("Adım 8: Beğen butonuna tıklama");
        extentTest.info("Adım 8: Beğen butonuna tıklama");
        ECommerceProductPage productPage = new ECommerceProductPage(driver);
        productPage.clickLikeButton();
        try {
            Thread.sleep(2000); // Popup için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("Beğen butonuna tıklandı");

        // Adım 9: 'Ürün listenize eklendi.' popup kontrolü yapacak
        LoggerUtils.logInfo("Adım 9: Popup kontrolü");
        extentTest.info("Adım 9: 'Ürün listenize eklendi' popup kontrolü");
        Assertions.assertThat(productPage.isProductAddedToWishlistPopupDisplayed())
                .as("'Ürün listenize eklendi' popup'ı görüntülenmeli")
                .isTrue();
        extentTest.pass("Popup başarıyla görüntülendi");

        // Adım 10: Ekranın en üstündeki hesabım alanında 'Beğendiklerim' tıklayacak
        LoggerUtils.logInfo("Adım 10: Beğendiklerim sayfasına gidiş");
        extentTest.info("Adım 10: Beğendiklerim sayfasına gidiş");
        homePage = new ECommerceHomePage(driver);
        homePage.goToWishlist();
        try {
            Thread.sleep(2000); // Sayfa yüklenmesi için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("Beğendiklerim sayfasına gidildi");

        // Adım 11: Açılan sayfada bir önceki sayfada beğendiklerime alınmış ürünün bulunduğunu onaylayacak
        LoggerUtils.logInfo("Adım 11: Beğendiklerim listesi kontrolü");
        extentTest.info("Adım 11: Beğendiklerim listesi kontrolü");
        ECommerceWishlistPage wishlistPage = new ECommerceWishlistPage(driver);
        Assertions.assertThat(wishlistPage.isProductInWishlist())
                .as("Beğendiklerim listesinde ürün olmalı")
                .isTrue();
        extentTest.pass("Ürün beğendiklerim listesinde bulundu");

        // Adım 12: Beğendiklerime alınmış ürün bulunup seçilecek ve sepete eklenecek
        LoggerUtils.logInfo("Adım 12: Ürünü sepete ekleme");
        extentTest.info("Adım 12: Ürünü sepete ekleme");
        wishlistPage.addFirstProductToCart();
        try {
            Thread.sleep(2000); // Popup için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("Ürün sepete eklendi");

        // Adım 13: 'Ürün sepete eklendi' popup kontrolü yapacak
        LoggerUtils.logInfo("Adım 13: Sepete ekleme popup kontrolü");
        extentTest.info("Adım 13: 'Ürün sepete eklendi' popup kontrolü");
        // Popup ürün detay sayfasında görünebilir, kontrol edelim
        productPage = new ECommerceProductPage(driver);
        boolean popupDisplayed = productPage.isProductAddedToCartPopupDisplayed();
        if (!popupDisplayed) {
            LoggerUtils.logInfo("Popup ürün detay sayfasında görünmedi, beğendiklerim sayfasında olabilir");
        }
        Assertions.assertThat(popupDisplayed)
                .as("'Ürün sepete eklendi' popup'ı görüntülenmeli")
                .isTrue();
        extentTest.pass("Sepete ekleme popup'ı görüntülendi");

        // Adım 14: Sepetim sayfasına gidecek
        LoggerUtils.logInfo("Adım 14: Sepet sayfasına gidiş");
        extentTest.info("Adım 14: Sepet sayfasına gidiş");
        homePage = new ECommerceHomePage(driver);
        homePage.goToCart();
        try {
            Thread.sleep(2000); // Sayfa yüklenmesi için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("Sepet sayfasına gidildi");

        // Adım 15: Sepete eklenen bu ürünün içine girilip 'Kaldır' butonuna basılacak, sepetimden çıkarılacak
        LoggerUtils.logInfo("Adım 15: Ürünü sepetten kaldırma");
        extentTest.info("Adım 15: Ürünü sepetten kaldırma");
        ECommerceCartPage cartPage = new ECommerceCartPage(driver);
        Assertions.assertThat(cartPage.isProductInCart())
                .as("Sepette ürün olmalı")
                .isTrue();
        cartPage.removeFirstProduct();
        try {
            Thread.sleep(2000); // Sayfa güncellemesi için bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        extentTest.pass("Ürün sepetten kaldırıldı");

        // Adım 16: Bu ürünün artık sepette olmadığını onaylayacak
        LoggerUtils.logInfo("Adım 16: Sepet boş kontrolü");
        extentTest.info("Adım 16: Sepet boş kontrolü");
        Assertions.assertThat(cartPage.isCartEmpty())
                .as("Sepet boş olmalı")
                .isTrue();
        extentTest.pass("Sepet boş olduğu doğrulandı");

        LoggerUtils.logInfo("E-ticaret tam akış testi başarıyla tamamlandı");
        extentTest.pass("Tüm adımlar başarıyla tamamlandı");
    }
}

