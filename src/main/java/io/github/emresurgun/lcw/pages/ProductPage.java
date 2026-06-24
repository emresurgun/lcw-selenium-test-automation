package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class ProductPage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    public ProductPage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait)
    {
        this.jsonReader=jsonReader;
        this.webDriver=webDriver;
        this.webDriverWait=webDriverWait;
    }

    public void clickMoreProduct()
    {
        By moreProductButtonLocator=jsonReader.getLocator("productPage","moreProductButton");
        WebElement moreProductButton=webDriverWait.until(ExpectedConditions.elementToBeClickable(moreProductButtonLocator));
        ((JavascriptExecutor) webDriver).executeScript(

                "arguments[0].scrollIntoView({block: 'center'});",

                moreProductButton

        );
        try{
            Thread.sleep(2000);
        }catch (Exception e){}
        moreProductButton.click();
        try{
            Thread.sleep(3000);
        }catch (Exception e){}
    }

    public void clickRandomProduct()
    {
        By productLinksLocator = jsonReader.getLocator("productPage", "productLinks");

        List<WebElement> products = webDriverWait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(productLinksLocator)
        );

        int randomIndex = new Random().nextInt(products.size());
        WebElement selectedProduct = products.get(randomIndex);

        System.out.println("Seçilen ürün index: " + randomIndex);

        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                selectedProduct
        );

        try{
            Thread.sleep(2000);
        }catch (Exception e){}
        selectedProduct.click();
    }

}
