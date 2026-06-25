package io.github.emresurgun.lcw.base;

import io.github.emresurgun.lcw.utils.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

   private static final Logger logger = LogManager.getLogger(BaseTest.class);

   protected ChromeOptions chromeOptions;
   protected WebDriver webDriver;
   protected WebDriverWait webDriverWait;
   protected JsonReader jsonReader;

   @BeforeEach
   public void setUp()
   {
      logger.info("Test kurulumu başlatıldı.");
      jsonReader = new JsonReader("lcw.json");
      logger.info("JSON locator dosyası yüklendi.");
      chromeOptions = new ChromeOptions();
      logger.info("ChromeOptions oluşturuldu.");
      chromeOptions.addArguments("--start-maximized");
      chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
      chromeOptions.addArguments("--disable-notifications");
      webDriver = new ChromeDriver(chromeOptions);
      logger.info("ChromeDriver başlatıldı.");
      webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
      logger.info("WebDriverWait oluşturuldu. Timeout: 20 saniye.");
   }

   @AfterEach
   public void tearDown()
   {
      logger.info("Test temizleme işlemi başlatıldı.");
      if (webDriver !=null)
      {
         webDriver.quit();
         logger.info("ChromeDriver kapatıldı.");
      }
   }
}
