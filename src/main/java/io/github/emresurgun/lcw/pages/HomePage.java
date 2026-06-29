package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    private static final Logger logger = LogManager.getLogger(HomePage.class);


    public HomePage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait) {
        this.jsonReader = jsonReader;
        this.webDriver = webDriver;
        this.webDriverWait = webDriverWait;
    }


    public void openHomePage() {
        logger.info("LCW ana sayfası açılıyor.");
        webDriver.get("https://www.lcw.com/");
        logger.info("LCW ana sayfasına gidildi. URL: {}", webDriver.getCurrentUrl());
    }


    public boolean isHomePageOpen() {
        logger.info("LCW ana sayfasının açılıp açılmadığı kontrol ediliyor.");

        String homePageTitle = webDriver.getTitle();

        By searchBoxLocator = jsonReader.getLocator("homePage", "searchBox");
        WebElement searchBox = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));

        boolean isHomePageOpen = homePageTitle.toLowerCase().contains("lcw") && searchBox.isDisplayed();

        logger.info("Ana sayfa kontrol sonucu: {} | Title: {}", isHomePageOpen, homePageTitle);

        return isHomePageOpen;
    }


    public void closeCookieIfPresent() {
        logger.info("Çerez bildirimi kontrol ediliyor.");

        boolean cookieFlag = false;
        int tryCount = 0;

        By cookieAcceptButtonLocator = jsonReader.getLocator("homePage", "cookieAcceptButton");

        while (!cookieFlag) {
            try {
                tryCount++;

                if (tryCount >= 20) {
                    logger.info("Çerez bildirimi bulunamadı veya görünmedi.");
                    break;
                }

                WebElement cookieButton = webDriver.findElement(cookieAcceptButtonLocator);

                if (cookieButton.isDisplayed()) {
                    Thread.sleep(100);
                    cookieButton.click();
                    cookieFlag = true;
                    logger.info("Çerez bildirimi kapatıldı.");
                }
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(500);
                }catch (Exception ez){}

            } catch (Exception e) {
                logger.warn("Çerez bildirimi kapatılırken beklenmeyen bir durum oluştu: {}", e.getMessage());
            }
        }
    }


    public void gotoLogin() {
        logger.info("Login sayfasına yönlendirme başlatıldı.");

        By loginButtonLocator = jsonReader.getLocator("homePage", "loginButton");
        WebElement loginButon = webDriverWait.until(
                ExpectedConditions.elementToBeClickable(loginButtonLocator)
        );

        loginButon.click();

        logger.info("Login butonuna tıklandı.");
    }


    public void searchProduct(String product) {
        logger.info("Ürün araması yapılıyor. Arama kelimesi: {}", product);

        By searchBoxLocator = jsonReader.getLocator("homePage", "searchBox");
        WebElement searchBox = webDriverWait.until(ExpectedConditions.elementToBeClickable(searchBoxLocator));

        searchBox.sendKeys(product);
        searchBox.sendKeys(Keys.ENTER);

        logger.info("Ürün araması gönderildi. Arama kelimesi: {}", product);
    }
}
