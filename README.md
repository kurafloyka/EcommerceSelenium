# Selenium Essential Test Automation Project

Bu proje, Selenium WebDriver, TestNG, Page Object Model, Extent Reports, Allure ve Excel entegrasyonu içeren kapsamlı bir test otomasyon projesidir.

## Proje Özellikleri

- ✅ **Page Object Model (POM)** yapısı
- ✅ **TestNG Framework** entegrasyonu
- ✅ **Assertion'lar** (AssertJ kullanılarak)
- ✅ **Loglama** (Log4j2)
- ✅ **Raporlama**:
  - Extent Reports
  - Allure Reports
- ✅ **Excel Entegrasyonu**:
  - Test verilerinin Excel'den okunması
  - Test sonuçlarının Excel'e yazılması
- ✅ **Parametrik Testler** (DataProvider ile)
- ✅ **WebDriverManager** (Otomatik driver yönetimi)

## Proje Yapısı

```
SeleniumEssential/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/
│   │   │       ├── base/
│   │   │       │   └── BaseTest.java
│   │   │       ├── pages/
│   │   │       │   ├── BasePage.java
│   │   │       │   └── LoginPage.java
│   │   │       ├── utils/
│   │   │       │   ├── ExcelUtils.java
│   │   │       │   ├── ExcelDataGenerator.java
│   │   │       │   └── LoggerUtils.java
│   │   │       └── listeners/
│   │   │           └── TestListener.java
│   │   └── resources/
│   │       ├── log4j2.xml
│   │       ├── allure.properties
│   │       └── testdata/
│   │           ├── TestData.xlsx
│   │           └── TestResults.xlsx
│   └── test/
│       ├── java/
│       │   └── org/example/
│       │       └── tests/
│       │           ├── LoginTest.java
│       │           └── HomePageTest.java
│       └── resources/
│           └── testng.xml
└── pom.xml
```

## Kurulum

### Gereksinimler

- Java 11 veya üzeri
- Maven 3.6 veya üzeri
- Chrome, Firefox veya Edge browser

### Adımlar

1. **Projeyi klonlayın veya indirin**

2. **Maven bağımlılıklarını yükleyin:**
   ```bash
   mvn clean install
   ```

3. **Test verileri Excel dosyasını oluşturun:**
   ```bash
   mvn exec:java -Dexec.mainClass="org.example.utils.ExcelDataGenerator"
   ```
   Veya IDE'den `ExcelDataGenerator` sınıfını çalıştırın.

4. **Excel dosyasını düzenleyin:**
   `src/main/resources/testdata/TestData.xlsx` dosyasını açın ve test verilerinizi girin.

## Test Çalıştırma

### Tüm testleri çalıştırma:
```bash
mvn test
```

### TestNG XML ile çalıştırma:
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Belirli bir test sınıfını çalıştırma:
```bash
mvn test -Dtest=LoginTest
```

## Raporlar

### Extent Reports
Test çalıştıktan sonra rapor şu konumda oluşturulur:
```
test-output/ExtentReport.html
```

Tarayıcıda açarak görüntüleyebilirsiniz.

### Allure Reports
Allure raporunu oluşturmak için:
```bash
mvn allure:report
```

Raporu görüntülemek için:
```bash
mvn allure:serve
```

### Log Dosyaları
Log dosyaları şu konumda oluşturulur:
```
test-output/logs/
```

### Excel Test Sonuçları
Test sonuçları otomatik olarak şu dosyaya yazılır:
```
src/main/resources/testdata/TestResults.xlsx
```

## Test Senaryoları

### LoginTest
- `testSuccessfulLogin`: Geçerli kullanıcı bilgileri ile login testi
- `testFailedLogin`: Geçersiz kullanıcı bilgileri ile login testi

### HomePageTest
- `testPageTitle`: Ana sayfa başlık kontrolü
- `testCurrentUrl`: URL kontrolü

## Kod Kalitesi Özellikleri

- **Page Object Model**: Tüm sayfa elementleri ve metodları ayrı sınıflarda
- **Base Classes**: Ortak fonksiyonlar base sınıflarda
- **Utility Classes**: Tekrar kullanılabilir yardımcı sınıflar
- **Assertions**: AssertJ ile detaylı assertion'lar
- **Logging**: Her adımda loglama
- **Error Handling**: Try-catch blokları ve hata yönetimi
- **Code Comments**: Türkçe ve İngilizce açıklamalar

## Browser Seçenekleri

Browser seçimi sistem property'si ile yapılabilir:
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

Varsayılan browser: `chrome`

## Excel Dosya Formatı

### TestData.xlsx
ECommerceData sheet'i şu formatta olmalıdır:

| Email | Password | SearchKeyword | MainCategory | SubCategory | PageNumber | ProductIndex |
|-------|----------|---------------|--------------|-------------|------------|--------------|
| test@example.com | Test123456 | samsung | Telefon | Cep Telefonu | 2 | 5 |

### TestResults.xlsx
Test sonuçları otomatik olarak şu formatta yazılır:

| Test Adı | Test Durumu | Çalışma Süresi (ms) | Hata Mesajı | Tarih/Saat |

## Notlar

- WebDriverManager otomatik olarak browser driver'larını yönetir
- Test sonuçları hem Extent Reports, Allure Reports hem de Excel'e yazılır
- Allure raporları için `allure-results` klasörü otomatik oluşturulur
- Log dosyaları otomatik olarak rotate edilir
- Test verileri Excel dosyasından okunur (`TestData.xlsx`)
- Browser ve URL ayarları sistem property'leri ile yapılabilir

## Geliştirici Notları

- Yeni test senaryoları eklemek için `src/test/java/org/example/tests/` klasörüne yeni test sınıfları ekleyin
- Yeni sayfalar için `src/main/java/org/example/pages/` klasörüne Page Object sınıfları ekleyin
- Test verilerini güncellemek için `TestData.xlsx` dosyasını düzenleyin
- Browser ve URL ayarları için sistem property'lerini kullanın: `-Dbrowser=chrome -Dbase.url=https://example.com`

## Lisans

Bu proje eğitim amaçlı oluşturulmuştur.

