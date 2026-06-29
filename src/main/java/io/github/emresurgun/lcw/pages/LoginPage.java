package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    private static final Logger logger = LogManager.getLogger(LoginPage.class);


    public LoginPage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait)
    {
        this.jsonReader=jsonReader;
        this.webDriver=webDriver;
        this.webDriverWait=webDriverWait;
    }

    public void fillMail(String email)
    {
        logger.info("Mail alanı dolduruluyor.");
        By mailFormLocator=jsonReader.getLocator("loginPage","mailForm");
        WebElement mailForm = webDriver.findElement(mailFormLocator);
        mailForm.sendKeys(email);
        logger.info("Mail alanı dolduruldu.");
    }


    public void fillPassword(String password)
    {
        logger.info("Şifre alanı dolduruluyor.");
        By passwordFormLocator=jsonReader.getLocator("loginPage","passwordForm");
        WebElement passwordForm=webDriver.findElement(passwordFormLocator);
        passwordForm.sendKeys(password);
        logger.info("Şifre alanı dolduruldu.");
        try {
            Thread.sleep(1500);
        }catch (Exception e){}
    }


    public void clickLogin() {
        logger.info("Login butonuna tıklama işlemi başlatıldı.");
        By loginButtonLocator = jsonReader.getLocator("loginPage", "loginButton");
        WebElement loginButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginButton.click();
        logger.info("Login butonuna tıklandı.");

        try {
            Thread.sleep(15000);
        }catch (Exception e){}
        }
    }

