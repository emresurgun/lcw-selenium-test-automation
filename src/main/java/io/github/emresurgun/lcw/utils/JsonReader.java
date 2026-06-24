package io.github.emresurgun.lcw.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;

import java.io.InputStream;

public class JsonReader {


    private JsonNode root;

    public JsonReader(String filePath) {
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(filePath);

            if (inputStream == null) {
                throw new RuntimeException("Locator file not found: " + filePath);
            }

            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(inputStream);

        } catch (Exception e) {
            throw new RuntimeException("Could not read locator JSON file: " + filePath, e);
        }
    }

    public String getType(String page, String element) {
        return root.get(page).get(element).get("type").asText();
    }

    public String getValue(String page, String element) {
        return root.get(page).get(element).get("value").asText();
    }

    public By getLocator(String page, String element) {
        String type = getType(page, element);
        String value = getValue(page, element);

        if (type.equals("id")) {
            return By.id(value);
        }

        if (type.equals("name")) {
            return By.name(value);
        }

        if (type.equals("css")) {
            return By.cssSelector(value);
        }

        if (type.equals("xpath")) {
            return By.xpath(value);
        }

        throw new RuntimeException("Unknown locator type: " + type);
    }


}
