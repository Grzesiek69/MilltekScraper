package com.example.scraperremus;

import lombok.Getter;

/**
 * Simple POJO for Excel-based info (make, model, variant, yearFrom, yearTo, system type, price).
 */
@Getter
public class ProductMetadata {
    private String make;
    private String model;
    private String variant;

    private String yearFrom; // Nowe pole
    private String yearTo;   // Nowe pole

    private String typeOfSystem;
    private double price;

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
