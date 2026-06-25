package io.github.emresurgun.lcw.tests;

import io.github.emresurgun.lcw.base.BaseTest;
import io.github.emresurgun.lcw.pages.CartPage;
import io.github.emresurgun.lcw.pages.HomePage;
import io.github.emresurgun.lcw.pages.ProductDetailPage;
import io.github.emresurgun.lcw.pages.ProductPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class LcwShoppingTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LcwShoppingTest.class);

    @Test
    public void shouldCompleteShoppingCartFlow() {
        logger.info("LCW alışveriş sepeti test senaryosu başlatıldı.");
        HomePage homePage = new HomePage(jsonReader, webDriver, webDriverWait);
        ProductPage productPage = new ProductPage(jsonReader, webDriver, webDriverWait);
        ProductDetailPage productDetailPage = new ProductDetailPage(jsonReader, webDriver, webDriverWait);
        CartPage cartPage = new CartPage(jsonReader, webDriver, webDriverWait);

        logger.info("Ana sayfa açılış adımı başlatıldı.");
        // Ana sayfa açılır
        homePage.openHomePage();
        homePage.closeCookieIfPresent();

        Assertions.assertTrue(
                homePage.isHomePageOpen(),
                "LCW ana sayfası açılmadı."
        );
        logger.info("Ana sayfa doğrulaması tamamlandı.");

        // Login kısmı LCW tarafında Selenium'da sorun çıkardığı için şimdilik kapalı
        // homePage.gotoLogin();
        // loginPage.fillMail("@gmail.com");
        // loginPage.fillPassword("");
        // loginPage.clickLogin();

        logger.info("Ürün arama adımı başlatıldı.");
        // Ürün aranır
        homePage.searchProduct("pantolon");
        logger.info("Ürün arama adımı tamamlandı.");

        logger.info("Ürün listeleme ve rastgele ürün seçme adımı başlatıldı.");
        // Daha fazla ürün gösterilir ve rastgele ürün seçilir
        productPage.clickMoreProduct();
        productPage.clickRandomProduct();
        logger.info("Rastgele ürün seçme adımı tamamlandı.");

        logger.info("Ürün detay kombinasyon seçimi adımı başlatıldı.");
        // Uygun renk / beden / boy kombinasyonu seçilir
        productDetailPage.selectAvailableProductCombination();
        logger.info("Ürün detay kombinasyon seçimi adımı tamamlandı.");

        // Ürün sayfasındaki fiyat alınır
        String productPagePrice = productDetailPage.getProductPrice();
        logger.info("Ürün detay sayfasındaki fiyat alındı: {}", productPagePrice);

        logger.info("Ürünü sepete ekleme adımı başlatıldı.");
        // Ürün sepete eklenir ve sepete gidilir
        productDetailPage.clickAddToCart();
        productDetailPage.gotoCart();
        logger.info("Ürün sepete eklendi ve sepet sayfasına gidildi.");

        // Sepetteki fiyatlar alınır
        String cartProductPrice = cartPage.getCartProductPrice();
        String shippingPrice = cartPage.getShippingPrice();
        String generalTotalPrice = cartPage.getGeneralTotalPrice();
        logger.info("Sepet fiyat bilgileri alındı. Ürün: {}, Kargo: {}, Genel toplam: {}", cartProductPrice, shippingPrice, generalTotalPrice);

        // Ürün sayfasındaki fiyat ile sepetteki ürün fiyatı karşılaştırılır
        Assertions.assertEquals(
                productPagePrice,
                cartProductPrice,
                "Ürün sayfasındaki fiyat ile sepetteki ürün fiyatı aynı değil."
        );
        logger.info("Ürün detay fiyatı ile sepet ürün fiyatı doğrulandı.");

        double cartPrice = parsePrice(cartProductPrice);
        double shipping = parsePrice(shippingPrice);
        double generalTotal = parsePrice(generalTotalPrice);

        // Ürün fiyatı + kargo = genel toplam mı kontrol edilir
        Assertions.assertEquals(
                cartPrice + shipping,
                generalTotal,
                0.01,
                "Ürün fiyatı + kargo, genel toplam ile aynı değil."
        );
        logger.info("Ürün fiyatı + kargo = genel toplam doğrulaması tamamlandı.");

        logger.info("Ürün adedi artırma adımı başlatıldı.");
        // Ürün adedi arttırılır
        cartPage.increaseProductQuantity();

        webDriverWait.until(driver -> cartPage.getProductQuantity().equals("2"));

        String quantityText = cartPage.getProductQuantity();

        // Ürün adedinin 2 olduğu doğrulanır
        Assertions.assertEquals(
                "2",
                quantityText,
                "Ürün adedi 2 olmadı."
        );
        logger.info("Ürün adedinin 2 olduğu doğrulandı.");

        int quantity = Integer.parseInt(quantityText);

        double productPrice = parsePrice(cartProductPrice);
        double updatedShipping = parsePrice(cartPage.getShippingPrice());
        double updatedGeneralTotal = parsePrice(cartPage.getGeneralTotalPrice());
        logger.info("Güncel sepet değerleri alındı. Adet: {}, Güncel kargo: {}, Güncel genel toplam: {}", quantity, updatedShipping, updatedGeneralTotal);

        // Ürün fiyatı * adet + güncel kargo = güncel genel toplam mı kontrol edilir
        Assertions.assertEquals(
                productPrice * quantity + updatedShipping,
                updatedGeneralTotal,
                0.01,
                "Ürün fiyatı * adet + güncel kargo, genel toplam ile aynı değil."
        );
        logger.info("Ürün fiyatı * adet + güncel kargo = güncel genel toplam doğrulaması tamamlandı.");

        logger.info("Sepeti temizleme adımı başlatıldı.");
        // Ürün sepetten silinir
        cartPage.cleanCart();
        cartPage.confirmCleaning();

        // Sepetin boş olduğu kontrol edilir
        Assertions.assertTrue(
                cartPage.isCartEmpty(),
                "Ürün silindikten sonra sepet boş görünmüyor."
        );
        logger.info("Sepetin boş olduğu doğrulandı.");
        logger.info("LCW alışveriş sepeti test senaryosu başarıyla tamamlandı.");
    }

    private double parsePrice(String priceText) {
        logger.info("Fiyat metni sayısal değere dönüştürülüyor: {}", priceText);
        if (priceText == null || priceText.isBlank()) {
            logger.warn("Fiyat metni boş veya null olduğu için 0 kabul edildi.");
            return 0;
        }

        if (priceText.toLowerCase().contains("ücretsiz")) {
            logger.info("Fiyat metni ücretsiz olduğu için 0 kabul edildi.");
            return 0;
        }

        return Double.parseDouble(
                priceText
                        .replace("TL", "")
                        .replace(".", "")
                        .replace(",", ".")
                        .trim()
        );
    }
}