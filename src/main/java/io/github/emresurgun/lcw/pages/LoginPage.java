package io.github.emresurgun.lcw.pages;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private JsonReader jsonReader;
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    public LoginPage(JsonReader jsonReader, WebDriver webDriver, WebDriverWait webDriverWait)
    {
        this.jsonReader=jsonReader;
        this.webDriver=webDriver;
        this.webDriverWait=webDriverWait;
    }

    public void fillMail(String email)
    {
        By mailFormLocator=jsonReader.getLocator("loginPage","mailForm");
        WebElement mailForm = webDriver.findElement(mailFormLocator);
        mailForm.sendKeys(email);
    }

    public void fillPassword(String password)
    {
        By passwordFormLocator=jsonReader.getLocator("loginPage","passwordForm");
        WebElement passwordForm=webDriver.findElement(passwordFormLocator);
        passwordForm.sendKeys(password);
        try {
            Thread.sleep(1500);
        }catch (Exception e){}
    }
    public void clickLogin() {
        By loginButtonLocator = jsonReader.getLocator("loginPage", "loginButton");
        WebElement loginButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginButton.click();
        }
    }

