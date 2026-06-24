package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CartPage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    public CartPage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait) {
        this.jsonReader = jsonReader;
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
    }

    public String getCartProductPrice() {
        By salePriceLocator = jsonReader.getLocator("cartPage", "cartSaleProductPrice");
        By normalPriceLocator = jsonReader.getLocator("cartPage", "cartProductPrice");

        List<WebElement> salePrices = webDriver.findElements(salePriceLocator);

        if (!salePrices.isEmpty()) {
            String price = salePrices.get(0).getText().trim();
            System.out.println("Sepetteki indirimli ürün fiyatı: " + price);
            return price;
        }

        WebElement normalPrice = webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(normalPriceLocator)
        );

        String price = normalPrice.getText().trim();
        System.out.println("Sepetteki normal ürün fiyatı: " + price);
        return price;
    }

    public String getShippingPrice() {
        By locator = jsonReader.getLocator("cartPage", "shippingPrice");

        WebElement element = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        String price = element.getText().trim();
        System.out.println("Kargo fiyatı: " + price);
        return price;
    }

    public String getGeneralTotalPrice() {
        By locator = jsonReader.getLocator("cartPage", "generalTotalPrice");

        WebElement element = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        String price = element.getText().trim();
        System.out.println("Genel toplam: " + price);
        return price;
    }

    public void increaseProductQuantity() {
        By increaseButtonLocator = jsonReader.getLocator("cartPage", "increaseQuantityButton");
        WebElement increaseButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(increaseButtonLocator));
        increaseButton.click();

    }
    public String getProductQuantity() {
        By quantityLocator = jsonReader.getLocator("cartPage", "productQuantity");
        WebElement quantityElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(quantityLocator));
        String quantity = quantityElement.getText().trim();

        System.out.println("Sepetteki ürün adedi: " + quantity);

        return quantity;
    }

    public void cleanCart()
    {
        By cleanCartButtonLocator=jsonReader.getLocator("cartPage","deleteProductButton");
        WebElement cleanCartButton=webDriverWait.until(ExpectedConditions.elementToBeClickable(cleanCartButtonLocator));
        cleanCartButton.click();
        try{
            Thread.sleep(1000);
        }catch (Exception e){}
    }

    public void confirmCleaning()
    {
        By confirmCleaningButtonLocator=jsonReader.getLocator("cartPage","confirmDeleteButton");
        WebElement confirmCleaningButton=webDriverWait.until(ExpectedConditions.elementToBeClickable(confirmCleaningButtonLocator));
        confirmCleaningButton.click();
    }

    public boolean isCartEmpty() {
        By emptyCartTitleLocator = jsonReader.getLocator("cartPage", "emptyCartTitle");
        WebElement emptyCartTitle = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(emptyCartTitleLocator));

        return emptyCartTitle.isDisplayed();
    }

}