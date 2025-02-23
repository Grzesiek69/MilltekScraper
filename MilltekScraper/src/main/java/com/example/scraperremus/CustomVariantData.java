package com.example.scraperremus;

public class CustomVariantData { private String make; private String model; private String variant; private String yearFrom; private String yearTo;

    public CustomVariantData(String make, String model, String variant, String yearFrom, String yearTo) {
        this.make = make;
        this.model = model;
        this.variant = variant;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getVariant() {
        return variant;
    }

    public String getYearFrom() {
        return yearFrom;
    }

    public String getYearTo() {
        return yearTo;
    }

    @Override
    public String toString() {
        return "CustomVariantData{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", variant='" + variant + '\'' +
                ", yearFrom='" + yearFrom + '\'' +
                ", yearTo='" + yearTo + '\'' +
                '}';
    }
}