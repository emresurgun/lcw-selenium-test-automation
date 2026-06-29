package io.github.emresurgun.lcw.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.By;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class JsonReader {

    private JsonNode root;
    private ObjectMapper mapper;

    private static final String LOCATORS_FOLDER = "locators";

    public JsonReader() {
        try {
            mapper = new ObjectMapper();
            root = mapper.createObjectNode();

            URL locatorsUrl = getClass()
                    .getClassLoader()
                    .getResource(LOCATORS_FOLDER);

            if (locatorsUrl == null) {
                throw new RuntimeException("Locator klasörü bulunamadı: " + LOCATORS_FOLDER);
            }

            Path locatorsPath = Path.of(locatorsUrl.toURI());

            List<Path> jsonFiles = Files.walk(locatorsPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();

            for (Path jsonFile : jsonFiles) {
                JsonNode jsonNode = mapper.readTree(jsonFile.toFile());
                mergeJson(jsonNode);
            }

        } catch (Exception e) {
            throw new RuntimeException("Json dosyaları okunamadı.", e);
        }
    }

    private void mergeJson(JsonNode jsonNode) {
        jsonNode.fields().forEachRemaining(pageEntry -> {
            String pageName = pageEntry.getKey();
            JsonNode pageNode = pageEntry.getValue();

            if (!root.has(pageName)) {
                ((ObjectNode) root).set(pageName, mapper.createObjectNode());
            }

            ObjectNode targetPageNode = (ObjectNode) root.get(pageName);

            pageNode.fields().forEachRemaining(elementEntry ->
                    targetPageNode.set(elementEntry.getKey(), elementEntry.getValue())
            );
        });
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