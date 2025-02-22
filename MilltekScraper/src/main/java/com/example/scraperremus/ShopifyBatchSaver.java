package com.example.scraperremus;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopifyBatchSaver {

    private static final String VENDOR = "Milltek";
    private static final String PRODUCT_CATEGORY =
            "Pojazdy i części > Akcesoria i części do pojazdów > Części do pojazdów silnikowych > Elementy układu wydechowego";
    private static final String TYPE = "Układ wydechowy";
    private static final String TAGS = "Milltek, BMW, Układ wydechowy";
    private static final String PUBLISHED = "TRUE";
    private static final String VARIANT_INVENTORY_POLICY = "continue";
    private static final String VARIANT_FULFILLMENT_SERVICE = "manual";
    private static final String VARIANT_REQUIRES_SHIPPING = "TRUE";
    private static final String VARIANT_TAXABLE = "TRUE";
    private static final String GIFT_CARD = "FALSE";
    private static final String STATUS = "active";

    // Przykładowy VAT i kurs
    private static final double VAT_RATE = 1.23;
    private static final double EXCHANGE_RATE = 4.3;

    private static final DecimalFormat priceFormat = new DecimalFormat("0.00");
    private static final Map<String, String> translationsTypeOfSystem = new HashMap<>();

    static {
        translationsTypeOfSystem.put("Cat-back", "Cat-back");
        translationsTypeOfSystem.put("Particulate Filter-back", "GPF/OPF-back");
        translationsTypeOfSystem.put("Resonator Bypass", "Resonator Bypass");
        translationsTypeOfSystem.put("Full System", "Układ wydechowy");
        translationsTypeOfSystem.put("Downpipe-back", "Downpipe-back");
        translationsTypeOfSystem.put("Front Pipe-back", "Front Pipe-back");


    }

    // Mapa tłumaczeń do Option Name / Value:
    private static final Map<String, String> translations = new HashMap<>();

    static {
        // Przykładowe wpisy z treści pytania:
        translations.put("435i Style", "Styl 435i");
        translations.put("Exit", "Wylot");
        translations.put("OE Style", "Styl OE");
        translations.put("Cerakote Black", "Cerakote Czarny");
        translations.put("Equal Length (Loudest)", "Równa długość (najgłośniejsze)");
        translations.put("Equal Length Non Resonated (Loudest)", "Równa długość (bez tłumika) (najgłośniejsze)");
        translations.put("GT-115 Brushed Titanium", "GT-115 Tytan szczotkowany");
        translations.put("GT-115 Burnt Titanium", "GT-115 Tytan palony");
        translations.put("GT-115 Cerakote Black", "GT-115 Cerakote Czarny");
        translations.put("GT-115 Cerakote Black Trims", "GT-115 Cerakote Czarny (wykończenia)");
        translations.put("GT-115 Polished", "GT-115 Polerowany");
        translations.put("GT-115 Titanium Trims", "GT-115 Tytanowe wykończenia");
        translations.put("GT-80 Black Cerakote", "GT-80 Cerakote Czarny");
        translations.put("GT-80 Cerakote Black", "GT-80 Cerakote Czarny");
        translations.put("GT-80 Polished", "GT-80 Polerowany");
        translations.put("GT-80 Polished Trims", "GT-80 Polerowany (wykończenia)");
        translations.put("GT-90 Burnt Titanium", "GT-90 Tytan palony");
        translations.put("GT-90 Cerakote Black", "GT-90 Cerakote Czarny");
        translations.put("GT-90 Polished", "GT-90 Polerowany");
        translations.put("GT-90 Titanium", "GT-90 Tytan");
        translations.put("JET-90 Carbon", "JET-90 Carbon");

        // Zamiana "Non Resonated" => "Bez tłumika"
        translations.put("Non Resonated", "Bez tłumika");

        translations.put("Polished", "Polerowany");

        // Zamiana "Race Non Resonated" => "Torowy bez tłumika"
        translations.put("Race Non Resonated", "Torowy bez tłumika");

        // Zamiana "Race Resonated" => "Torowy z tłumikiem"
        translations.put("Race Resonated", "Torowy z tłumikiem");

        // Zamiana "Resonated?" => "Z tłumikiem?"
        translations.put("Resonated?", "Z tłumikiem?");

        // "Race" w sensie ogólnym – "Torowy"
        translations.put("Road", "Uliczny");

        // Zamiana "Road Resonated" => "Uliczny z tłumikiem"
        translations.put("Road Resonated", "Uliczny z tłumikiem");

        // Zamiana "Titanium" => "Tytan" (bez zmian)
        translations.put("Titanium", "Tytan");
        translations.put("Twin GT-80 Cerakote Black", "Podwójny GT-80 Cerakote Czarny");
        translations.put("Twin GT-80 Polished", "Podwójny GT-80 Polerowany");
        translations.put("Twin Valved", "Podwójny z klapami");
        translations.put("Usage", "Zastosowanie");
        translations.put("Valved?", "Z klapami?");
        translations.put("Black Tips", "Czarne końcówki");
        translations.put("Burnt Titanium Tips", "Palone tytanowe końcówki");
        translations.put("Carbon Tips", "Karbonowe końcówki");
        translations.put("Cerakote Black Tips", "Cerakote czarne końcówki");
        translations.put("Default Title", "");
        translations.put("Title", "");
        translations.put("EC Approved Non Valved", "EC homologowany (bez klap)");
        translations.put("Fits to OE Outlet", "Pasuje do fabrycznego wylotu");
        translations.put("Fits to OE trims", "Pasuje do fabrycznych wykończeń");
        translations.put("GT-100 Brushed Titanium", "GT-100 Tytan szczotkowany");
        translations.put("GT-100 Burnt / Blue Titanium", "GT-100 Tytan palony (niebieski)");
        translations.put("GT-100 Cerakote Black", "GT-100 Cerakote Czarny");
        translations.put("GT-100 Polished", "GT-100 Polerowany");

        // Powtórzenia (dla spójności)
        translations.put("GT-115 Burnt / Blue Titanium", "GT-115 Tytan palony (niebieski)");

        translations.put("JET-100 Carbon Fibre", "JET-100 włókno węglowe");

        // Zamiana Non Valved => Bez klap
        translations.put("Non Valved", "Bez klap");

        translations.put("OE", "Fabryczny");
        translations.put("Polished Tips", "Polerowane końcówki");
        translations.put("Quad GT-100 Black", "Poczwórny GT-100 Czarny");
        translations.put("Quad GT-100 Burnt Titanium", "Poczwórny GT-100 Tytan palony");
        translations.put("Quad GT-100 Cerakote Black", "Poczwórny GT-100 Cerakote Czarny");
        translations.put("Quad GT-100 Polished", "Poczwórny GT-100 Polerowany");

        // "Race" – ponowne nadpisanie, nadal "Torowy"
        translations.put("Race", "Torowy");


        // "Road (EC Approved)" => "Uliczny (z homologacją EC)"
        translations.put("Road (EC Approved)", "Uliczny (z homologacją EC)");

        translations.put("Titanium Tips", "Tytanowe końcówki");
        translations.put("Trims", "Wybierz końcówki");

        // Zamiana "Valved" => "Z klapami"
        translations.put("Valved", "Z klapami");

        // -------------------------
        // 2. ZESTAW DRUGI
        // -------------------------

        translations.put("Option", "Opcja");
        translations.put("System", "Układ");
        translations.put("Race System?", "Układ torowy?");
        translations.put("Valvesonic?", "Valvesonic (z klapami)?");

        // Zmiana "Non Resonated" => "bez tłumika"
        translations.put("Non Resonated Black Oval Tips", "Bez tłumika z czarnymi owalnymi końcówkami");
        translations.put("Non Resonated Polished Oval Tips", "Bez tłumika z polerowanymi owalnymi końcówkami");
        translations.put("Non Resonated Black Round Tips", "Bez tłumika z czarnymi okrągłymi końcówkami");
        translations.put("Non Resonated Polished Round Tips", "Bez tłumika z polerowanymi okrągłymi końcówkami");
        translations.put("Non Resonated Titanium Round Tips", "Bez tłumika z tytanowymi okrągłymi końcówkami");
        translations.put("Non Resonated Race System Black Oval Tips", "Torowy bez tłumika z czarnymi owalnymi końcówkami");
        translations.put("Non Resonated Race System Polished Oval Tips", "Torowy bez tłumika z polerowanymi owalnymi końcówkami");
        translations.put("Non Resonated Race System Black Round Tips", "Torowy bez tłumika z czarnymi okrągłymi końcówkami");
        translations.put("Non Resonated Race System Polished Round Tips", "Torowy bez tłumika z polerowanymi okrągłymi końcówkami");
        translations.put("Non Resonated Race System Titanium Round Tips", "Torowy bez tłumika z tytanowymi okrągłymi końcówkami");

        // Zmiana "Resonated" => "z tłumikiem"
        translations.put("Resonated Race System Black Oval Tips", "Torowy z tłumikiem z czarnymi owalnymi końcówkami");
        translations.put("Resonated Race System Polished Oval Tips", "Torowy z tłumikiem z polerowanymi owalnymi końcówkami");
        translations.put("Resonated Race System Black Round Tips", "Torowy z tłumikiem z czarnymi okrągłymi końcówkami");
        translations.put("Resonated Race System Polished Round Tips", "Torowy z tłumikiem z polerowanymi okrągłymi końcówkami");
        translations.put("Resonated Race System Titanium Round Tips", "Torowy z tłumikiem z tytanowymi okrągłymi końcówkami");
        translations.put("Resonated Black Oval Tips", "Z tłumikiem z czarnymi owalnymi końcówkami");
        translations.put("Resonated Polished Oval Tips", "Z tłumikiem z polerowanymi owalnymi końcówkami");
        translations.put("Resonated Black Round Tips", "Z tłumikiem z czarnymi okrągłymi końcówkami");
        translations.put("Resonated Polished Round Tips", "Z tłumikiem z polerowanymi okrągłymi końcówkami");
        translations.put("Resonated Titanium Round Tips", "Z tłumikiem z tytanowymi okrągłymi końcówkami");
        translations.put("Resonated", "Z tłumikiem");
        translations.put("Part Resonated", "Częściowo z tłumikiem");

        translations.put("OPF Back", "Układ za filtrem OPF");
        translations.put("Front Pipe Back", "Układ za przednią rurą");
        translations.put("Yes", "Tak");
        translations.put("No", "Nie");
        translations.put("Cerakote Czarny", "Cerakote Czarny"); // Już po polsku
        translations.put("Road+ Resonated", "Uliczny+ z tłumikiem");
        translations.put("Twin 80mm Cerakote Black Oval", "Podwójny 80 mm Cerakote Czarny (owalny)");
        translations.put("Twin 80mm Polished Oval", "Podwójny 80 mm polerowany (owalny)");
        translations.put("Valvesonic", "Valvesonic (z klapami)");
        translations.put("Quad Cerakote Black", "Poczwórny Cerakote Czarny");
        translations.put("Quad Polished", "Poczwórny polerowany");
        translations.put("Black Velvet", "Czarny Velvet");
        translations.put("Dual Cerakote Black", "Podwójny Cerakote Czarny");
        translations.put("Cerakote Black Oval", "Cerakote Czarny (owalny)");
        translations.put("Dual Cerakote Black Oval", "Podwójny Cerakote Czarny (owalny)");
        translations.put("Quad Burnt Titanium", "Poczwórny palony tytan");
        translations.put("Quad JET-100 Carbon", "Poczwórny JET-100 karbon");
        translations.put("Quad Titanium", "Poczwórny tytan");
        translations.put("GT-80 Quad Polished", "GT-80 poczwórny polerowany");
        translations.put("Dual Polished Oval", "Podwójny polerowany (owalny)");
        translations.put("Polished GT-100", "Polerowany GT-100");
        translations.put("Polished JET", "Polerowany JET");
        translations.put("US-Spec", "Specyfikacja USA");
        translations.put("Dual Polished", "Podwójny polerowany");
        translations.put("Race Version", "Wersja torowa");
        translations.put("Supercup Version", "Wersja Supercup");
        translations.put("100mm GT-100", "GT-100 100 mm");
        translations.put("100mm JET", "JET 100 mm");
        translations.put("Catalytic Converter?", "Katalizator?");
        translations.put("90mm Detachable", "Demontowalny 90 mm");
        translations.put("100mm Detachable", "Demontowalny 100 mm");
        translations.put("90mm GT90 Detachable", "Demontowalny GT-90 90 mm");
        translations.put("Polished Oval", "Polerowany (owalny)");
        translations.put("Burnt Titanium", "Palony tytan");
        translations.put("Polished Ovals", "Polerowane owale");

        // Już po polsku (GT-115 Tytan szczotkowany, etc.) – zostawiamy
        translations.put("GT-115 Tytan szczotkowany", "GT-115 Tytan szczotkowany");
        translations.put("GT-115 Tytan palony", "GT-115 Tytan palony");
        translations.put("JET-115 Carbon", "JET-115 karbon");
        translations.put("GT-115 Polerowany", "GT-115 Polerowany");
        translations.put("GT-115 Cerakote Czarny", "GT-115 Cerakote Czarny");

        translations.put("Brushed Titanium", "Tytan szczotkowany");
        translations.put("Twin 80mm GT-115 Polished", "Podwójny 80 mm GT-115 polerowany");
        translations.put("GT-115 Titanium", "GT-115 Tytan");
        translations.put("Twin 80mm GT-115 Cerakote Black", "Podwójny 80 mm GT-115 Cerakote Czarny");
        translations.put("Twin 80mm GT-115 Titanium", "Podwójny 80 mm GT-115 Tytan");
        translations.put("Twin 80mm GT-115 Burnt Titanium", "Podwójny 80 mm GT-115 palony tytan");
        translations.put("Twin 80mm JET-115 Carbon", "Podwójny 80 mm JET-115 karbon");
        translations.put("Carbon Fibre Oval", "Owal z włókna węglowego");
        translations.put("100 Cell", "100 komórek");
        translations.put("Decat", "Bez katalizatora");
        translations.put("Quad GT-100 Titanium", "Poczwórny GT-100 tytan");
        translations.put("Quad GT-90 Burnt Titanium", "Poczwórny GT-90 palony tytan");
        translations.put("Quad Cerakote Black Oval", "Poczwórny Cerakote Czarny (owalny)");
        translations.put("Quad GT-90 Cerakote Black", "Poczwórny GT-90 Cerakote Czarny");
        translations.put("Poczwórny GT-100 Tytan palony", "Poczwórny GT-100 Tytan palony");
        translations.put("Quad GT-90 Polished", "Poczwórny GT-90 polerowany");
        translations.put("Quad GT-90 Titanium", "Poczwórny GT-90 tytan");
        translations.put("Quad Polished Oval", "Poczwórny polerowany (owalny)");
        translations.put("Poczwórny GT-100 Cerakote Czarny", "Poczwórny GT-100 Cerakote Czarny");
        translations.put("Poczwórny GT-100 Polerowany", "Poczwórny GT-100 Polerowany");
        translations.put("Titanium Oval", "Tytanowy (owalny)");
        translations.put("Burnt Titanium Oval", "Palony tytan (owalny)");
        translations.put("Poczwórny GT-100 Czarny", "Poczwórny GT-100 Czarny");
        translations.put("Quad Black Oval", "Poczwórny czarny (owalny)");
        translations.put("GT-100 Cerakote Czarny", "GT-100 Cerakote Czarny");
        translations.put("JET-100 Cerakote Black", "JET-100 Cerakote Czarny");
        translations.put("JET-100 Polished", "JET-100 polerowany");
        translations.put("GT-100 Polerowany", "GT-100 Polerowany");
        translations.put("JET-115 Carbon Fibre", "JET-115 włókno węglowe");
        translations.put("Satin Sheen Black", "Czarny satynowy połysk");
        translations.put("GT-100 Black Velvet", "GT-100 Czarny Velvet");
        translations.put("GT-100 Titanium", "GT-100 Tytan");
        translations.put("Quad Ceramic Black", "Poczwórny ceramiczny czarny");
        translations.put("Quad", "Poczwórny");
        translations.put("Twin 80mm GT-115 Brushed Titanium", "Podwójny 80 mm GT-115 tytan szczotkowany");
        translations.put("Dual GT-100", "Podwójny GT-100");
        translations.put("Quad GT-80", "Poczwórny GT-80");
        translations.put("Quad Oval Polished", "Poczwórny owal polerowany");
        translations.put("Quad Oval Cerakote Black", "Poczwórny owal Cerakote Czarny");

        // "Non-Resonated" => "Bez tłumika"
        translations.put("Non-Resonated", "Bez tłumika");

        translations.put("JET-100 Carbon", "JET-100 karbon");
        translations.put("GT-100 Tytan szczotkowany", "GT-100 Tytan szczotkowany");
        translations.put("GT-100 Burnt Titanium", "GT-100 palony tytan");
        translations.put("100mm JET-100", "JET-100 100 mm");
        translations.put("GT-100", "GT-100");
        translations.put("Dual JET-100 Polished", "Podwójny JET-100 polerowany");
        translations.put("Dual GT-100 Polished", "Podwójny GT-100 polerowany");
        translations.put("Twin Same Side", "Podwójny po jednej stronie");
        translations.put("Twin V6 Valance", "Podwójny (dla dyfuzora V6)");
        translations.put("None", "Brak");
        translations.put("Requires Cutting?", "Wymaga cięcia?");
        translations.put("Quad Black Velvet", "Poczwórny Czarny Velvet");
        translations.put("Tiptronic?", "Tiptronic?");
        translations.put("Race Version?", "Wersja torowa?");
    }

    public static void saveForShopify(List<Product> products, String outputFilePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputFilePath))) {

            // Nagłówek CSV (26 kolumn + 4 custom) = 30 kolumn
            pw.println("Handle," +                      // 1
                    "Title," +                       // 2
                    "Body (HTML)," +                 // 3
                    "Vendor," +                      // 4
                    "Product Category," +            // 5
                    "Type," +                        // 6
                    "Tags," +                        // 7
                    "Published," +                   // 8
                    "Option1 Name," +                // 9
                    "Option1 Value," +               // 10
                    "Option2 Name," +                // 11
                    "Option2 Value," +               // 12
                    "Option3 Name," +                // 13
                    "Option3 Value," +               // 14
                    "Variant SKU," +                 // 15
                    "Variant Grams," +               // 16
                    "Variant Inventory Qty," +       // 17
                    "Variant Price," +               // 18
                    "Variant Inventory Policy," +    // 19
                    "Variant Fulfillment Service," + // 20
                    "Variant Requires Shipping," +    // 21
                    "Variant Taxable," +             // 22
                    "Image Src," +                   // 23
                    "Image Position," +              // 24
                    "Gift Card," +                   // 25
                    "Status," +                      // 26
                    "custom.make," +                 // 27
                    "custom.model," +                // 28
                    "custom.variant," +              // 29
                    "custom.typeOfSystem," +         // 30
                    "custom.yearFrom," +             // 31
                    "custom.yearTo"                  // 32
            );

            for (Product product : products) {
                List<Product.VariantData> variantList = product.getVariantData();
                int variantCount = (variantList != null) ? variantList.size() : 0;

                List<String> images = product.getImagesLinks();
                int imageCount = (images != null) ? images.size() : 0;

                // Każdy produkt będzie miał tylu wierszy w CSV,
                // ile wariantów (max(variantCount, imageCount)).
                int rows = Math.max(variantCount, imageCount);

                for (int i = 0; i < rows; i++) {
                    boolean firstRow = (i == 0);
                    String customVariant = (product.getCustomVariant() != null) ? product.getCustomVariant() : "";

                    // Handle w każdym wierszu, aby Shopify wiedział, że to ten sam produkt
                    String handle = product.getHandle();
                    String goodTitle = "Układ wydechowy Milltek " + product.getCustomTypeOfSystem() + " dla " + product.getCustomMake() + " "
                            + product.getCustomModel() + " " + product.getCustomVariant() + " " + product.getCustomYearFrom()
                            + " " + product.getCustomYearTo();
                    // Pola pojawiające się tylko w pierwszym wierszu:
                    String title = firstRow ? product.getTitle() +"\n" + goodTitle : "";
                    String body = firstRow ? product.getDescription() : "";
                    String vendor = firstRow ? VENDOR : "";
                    String productCategory = firstRow ? PRODUCT_CATEGORY : "";
                    String type = firstRow ? TYPE : "";
                    String published = firstRow ? PUBLISHED : "";

                    // Domyślnie puste:
                    String option1Name = "";
                    String option1Value = "";
                    String option2Name = "";
                    String option2Value = "";
                    String option3Name = "";
                    String option3Value = "";

                    String variantSKU = "";
                    String variantPrice = "";
                    String variantInventoryPolicy = "";
                    String variantFulfillmentService = "";
                    String variantRequiresShipping = "";
                    String variantTaxable = "";

                    if (i < variantCount) {
                        Product.VariantData vData = variantList.get(i);

                        variantSKU = vData.getSku();

                        if (product.getOptionNames() != null) {
                            if (product.getOptionNames().size() > 0) {
                                option1Name = product.getOptionNames().get(0);
                                option1Value = vData.getOpt1();
                            }
                            if (product.getOptionNames().size() > 1) {
                                option2Name = product.getOptionNames().get(1);
                                option2Value = vData.getOpt2();
                            }
                            if (product.getOptionNames().size() > 2) {
                                option3Name = product.getOptionNames().get(2);
                                option3Value = vData.getOpt3();
                            }
                        }

                        // Tłumaczenie (jeśli klucz istnieje w mapie)
                        option1Name = translations.getOrDefault(option1Name, option1Name);
                        option1Value = translations.getOrDefault(option1Value, option1Value);
                        option2Name = translations.getOrDefault(option2Name, option2Name);
                        option2Value = translations.getOrDefault(option2Value, option2Value);
                        option3Name = translations.getOrDefault(option3Name, option3Name);
                        option3Value = translations.getOrDefault(option3Value, option3Value);

                        // Cena = basePrice * VAT_RATE * EXCHANGE_RATE
                        double raw = vData.getBasePrice();
                        double finalPrice = raw * VAT_RATE * EXCHANGE_RATE;
                        variantPrice = priceFormat.format(finalPrice);

                        variantInventoryPolicy = VARIANT_INVENTORY_POLICY;
                        variantFulfillmentService = VARIANT_FULFILLMENT_SERVICE;
                        variantRequiresShipping = VARIANT_REQUIRES_SHIPPING;
                        variantTaxable = VARIANT_TAXABLE;
                    }

                    // Obrazki
                    String imageSrc = (i < imageCount) ? images.get(i) : "";
                    String imagePosition = (i < imageCount) ? String.valueOf(i + 1) : "";

                    // Gift Card i Status tylko w pierwszym wierszu
                    String giftCard = firstRow ? GIFT_CARD : "";
                    String status = firstRow ? STATUS : "";

                    // Nowe kolumny custom.* i tagi
                    String customMake = (product.getCustomMake() != null) ? product.getCustomMake() : "";
                    String customModel = (product.getCustomModel() != null) ? product.getCustomModel() : "";
//                    String customVariant = (product.getCustomVariant() != null) ? product.getCustomVariant() : "";
                    String customYearFrom = (product.getCustomYearFrom() != null) ? product.getCustomYearFrom() : "";
                    String customYearTo = (product.getCustomYearTo() != null) ? product.getCustomYearTo() : "";

                    // Tag - budujemy tylko raz (firstRow). Zawiera też customVariant (o ile niepuste).
                    String tags = "";
                    if (firstRow) {
                        tags = TAGS;
                        if (!customMake.isEmpty()) {
                            tags += ", " + customMake;
                        }
                        if (!customModel.isEmpty()) {
                            tags += ", " + customModel;
                        }
                        if (!customVariant.isEmpty()) {
                            tags += ", " + customVariant;
                        }
                    }

                    // finalnie:
                    String customTypeOfSystem = (product.getCustomTypeOfSystem() != null)
                            ? translationsTypeOfSystem.getOrDefault(product.getCustomTypeOfSystem(), product.getCustomTypeOfSystem()) : "";

                    // Składamy wiersz CSV
                    StringBuilder row = new StringBuilder();
                    row.append(escapeCSV(handle)).append(",");           // 1  Handle
                    row.append(escapeCSV(title)).append(",");            // 2  Title
                    row.append(escapeCSV(body)).append(",");             // 3  Body (HTML)
                    row.append(escapeCSV(vendor)).append(",");           // 4  Vendor
                    row.append(escapeCSV(productCategory)).append(",");  // 5  Product Category
                    row.append(escapeCSV(type)).append(",");             // 6  Type
                    row.append(escapeCSV(tags)).append(",");             // 7)  Tags
                    row.append(escapeCSV(published)).append(",");        // 8  Published

                    row.append(escapeCSV(option1Name)).append(",");      // 9  Option1 Name
                    row.append(escapeCSV(option1Value)).append(",");     // 10 Option1 Value
                    row.append(escapeCSV(option2Name)).append(",");      // 11 Option2 Name
                    row.append(escapeCSV(option2Value)).append(",");     // 12 Option2 Value
                    row.append(escapeCSV(option3Name)).append(",");      // 13 Option3 Name
                    row.append(escapeCSV(option3Value)).append(",");     // 14 Option3 Value

                    row.append(escapeCSV(variantSKU)).append(",");       // 15 Variant SKU
                    row.append("0.0").append(",");                       // 16 Variant Grams
                    row.append("0").append(",");                         // 17 Variant Inventory Qty
                    row.append(escapeCSV(variantPrice)).append(",");     // 18 Variant Price
                    row.append(escapeCSV(variantInventoryPolicy)).append(",");   // 19
                    row.append(escapeCSV(variantFulfillmentService)).append(","); // 20
                    row.append(escapeCSV(variantRequiresShipping)).append(",");   // 21
                    row.append(escapeCSV(variantTaxable)).append(",");   // 22
                    row.append(escapeCSV(imageSrc)).append(",");         // 23
                    row.append(escapeCSV(imagePosition)).append(",");    // 24
                    row.append(escapeCSV(giftCard)).append(",");         // 25
                    row.append(escapeCSV(status)).append(",");           // 26

                    // 4 kolumny custom:
                    row.append(escapeCSV(customMake)).append(",");       // 27
                    row.append(escapeCSV(customModel)).append(",");      // 28
                    row.append(escapeCSV(customVariant)).append(",");    // 29
                    row.append(escapeCSV(customTypeOfSystem)).append(",");  // 30
                    row.append(escapeCSV(customYearFrom)).append(",");      // 31
                    row.append(escapeCSV(customYearTo));                    // 32 (bez przecinka)

                    pw.println(row.toString());
                }
            }

            System.out.println("Zakończono generowanie pliku CSV: " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escapowanie pól CSV: jeśli jest przecinek, cudzysłów lub nowa linia, otaczamy w ""
     * i podwajamy cudzysłowy wewnątrz.
     */
    private static String escapeCSV(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }
}
