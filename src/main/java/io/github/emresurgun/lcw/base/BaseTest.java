package io.github.emresurgun.lcw.base;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

   protected ChromeOptions chromeOptions;
   protected WebDriver webDriver;
   protected WebDriverWait webDriverWait;
   protected JsonReader jsonReader;

   @BeforeEach
   public void setUp()
   {
      jsonReader = new JsonReader("lcw.json");
      chromeOptions = new ChromeOptions();
      chromeOptions.addArguments("--start-maximized");
      chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
      chromeOptions.addArguments("--disable-notifications");
      webDriver = new ChromeDriver(chromeOptions);
      webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
   }

   @AfterEach
   public void tearDown()
   {
      if (webDriver !=null)
      {
         webDriver.quit();
      }
   }
}
