package com.example.scraperremus;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.InputStream;
import java.time.Duration;
import java.util.*;

public class ProductScrapperMilltek {

    private static final String PRICE_LIST_FILE = "src/main/resources/milltek-sport.price.list.xlsx";

    // Mapa (systemNumber -> lista ProductMetadata) wczytana z Excela
    private static final Map<String, List<ProductMetadata>> productMetadataMap = new HashMap<>();
@Getter
    public static class ProductMetadata {
        private final String make;
        private final String model;
        private final String variant;
        private final String yearFrom;
        private final String yearTo;
        private final String typeOfSystem;
        private final double price;

        public ProductMetadata(String make,
                               String model,
                               String variant,
                               String yearFrom,
                               String yearTo,
                               String typeOfSystem,
                               double price) {
            this.make = make;
            this.model = model;
            this.variant = variant;
            this.yearFrom = yearFrom;
            this.yearTo = yearTo;
            this.typeOfSystem = typeOfSystem;
            this.price = price;
        }
    }

    // -------------------------------------------------------------------------------------
    // 1) Wczytanie pliku Excel do mapy systemNumber -> [metryczki]
    // -------------------------------------------------------------------------------------
    public static void loadPriceList() {
        if (!productMetadataMap.isEmpty()) {
            return; // już wczytane
        }
        try (InputStream inputStream = ProductScrapperMilltek.class
                .getClassLoader()
                .getResourceAsStream("milltek-sport.price.list.xlsx")) {

            if (inputStream == null) {
                System.err.println("Nie znaleziono pliku milltek-sport.price.list.xlsx!");
                return;
            }

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();

            // Pomijamy nagłówek
            if (it.hasNext()) {
                it.next();
            }

            while (it.hasNext()) {
                Row row = it.next();

                // Kolumny w Excelu (przykładowo):
                // col(0) = make
                // col(1) = model
                // col(2) = variant
                // col(5) = systemNumber
                // col(6) = typeOfSystem
                // col(17)= price
                String make         = getCellValue(row.getCell(0));
                String model        = getCellValue(row.getCell(1));
                String variant      = getCellValue(row.getCell(2));
                String yearFrom     = getCellValue(row.getCell(3));
                String yearTo       = getCellValue(row.getCell(4));
                String systemNumber = getCellValue(row.getCell(5)).trim();
                String typeOfSystem = getCellValue(row.getCell(6)).trim();
                String priceString  = getCellValue(row.getCell(17));

                if (systemNumber.isEmpty() || typeOfSystem.isEmpty() || priceString.isEmpty()) {
                    continue;
                }
                double price;
                try {
                    price = Double.parseDouble(priceString);
                } catch (NumberFormatException e) {
                    continue;
                }

                ProductMetadata meta = new ProductMetadata(
                        make, model, variant, yearFrom.substring(0,4), yearTo.substring(0,4), typeOfSystem, price
                );

                productMetadataMap
                        .computeIfAbsent(systemNumber, k -> new ArrayList<>())
                        .add(meta);
            }
            System.out.println("Załadowano Excela. Rozmiar mapy = " + productMetadataMap.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------------------
    // 2) Wyszukiwanie wiersza z Excela na podstawie (systemNumber, typeOfSystem)
    // -------------------------------------------------------------------------------------
    private static ProductMetadata getMetadataForSKUAndType(String systemNumber, String typeFromTable) {
        List<ProductMetadata> metas = productMetadataMap.getOrDefault(systemNumber, new ArrayList<>());
        for (ProductMetadata meta : metas) {
            // np. equalsIgnoreCase, jeżeli chcesz mniej restrykcyjnie:
            if (meta.getTypeOfSystem().equalsIgnoreCase(typeFromTable)) {
                return meta;
            }
        }
        return null;
    }

    // -------------------------------------------------------------------------------------
    // 3) Główna metoda
    // -------------------------------------------------------------------------------------
    public static void scrapeAndSaveCategory(String pageUrl) {
        loadPriceList(); // wczytanie pliku Excel

        Map<String, String> productMap = getProductMapFromCategory(pageUrl);
        List<Product> allProducts = new ArrayList<>();

        for (Map.Entry<String, String> entry : productMap.entrySet()) {
            String url = entry.getKey();
            String handle = entry.getValue();

            Product p = getProductFromBssJson(url, handle);
            if (p != null) {
                allProducts.add(p);
            }
        }

        // Zapis do CSV
        ShopifyBatchSaver.saveForShopify(allProducts, "shopify_import.csv");
    }

    // -------------------------------------------------------------------------------------
    // 4) Pobranie listy produktów (handle -> productUrl) z #bss-po-store-data
    // -------------------------------------------------------------------------------------
    private static Map<String, String> getProductMapFromCategory(String pageUrl) {
        Map<String, String> result = new LinkedHashMap<>();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(pageUrl);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement scriptEl = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("bss-po-store-data"))
            );

            String jsonText = (String)((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].textContent;", scriptEl);

            JSONObject root = new JSONObject(jsonText);
            JSONArray coll = root.getJSONArray("collection");
            for (int i = 0; i < coll.length(); i++) {
                JSONObject prod = coll.getJSONObject(i);
                String handle = prod.getString("handle");
                String productUrl = "https://milltekexhaustshop.com/products/" + handle;
                result.put(productUrl, handle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return result;
    }

    // -------------------------------------------------------------------------------------
    // 5) Parsowanie pojedynczego produktu
    // -------------------------------------------------------------------------------------
    public static Product getProductFromBssJson(String productUrl, String productHandle) {
        try {
            // Ściągamy cały HTML
            Document doc = Jsoup.connect(productUrl).ignoreContentType(true).get();

            Product product = new Product();
            product.setHandle(productHandle);
            product.setUrl(productUrl);

            // Bierzemy JSON #bss-po-store-data
            Element scriptEl = doc.getElementById("bss-po-store-data");
            String title = "";
            String description = "";
            JSONArray variantsArr = null;
            JSONArray optsArr = null;
            JSONArray imagesArr = null;

            if (scriptEl != null) {
                String jsonText = scriptEl.html();
                JSONObject storeData = new JSONObject(jsonText);
                JSONObject productObj = storeData.getJSONObject("product");

                title = productObj.optString("title", "No Title");
                description = productObj.optString("description", "");
                variantsArr = productObj.optJSONArray("variants");
                optsArr = productObj.optJSONArray("options");
                imagesArr = productObj.optJSONArray("images");
            } else {
                // fallback
                Element metaDesc = doc.selectFirst("meta[name=description]");
                if (metaDesc != null) {
                    description = metaDesc.attr("content");
                }
                title = doc.title();
            }

            product.setTitle(title);
            product.setDescription(description);

            // Nazwy opcji
            List<String> optionNames = new ArrayList<>();
            if (optsArr != null) {
                for (int i = 0; i < optsArr.length(); i++) {
                    optionNames.add(optsArr.getString(i));
                }
            }
            product.setOptionNames(optionNames);

            // Doklejamy tabele do opisu
            String extraTablesHtml = extractAndCleanVariantTables(doc);
            product.setDescription(product.getDescription() + "<br><br>" + extraTablesHtml);

            // Parsujemy finalny opis, żeby ustawić Vehicle Make/Model/Type, ale NIE Vehicle Variant
            parseTableFromDescription(product);

            // Tworzymy warianty
            List<Product.VariantData> variantList = new ArrayList<>();
            if (variantsArr != null) {
                for (int i = 0; i < variantsArr.length(); i++) {
                    JSONObject vObj = variantsArr.getJSONObject(i);

                    String sku = vObj.optString("sku", "NO-SKU");
                    if (sku.startsWith("MIL-")) {
                        sku = sku.substring(4);
                    }

                    String o1 = vObj.optString("option1", "");
                    String o2 = vObj.optString("option2", "");
                    String o3 = vObj.optString("option3", "");

                    Product.VariantData vData = new Product.VariantData(sku, o1, 0.0, o2, 0.0, o3, 0.0);

                    // Szukamy ceny w pliku Excel na podstawie (sku + product.getCustomTypeOfSystem)
                    String systemNumber = sku;
                    String typeFromTable = product.getCustomTypeOfSystem() != null
                            ? product.getCustomTypeOfSystem().trim()
                            : "";

                    ProductMetadata meta = getMetadataForSKUAndType(systemNumber, typeFromTable);
                    if (meta != null) {
                        vData.setBasePrice(meta.getPrice());
                        product.setCustomYearFrom(meta.yearFrom);
                        product.setCustomYearTo(meta.yearTo);
                    } else {
                        vData.setBasePrice(0.0);
                    }
                    variantList.add(vData);
                }
            }
            product.setVariantData(variantList);

            // SKU produktu = SKU pierwszego wariantu
            if (!variantList.isEmpty()) {
                product.setSku(variantList.get(0).getSku());
                product.setPrice(String.valueOf(variantList.get(0).getBasePrice()));
            } else {
                product.setSku("NO-SKU");
                product.setPrice("0.0");
            }

            // Zdjęcia
            List<String> images = new ArrayList<>();
            if (imagesArr != null) {
                for (int i = 0; i < imagesArr.length(); i++) {
                    String partial = imagesArr.getString(i);
                    if (partial.startsWith("//")) {
                        partial = "https:" + partial;
                    }
                    images.add(partial);
                }
            }
            product.setImagesLinks(images);

            return product;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metoda, która z finalnego "product.getDescription()" (już z doklejoną tabelą)
     * parsuje klucze: Vehicle Make, Vehicle Model, Vehicle Variant, Type
     * i ustawia w polach:
     *  - customMake
     *  - customModel
     *  - customTypeOfSystem
     *
     * Zamiast "Vehicle Variant" -> product.setCustomVariant(...),
     *   użyjemy getGenerationCodes(...) w oparciu o customModel i title
     */
    private static void parseTableFromDescription(Product product) {
        // Zamiana opisu produktu (HTML) na obiekt Document
        Document descDoc = Jsoup.parse(product.getDescription());

        // Znalezienie tabeli wewnątrz div.variant-table-cleaned
        Elements tables = descDoc.select("div.variant-table-cleaned table");
        if (tables.isEmpty()) {
            return;
        }

        // Parsowanie wierszy tabeli
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cells.size() < 2) {
                    continue;
                }
                String key = cells.get(0).text().trim();
                String val = cells.get(1).text().trim();

                switch (key) {
                    case "Vehicle Make":
                        product.setCustomMake(val);
                        break;
                    case "Vehicle Model":
                        product.setCustomModel(val);
                        break;
                    case "Vehicle Variant":
                        // Celowo pomijamy – ustawiamy generacje na podstawie tytułu i modelu
                        break;
                    case "Type":
                    case "System Type":
                        product.setCustomTypeOfSystem(val);
                        break;
                    case "Pipe Size":
                        product.setCustomPipeSize(val);
                        break;
                    case "Fitting Time":
                        product.setCustomFittingTime(val);
                        break;
                    default:
                        break;
                }
            }
        }

        // Ustalanie customVariant w oparciu o generacje (dla Vehicle Variant)
        if (product.getCustomModel() != null) {
            List<String> genCodes = getGenerationCodes(
                    product.getCustomModel(),
                    product.getTitle(),
                    getModels()
            );
            if (!genCodes.isEmpty()) {
                product.setCustomVariant(String.join(", ", genCodes));
            } else {
                product.setCustomVariant("");
            }
        }
    }

    /**
     * Przykładowa metoda do mapowania modeli -> lista generacji
     * (możesz przerzucić w inne miejsce)
     */
    public static Map<String, List<String>> getModels() {
        Map<String, List<String>> models = new HashMap<>();

        models.put("A1", Arrays.asList("9Y", "GB"));
        models.put("A2", Arrays.asList("8Z"));
        models.put("A3", Arrays.asList("8L", "8P", "8V", "8Y", "1.8 TSI", "1.9 TDI", "2.0 TDI", "2.0 TFSI", "2.0T FSI", "1.8T", "Sportback"));
        models.put("80", Arrays.asList("B1", "B2", "B3", "B4"));
        models.put("A4", Arrays.asList("B5", "B6", "B7", "B8", "B9", "1.8 TSI", " 3.0 TDi", "1.9 TDI", "2.0 TDI", "2.0 TFSI", "2.0T FSI", "1.8T"));
        models.put("A5", Arrays.asList("B10", "1.8 TSI", "1.9 TDI", "2.0 TDI", "2.0 TFSI", "2.0T FSI", "1.8T", "B5", "B6", "B7", "B8", "B9"));
        models.put("100", Arrays.asList("C1", "C2", "C3", "C4"));
        models.put("A6", Arrays.asList("C4", "C5", "C6", "C7", "C8", "C9"));
        models.put("V8", Arrays.asList("4C"));
        models.put("A8", Arrays.asList("D2", "D3", "D4", "D5"));
        models.put("A7", Arrays.asList("4G8", "4G9"));
        models.put("TT", Arrays.asList("8N", "8J", "8S"));
        models.put("R8", Arrays.asList("42", "4S", "Gen 2", "Gen 1"));

        // Modele Q (SUV-y i crossovery)
        models.put("Q2", Arrays.asList("GA"));
        models.put("Q3", Arrays.asList("8U", "8Y", "8Z"));
        models.put("Q3 Sportback", Arrays.asList("9Y", "9Z"));
        models.put("Q5", Arrays.asList("8R", "FY", "GU"));
        models.put("SQ5", Arrays.asList("8R-SQ5")); // przykładowe oznaczenie dla SQ5
        models.put("Q7", Arrays.asList("4L", "4M", "4N"));
        models.put("SQ7", Arrays.asList("4L-SQ7")); // przykładowe dla SQ7
        models.put("Q8", Arrays.asList("4N"));
        models.put("SQ8", Arrays.asList("4N-SQ8")); // przykładowe dla SQ8

        // Modele elektryczne i hybrydowe (podstawowe)
        models.put("Q2L e-tron", Arrays.asList("GA"));
        models.put("Q4 e-tron", Arrays.asList("F4"));
        models.put("Q4 e-tron Sportback", Arrays.asList("F4"));
        models.put("Q5 e-tron", Arrays.asList("G4"));
        models.put("Q6 e-tron", Arrays.asList("GF"));
        models.put("e-tron", Arrays.asList("4X"));
        models.put("Q8 e-tron", Arrays.asList("4X"));
        models.put("e-tron Sportback", Arrays.asList("4X"));
        models.put("Q8 e-tron Sportback", Arrays.asList("4X"));
        models.put("A6 e-tron", Arrays.asList("GH"));
        models.put("e-tron GT", Arrays.asList("FW"));

        // Modele S (wersje sportowe)
        models.put("S1", Arrays.asList("8X"));
        models.put("S3", Arrays.asList("8L", "8P", "8V", "8Y"));
        models.put("S4", Arrays.asList("B5", "B6", "B7", "B8", "B9"));
        models.put("S5", Arrays.asList("8T", "F5"));
        models.put("S6", Arrays.asList("C4", "C5", "C6", "C7", "C8"));
        models.put("S7", Arrays.asList("4G8", "4G9", "C4", "C5", "C6", "C7", "C8"));
        models.put("S8", Arrays.asList("4C", "4D", "D2", "D3", "D4", "D5"));

        // Modele RS (wersje wyczynowe)
        models.put("RS3", Arrays.asList("8L", "8P", "8V", "8Y", "Sportback"));
        models.put("RS4", Arrays.asList("B5", "B7", "B8", "B9"));
        models.put("RS5", Arrays.asList("8T", "F5"));
        models.put("RS6", Arrays.asList("C5", "C6", "C7", "C8"));
        models.put("RS7", Arrays.asList("4G8", "4G9", "C4", "C5", "C6", "C7", "C8"));

        // Dodatkowe modele RSQ i SQ (wyczynowe wersje Q)
        models.put("RSQ3", Arrays.asList("8V-RSQ3")); // przykładowe oznaczenie
        models.put("RSQ8", Arrays.asList("4X-RSQ8")); // przykładowe oznaczenie


        return models;
    }

    /**
     * Metoda do wyciągania generacji z tytułu,
     * np. jeżeli title = "Milltek Cat-Back for Audi A4 B9..."
     */
    public static List<String> getGenerationCodes(String model, String title, Map<String, List<String>> models) {
        List<String> result = new ArrayList<>();
        List<String> codes = models.get(model);
        if (codes == null) {
            return result;
        }
        String lowerTitle = title.toLowerCase();
        for (String code : codes) {
            if (lowerTitle.contains(code.toLowerCase())) {
                result.add(code);
            }
        }
        return result;
    }

    /**
     * Wydobywa i czyści tabele <div class="variant-additional-info">,
     * zwracając je w postaci HTML, który doklejamy do opisu.
     */
    private static String extractAndCleanVariantTables(Document doc) {
        // Zestaw kluczy, które chcemy zostawić w tabeli:
        Set<String> allowedKeys = new HashSet<>(Arrays.asList(
                "Specification",
                "Vehicle Make",
                "Vehicle Model",
                "Vehicle Variant",
                "Type",
                "Pipe Size",
                "Fitting Time",
                "Tip Style" // jeżeli też chcesz
        ));

        // Znajdź pierwszy <div class="variant-additional-info">
        Element variantDiv = doc.selectFirst("div.variant-additional-info");
        if (variantDiv == null) {
            return ""; // brak
        }

        // Znajdź pierwszą tabelę w tym div
        Element table = variantDiv.selectFirst("table");
        if (table == null) {
            return ""; // brak
        }

        // Klonujemy, żeby nie modyfikować oryginalnego doc
        Element clonedTable = table.clone();

        // Wycinamy z clonedTable wszystkie wiersze, które nie mają klucza z allowedKeys
        Elements rows = clonedTable.select("tr");
        for (Element row : rows) {
            Element firstCell = row.selectFirst("th, td");
            if (firstCell == null) {
                row.remove();
                continue;
            }
            String keyText = firstCell.text().trim();
            if (!allowedKeys.contains(keyText)) {
                row.remove();
            }
        }

        // Sprawdzamy, czy coś zostało
        if (!clonedTable.select("tr").isEmpty()) {
            // Pakujemy w <div class="variant-table-cleaned">
            return "<div class=\"variant-table-cleaned\">" + clonedTable.outerHtml() + "</div>";
        } else {
            // Nie ma żadnych pasujących wierszy
            return "";
        }
    }
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            default:      return "";
        }
    }
}
