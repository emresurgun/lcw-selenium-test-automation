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
import java.util.Random;

public class ProductDetailPage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    private static final Logger logger = LogManager.getLogger(ProductDetailPage.class);

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
            logger.warn("Bu renkte seçilebilir beden yok.");
            return false;
        }

        for (int bodyIndex = 0; bodyIndex < bodySizes.size(); bodyIndex++) {
            bodySizes = webDriver.findElements(bodySizeLocator);

            WebElement bodySize = bodySizes.get(bodyIndex);
            String bodyText = bodySize.getText();

            logger.info("Denenen beden: {}", bodyText);

            bodySize.click();

            logger.info("Beden seçildi: {}", bodyText);

            waitSmall();

            List<WebElement> allLengthSizes = webDriver.findElements(allLengthSizeLocator);

            if (allLengthSizes.isEmpty()) {
                logger.info("Boy seçeneği yok. Beden yeterli: {}", bodyText);
                return true;
            }

            List<WebElement> availableLengthSizes = webDriver.findElements(availableLengthSizeLocator);

            if (!availableLengthSizes.isEmpty()) {
                WebElement lengthSize = availableLengthSizes.get(0);

                logger.info("Seçilen beden: {}", bodyText);
                logger.info("Seçilen boy: {}", lengthSize.getText());

                lengthSize.click();

                logger.info("Boy seçildi: {}", lengthSize.getText());

                return true;
            }

            logger.warn("{} bedeni için uygun boy yok. Sonraki beden deneniyor.", bodyText);
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
        logger.info("Uygun ürün kombinasyonu seçimi başlatıldı.");
        By colorOptionsLocator = jsonReader.getLocator("productDetailPage", "colorOptions");

        List<WebElement> colors = webDriver.findElements(colorOptionsLocator);

        if (colors.isEmpty()) {
            logger.info("Renk seçeneği yok, mevcut renkle kombinasyon aranıyor.");

            if (trySizeCombination()) {
                return;
            }

            logger.error("Mevcut renkte uygun beden/boy kombinasyonu bulunamadı.");
            throw new RuntimeException("Mevcut renkte uygun beden/boy kombinasyonu bulunamadı.");
        }

        for (int colorIndex = 0; colorIndex < colors.size(); colorIndex++) {
            colors = webDriver.findElements(colorOptionsLocator);

            WebElement color = colors.get(colorIndex);

            logger.info("Denenen renk index: {}", colorIndex);

            color.click();

            logger.info("Renk seçildi. Index: {}", colorIndex);

            waitSmall();

            if (trySizeCombination()) {
                logger.info("Uygun kombinasyon bulundu. Renk index: {}", colorIndex);
                return;
            }

            logger.warn("Bu renkte uygun kombinasyon bulunamadı. Sonraki renk deneniyor.");
        }

        logger.error("Hiçbir renkte uygun beden/boy kombinasyonu bulunamadı.");
        throw new RuntimeException("Hiçbir renkte uygun beden/boy kombinasyonu bulunamadı.");
    }

    public void clickAddToCart()
    {
        logger.info("Sepete ekle butonuna tıklama işlemi başlatıldı.");
        By addToCartButtonLocator=jsonReader.getLocator("productDetailPage","addToCartButton");
        WebElement addToCartButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(addToCartButtonLocator));
        addToCartButton.click();
        logger.info("Sepete ekle butonuna tıklandı.");
    }

    public void gotoCart()
    {
        logger.info("Sepete gitme işlemi başlatıldı.");
        By gotoCartButtonLocator=jsonReader.getLocator("productDetailPage","gotoCartButton");
        WebElement gotoCartButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(gotoCartButtonLocator));
        gotoCartButton.click();
        logger.info("Sepete git butonuna tıklandı.");
        try {
            Thread.sleep(1000);
        }catch (Exception e){}
    }

    public String getProductPrice() {
        logger.info("Ürün fiyatı alınıyor.");
        By discountedPriceLocator = jsonReader.getLocator("productDetailPage", "discountedPrice");
        By normalPriceLocator = jsonReader.getLocator("productDetailPage", "normalPrice");

        List<WebElement> discountedPrices = webDriver.findElements(discountedPriceLocator);

        if (!discountedPrices.isEmpty()) {
            String price = discountedPrices.get(0).getText().trim();
            logger.info("Ürün indirimli fiyatı: {}", price);
            return price;
        }

        WebElement normalPrice = webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(normalPriceLocator)
        );

        String price = normalPrice.getText().trim();
        logger.info("Ürün normal fiyatı: {}", price);
        return price;
    }


}
