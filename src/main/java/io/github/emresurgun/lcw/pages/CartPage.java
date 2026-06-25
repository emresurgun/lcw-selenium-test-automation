package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CartPage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    private static final Logger logger = LogManager.getLogger(CartPage.class);

    public CartPage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait) {
        this.jsonReader = jsonReader;
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
    }

    public String getCartProductPrice() {
        logger.info("Sepetteki ürün fiyatı alınıyor.");
        By salePriceLocator = jsonReader.getLocator("cartPage", "cartSaleProductPrice");
        By normalPriceLocator = jsonReader.getLocator("cartPage", "cartProductPrice");

        List<WebElement> salePrices = webDriver.findElements(salePriceLocator);

        if (!salePrices.isEmpty()) {
            String price = salePrices.get(0).getText().trim();
            logger.info("Sepetteki indirimli ürün fiyatı: {}", price);
            return price;
        }

        WebElement normalPrice = webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(normalPriceLocator)
        );

        String price = normalPrice.getText().trim();
        logger.info("Sepetteki normal ürün fiyatı: {}", price);
        return price;
    }

    public String getShippingPrice() {
        logger.info("Kargo fiyatı alınıyor.");
        By locator = jsonReader.getLocator("cartPage", "shippingPrice");

        WebElement element = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        String price = element.getText().trim();
        logger.info("Kargo fiyatı: {}", price);
        return price;
    }

    public String getGeneralTotalPrice() {
        logger.info("Genel toplam fiyatı alınıyor.");
        By locator = jsonReader.getLocator("cartPage", "generalTotalPrice");

        WebElement element = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        String price = element.getText().trim();
        logger.info("Genel toplam: {}", price);
        return price;
    }

    public void increaseProductQuantity() {
        logger.info("Ürün adedi artırılıyor.");
        By increaseButtonLocator = jsonReader.getLocator("cartPage", "increaseQuantityButton");
        WebElement increaseButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(increaseButtonLocator));
        increaseButton.click();
        logger.info("Ürün adedi artırma butonuna tıklandı.");

    }
    public String getProductQuantity() {
        logger.info("Sepetteki ürün adedi alınıyor.");
        By quantityLocator = jsonReader.getLocator("cartPage", "productQuantity");
        WebElement quantityElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(quantityLocator));
        String quantity = quantityElement.getText().trim();

        logger.info("Sepetteki ürün adedi: {}", quantity);

        return quantity;
    }

    public void cleanCart()
    {
        logger.info("Sepetteki ürün silme butonuna tıklanıyor.");
        By cleanCartButtonLocator=jsonReader.getLocator("cartPage","deleteProductButton");
        WebElement cleanCartButton=webDriverWait.until(ExpectedConditions.elementToBeClickable(cleanCartButtonLocator));
        cleanCartButton.click();
        logger.info("Sepetteki ürün silme butonuna tıklandı.");
        try{
            Thread.sleep(1000);
        }catch (Exception e){}
    }

    public void confirmCleaning()
    {
        logger.info("Ürün silme onayı veriliyor.");
        By confirmCleaningButtonLocator=jsonReader.getLocator("cartPage","confirmDeleteButton");
        WebElement confirmCleaningButton=webDriverWait.until(ExpectedConditions.elementToBeClickable(confirmCleaningButtonLocator));
        confirmCleaningButton.click();
        logger.info("Ürün silme onayı verildi.");
    }

    public boolean isCartEmpty() {
        logger.info("Sepetin boş olduğu kontrol ediliyor.");
        By emptyCartTitleLocator = jsonReader.getLocator("cartPage", "emptyCartTitle");
        WebElement emptyCartTitle = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartTitleLocator));

        boolean isCartEmpty = emptyCartTitle.isDisplayed();
        logger.info("Sepet boş kontrol sonucu: {}", isCartEmpty);

        return isCartEmpty;
    }

}