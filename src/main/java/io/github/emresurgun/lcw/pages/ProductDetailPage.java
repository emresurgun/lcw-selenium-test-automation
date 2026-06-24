package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class ProductDetailPage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    public ProductDetailPage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait)
    {
        this.jsonReader=jsonReader;
        this.webDriver=webDriver;
        this.webDriverWait=webDriverWait;
    }

    private boolean trySizeCombination() {
        By bodySizeLocator = jsonReader.getLocator("productDetailPage", "availableBodySizes");
        By allLengthSizeLocator = jsonReader.getLocator("productDetailPage", "allLengthSizes");
        By availableLengthSizeLocator = jsonReader.getLocator("productDetailPage", "availableLengthSizes");

        List<WebElement> bodySizes = webDriver.findElements(bodySizeLocator);

        if (bodySizes.isEmpty()) {
            System.out.println("Bu renkte seçilebilir beden yok.");
            return false;
        }

        for (int bodyIndex = 0; bodyIndex < bodySizes.size(); bodyIndex++) {
            bodySizes = webDriver.findElements(bodySizeLocator);

            WebElement bodySize = bodySizes.get(bodyIndex);
            String bodyText = bodySize.getText();

            System.out.println("Denenen beden: " + bodyText);

            bodySize.click();

            waitSmall();

            List<WebElement> allLengthSizes = webDriver.findElements(allLengthSizeLocator);

            if (allLengthSizes.isEmpty()) {
                System.out.println("Boy seçeneği yok. Beden yeterli: " + bodyText);
                return true;
            }

            List<WebElement> availableLengthSizes = webDriver.findElements(availableLengthSizeLocator);

            if (!availableLengthSizes.isEmpty()) {
                WebElement lengthSize = availableLengthSizes.get(0);

                System.out.println("Seçilen beden: " + bodyText);
                System.out.println("Seçilen boy: " + lengthSize.getText());

                lengthSize.click();

                return true;
            }

            System.out.println(bodyText + " bedeni için uygun boy yok. Sonraki beden deneniyor.");
        }

        return false;
    }

    private void waitSmall() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void selectAvailableProductCombination() {
        By colorOptionsLocator = jsonReader.getLocator("productDetailPage", "colorOptions");

        List<WebElement> colors = webDriver.findElements(colorOptionsLocator);

        if (colors.isEmpty()) {
            System.out.println("Renk seçeneği yok, mevcut renkle kombinasyon aranıyor.");

            if (trySizeCombination()) {
                return;
            }

            throw new RuntimeException("Mevcut renkte uygun beden/boy kombinasyonu bulunamadı.");
        }

        for (int colorIndex = 0; colorIndex < colors.size(); colorIndex++) {
            colors = webDriver.findElements(colorOptionsLocator);

            WebElement color = colors.get(colorIndex);

            System.out.println("Denenen renk index: " + colorIndex);

            color.click();

            waitSmall();

            if (trySizeCombination()) {
                System.out.println("Uygun kombinasyon bulundu. Renk index: " + colorIndex);
                return;
            }

            System.out.println("Bu renkte uygun kombinasyon bulunamadı. Sonraki renk deneniyor.");
        }

        throw new RuntimeException("Hiçbir renkte uygun beden/boy kombinasyonu bulunamadı.");
    }

    public void clickAddToCart()
    {
        By addToCartButtonLocator=jsonReader.getLocator("productDetailPage","addToCartButton");
        WebElement addToCartButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(addToCartButtonLocator));
        addToCartButton.click();
    }

    public void gotoCart()
    {
        By gotoCartButtonLocator=jsonReader.getLocator("productDetailPage","gotoCartButton");
        WebElement gotoCartButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(gotoCartButtonLocator));
        gotoCartButton.click();
        try {
            Thread.sleep(1000);
        }catch (Exception e){}
    }

    public String getProductPrice() {
        By discountedPriceLocator = jsonReader.getLocator("productDetailPage", "discountedPrice");
        By normalPriceLocator = jsonReader.getLocator("productDetailPage", "normalPrice");

        List<WebElement> discountedPrices = webDriver.findElements(discountedPriceLocator);

        if (!discountedPrices.isEmpty()) {
            String price = discountedPrices.get(0).getText().trim();
            System.out.println("Ürün indirimli fiyatı: " + price);
            return price;
        }

        WebElement normalPrice = webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(normalPriceLocator)
        );

        String price = normalPrice.getText().trim();
        System.out.println("Ürün normal fiyatı: " + price);
        return price;
    }


}
