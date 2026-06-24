package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;
    public HomePage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait)
    {
        this.jsonReader=jsonReader;
        this.webDriver=webDriver;
        this.webDriverWait=webDriverWait;
    }

    public void openHomePage()
    {
        webDriver.get("https://www.lcw.com/");
    }

    public boolean isHomePageOpen()
    {
        boolean isHomePageOpen = false;

        String homePageTitle=webDriver.getTitle();

        By searchBoxLocator=jsonReader.getLocator("homePage","searchBox");
        WebElement searchBox =  webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));

        if (homePageTitle.toLowerCase().contains("lcw")&&searchBox.isDisplayed())
        {
            isHomePageOpen = true;
        }
        return isHomePageOpen;
    }

    public void closeCookieIfPresent()
    {
        boolean cookieFlag = false;
        int tryCount =0;
        By cookieAcceptButtonLocator = jsonReader.getLocator("homePage","cookieAcceptButton");
        while (!cookieFlag)
        {
            try {
                tryCount++;
                if(tryCount>=15)
                {
                    break;
                }
                WebElement cookieButton = webDriver.findElement(cookieAcceptButtonLocator);
                if(cookieButton.isDisplayed())
                {
                    Thread.sleep(100);
                    cookieButton.click();
                    cookieFlag=true;
                }
            }catch (NoSuchElementException e)
            {

            }
            catch (Exception e)
            {}
        }
    }

    public void gotoLogin()
    {
        By loginButtonLocator=jsonReader.getLocator("homePage","loginButton");
        WebElement loginButon=webDriverWait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginButon.click();
    }

    public void searchProduct(String product)
    {
        By searchBoxLocator=jsonReader.getLocator("homePage","searchBox");
        WebElement searchBox = webDriverWait.until(ExpectedConditions.elementToBeClickable(searchBoxLocator));
        searchBox.sendKeys(product);
        searchBox.sendKeys(Keys.ENTER);
    }


}
