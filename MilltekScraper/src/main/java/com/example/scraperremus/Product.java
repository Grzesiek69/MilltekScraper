package com.example.scraperremus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Product object for storing:
 *  - base price from Excel
 *  - list of variant data (with option strings & optional price adjustments)
 *  - images, description, etc.
 */
@Getter
@Setter
@ToString
public class Product {
    private String handle;
    private String url;
    private String title;
    private String description;
    private String price;    // base price from Excel, as string
    private String currency; // e.g. "PLN"
    private String sku;      // base SKU (from first variant)

    // Option group names, e.g. ["Valved?", "Trims"]
    private List<String> optionNames;

    // Detailed variant data
    private List<VariantData> variantData;

    // Image URLs
    private List<String> imagesLinks;

    // Pola do custom info
    private String customMake;
    private String customModel;
    private String customVariant;
    private String customTypeOfSystem;

    // NOWE 2 POLA:
    private String customYearFrom;
    private String customYearTo;

    /**
     * Inner class to hold a single variant's data:
     *  - sku
     *  - opt1 + priceDiff1
     *  - opt2 + priceDiff2
     *  - opt3 + priceDiff3
     */
    @Getter
    @Setter
    @ToString
    public static class VariantData {
        private String sku;
        private String opt1;
        private double diff1;
        private String opt2;
        private double diff2;
        private String opt3;
        private double diff3;

        // Cena bazowa z Excela (bez dopłat)
        private double basePrice;

        public VariantData(String sku, String opt1, double diff1,
                           String opt2, double diff2, String opt3, double diff3) {
            this.sku = sku;
            this.opt1 = opt1;
            this.diff1 = diff1;
            this.opt2 = opt2;
            this.diff2 = diff2;
            this.opt3 = opt3;
            this.diff3 = diff3;
        }
    }
}
