package io.github.emresurgun.lcw.tests;

import io.github.emresurgun.lcw.base.BaseTest;
import io.github.emresurgun.lcw.pages.CartPage;
import io.github.emresurgun.lcw.pages.HomePage;
import io.github.emresurgun.lcw.pages.ProductDetailPage;
import io.github.emresurgun.lcw.pages.ProductPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class LcwShoppingTest extends BaseTest {

    @Test
    public void shouldCompleteShoppingCartFlow() {
        HomePage homePage = new HomePage(jsonReader, webDriver, webDriverWait);
        ProductPage productPage = new ProductPage(jsonReader, webDriver, webDriverWait);
        ProductDetailPage productDetailPage = new ProductDetailPage(jsonReader, webDriver, webDriverWait);
        CartPage cartPage = new CartPage(jsonReader, webDriver, webDriverWait);

        // Ana sayfa açılır
        homePage.openHomePage();
        homePage.closeCookieIfPresent();

        Assertions.assertTrue(
                homePage.isHomePageOpen(),
                "LCW ana sayfası açılmadı."
        );

        // Login kısmı LCW tarafında Selenium'da sorun çıkardığı için şimdilik kapalı
        // homePage.gotoLogin();
        // loginPage.fillMail("@gmail.com");
        // loginPage.fillPassword("");
        // loginPage.clickLogin();

        // Ürün aranır
        homePage.searchProduct("pantolon");

        // Daha fazla ürün gösterilir ve rastgele ürün seçilir
        productPage.clickMoreProduct();
        productPage.clickRandomProduct();

        // Uygun renk / beden / boy kombinasyonu seçilir
        productDetailPage.selectAvailableProductCombination();

        // Ürün sayfasındaki fiyat alınır
        String productPagePrice = productDetailPage.getProductPrice();

        // Ürün sepete eklenir ve sepete gidilir
        productDetailPage.clickAddToCart();
        productDetailPage.gotoCart();

        // Sepetteki fiyatlar alınır
        String cartProductPrice = cartPage.getCartProductPrice();
        String shippingPrice = cartPage.getShippingPrice();
        String generalTotalPrice = cartPage.getGeneralTotalPrice();

        // Ürün sayfasındaki fiyat ile sepetteki ürün fiyatı karşılaştırılır
        Assertions.assertEquals(
                productPagePrice,
                cartProductPrice,
                "Ürün sayfasındaki fiyat ile sepetteki ürün fiyatı aynı değil."
        );

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

        int quantity = Integer.parseInt(quantityText);

        double productPrice = parsePrice(cartProductPrice);
        double updatedShipping = parsePrice(cartPage.getShippingPrice());
        double updatedGeneralTotal = parsePrice(cartPage.getGeneralTotalPrice());

        // Ürün fiyatı * adet + güncel kargo = güncel genel toplam mı kontrol edilir
        Assertions.assertEquals(
                productPrice * quantity + updatedShipping,
                updatedGeneralTotal,
                0.01,
                "Ürün fiyatı * adet + güncel kargo, genel toplam ile aynı değil."
        );

        // Ürün sepetten silinir
        cartPage.cleanCart();
        cartPage.confirmCleaning();

        // Sepetin boş olduğu kontrol edilir
        Assertions.assertTrue(
                cartPage.isCartEmpty(),
                "Ürün silindikten sonra sepet boş görünmüyor."
        );
    }

    private double parsePrice(String priceText) {
        if (priceText == null || priceText.isBlank()) {
            return 0;
        }

        if (priceText.toLowerCase().contains("ücretsiz")) {
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