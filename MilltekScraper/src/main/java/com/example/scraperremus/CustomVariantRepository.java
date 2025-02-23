package com.example.scraperremus;

import lombok.Getter;

import java.util.ArrayList; import java.util.List;

public class CustomVariantRepository {

    @Getter
    private static final List<CustomVariantData> customVariants = new ArrayList<>();

    static {
        // Wprowadzenie danych z CSV – każdy rekord jako nowy obiekt:
        customVariants.add(new CustomVariantData("Ford", "Ranger", "", "2022", "2026"));
        customVariants.add(new CustomVariantData("Alfa Romeo", "Giulia", "Veloce", "2017", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Bronco", "6th Gen", "2021", "2026"));
        customVariants.add(new CustomVariantData("Ford", "F150", "Supercab", "2018", "2021"));
        customVariants.add(new CustomVariantData("Ford", "F150", "Supercrew", "2021", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk6 ST", "2005", "2008"));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk6 Zetec-S", "2004", "2008"));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk7 ST", "2014", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk7/Mk7.5", "2013", "2017"));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk8 and Mk8.5 ST", "2020", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk8 ST", "2018", "2020"));
        customVariants.add(new CustomVariantData("Ford", "Focus", "Mk2 RS", "2009", "2010"));
        customVariants.add(new CustomVariantData("Ford", "Focus", "Mk2 ST", "2005", "2010"));
        customVariants.add(new CustomVariantData("Ford", "Focus", "Mk3 RS", "2016", "2018"));
        customVariants.add(new CustomVariantData("Ford", "Focus", "Mk3 ST", "2012", "2018"));
        customVariants.add(new CustomVariantData("Ford", "Mondeo", "ST220", "2002", "2007"));
        customVariants.add(new CustomVariantData("Ford", "Mustang", "Dark Horse", "2024", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Mustang", "GT", "2019", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Mustang", "GT", "2015", "2018"));
        customVariants.add(new CustomVariantData("Ford", "Mustang", "Shelby GT500", "2020", "2023"));
        customVariants.add(new CustomVariantData("Ford", "Ranger", "Raptor Ranger", "2023", "2026"));
        customVariants.add(new CustomVariantData("Mercedes", "A-Class", "A35 AMG", "2019", "2026"));
        customVariants.add(new CustomVariantData("Mercedes", "A-Class", "A45 AMG", "2012", "2018"));
        customVariants.add(new CustomVariantData("Mercedes", "AMG GT", "AMG GT", "2015", "2019"));
        customVariants.add(new CustomVariantData("Mercedes", "CLA-Class", "CLA35 AMG", "2019", "2026"));
        customVariants.add(new CustomVariantData("Mercedes", "CLA-Class", "CLA45 AMG", "2013", "2018"));
        customVariants.add(new CustomVariantData("Mercedes", "G-Class", "G63", "2019", "2024"));
        customVariants.add(new CustomVariantData("Mercedes", "G-Class", "G63", "2019", "2026"));
        customVariants.add(new CustomVariantData("Mitsubishi", "Lancer Evolution", "7 / Evo 8", "2001", "2005"));
        customVariants.add(new CustomVariantData("New Mini", "Hatch", "", "2001", "2006"));
        customVariants.add(new CustomVariantData("New Mini", "Convertible", "", "2004", "2008"));
        customVariants.add(new CustomVariantData("New Mini", "Hatch", "", "2002", "2006"));
        customVariants.add(new CustomVariantData("New Mini", "Hatch", "", "2007", "2014"));
        customVariants.add(new CustomVariantData("New Mini", "Convertible", "", "2006", "2014"));
        customVariants.add(new CustomVariantData("New Mini", "Hatch", "", "2014", "2026"));
        customVariants.add(new CustomVariantData("New Mini", "Convertible", "", "2014", "2018"));
        customVariants.add(new CustomVariantData("Nissan", "350Z", "", "2003", "2010"));
        customVariants.add(new CustomVariantData("Subaru", "Impreza", "", "2001", "2005"));
        customVariants.add(new CustomVariantData("Porsche", "911", "GT3/GT3 RS", "2013", "2017"));
        customVariants.add(new CustomVariantData("Porsche", "911", "Carrera/Carrera S", "1998", "2005"));
        customVariants.add(new CustomVariantData("Porsche", "Boxster", "GTS 4.0", "2020", "2026"));
        customVariants.add(new CustomVariantData("Porsche", "Boxster", "S 3.2", "2004", "2009"));
        customVariants.add(new CustomVariantData("Porsche", "Boxster", "S 3.4", "2009", "2013"));
        customVariants.add(new CustomVariantData("Porsche", "Cayenne", "Turbo 4.8 V8", "2010", "2014"));
        customVariants.add(new CustomVariantData("Porsche", "Cayman", "S 3.4", "2004", "2009"));
        customVariants.add(new CustomVariantData("Porsche", "Cayman", "S 3.4", "2009", "2013"));
        customVariants.add(new CustomVariantData("Subaru", "Impreza", "WRX STI", "2014", "2021"));
        customVariants.add(new CustomVariantData("Toyota", "Camry", "TRD", "2017", "2024"));
        customVariants.add(new CustomVariantData("Toyota", "Tacoma", "Crew Cab/Extended Cab", "2016", "2023"));
// Wiersze z brakującymi danymi – pozostają bez zmian:
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "", "", ""));
        customVariants.add(new CustomVariantData("Ford", "Mustang", "", "", ""));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk7", "2008", "2012"));
        customVariants.add(new CustomVariantData("Toyota", "Supra", "A90 Coupe", "2019", "2026"));
        customVariants.add(new CustomVariantData("Porsche", "Boxster", "GTS 4.0", "2016", "2022"));
        customVariants.add(new CustomVariantData("Porsche", "Cayman", "GT4 4.0", "2016", "2022"));
        customVariants.add(new CustomVariantData("Porsche", "911", "992 3.0T Carrera S GTS and Dakar", "2021", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Fiesta", "Mk8 ST-Line", "2019", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Focus", "Mk4 ST", "2019", "2026"));
        customVariants.add(new CustomVariantData("Ford", "Puma", "ST", "2023", "2026"));
        customVariants.add(new CustomVariantData("Lamborghini", "Urus", "", "2018", "2026"));
        customVariants.add(new CustomVariantData("Lotus", "Emira", "2.0T AMG", "2023", "2026"));
        customVariants.add(new CustomVariantData("Lotus", "Emira", "3.5 V6 Supercharged", "2022", "2026"));
        customVariants.add(new CustomVariantData("Mercedes", "A-Class", "A35 AMG", "2019", "2021"));
        customVariants.add(new CustomVariantData("Mercedes", "CLA-Class", "CLA35 AMG", "2020", "2021"));
        customVariants.add(new CustomVariantData("New Mini", "Hatch", "", "2019", "2026"));
        customVariants.add(new CustomVariantData("New Mini", "Hatch", "", "2019", "2020"));
        customVariants.add(new CustomVariantData("Porsche", "Cayman", "GT4 4.0", "2020", "2026"));
        customVariants.add(new CustomVariantData("Toyota", "Supra", "A90 Coupe", "2021", "2026"));
        customVariants.add(new CustomVariantData("Toyota", "Yaris", "GR/GR Circuit Pack", "2020", "2024"));
        customVariants.add(new CustomVariantData("Toyota", "Yaris", "GR", "2024", "2026"));
        customVariants.add(new CustomVariantData("Porsche", "Boxster", "", "", ""));
        customVariants.add(new CustomVariantData("Nissan", "GT-R", "", "2009", "2015"));
        customVariants.add(new CustomVariantData("Subaru", "BRZ", "", "2012", "2021"));
        customVariants.add(new CustomVariantData("Toyota", "Corolla", "GR Corolla", "2023", "2026"));
        customVariants.add(new CustomVariantData("Toyota", "GT86", "", "2012", "2021"));
        customVariants.add(new CustomVariantData("Mercedes", "C-Class", "C63/C63 S", "2015", "2026"));
        customVariants.add(new CustomVariantData("Mercedes", "C-Class", "C63/C63 S", "2019", "2026"));
        customVariants.add(new CustomVariantData("Porsche", "911", "997.2", "2011", "2015"));
        customVariants.add(new CustomVariantData("Porsche", "911", "991.1 3.8", "2015", "2019"));
        customVariants.add(new CustomVariantData("Porsche", "911", "991.2 3.0", "2018", "2020"));
        customVariants.add(new CustomVariantData("Porsche", "911", "992 3.0T Carrera S GTS and Dakar", "2021", "2026"));
        customVariants.add(new CustomVariantData("Porsche", "911", "997.1 Carrera/Carrera S", "2004", "2008"));
        customVariants.add(new CustomVariantData("Porsche", "911", "997.2", "2009", "2012"));
        customVariants.add(new CustomVariantData("Porsche", "Macan", "2.9 V6 GTS and Turbo", "2021", "2026"));
        customVariants.add(new CustomVariantData("Subaru", "BRZ", "", "", ""));

    }

    /**
     * Wyszukuje rekord na podstawie: make, model, yearFrom i yearTo.
     * Zwraca null, jeśli nie znaleziono.
     */
    public static CustomVariantData findByFields(String make, String model, String yearFrom, String yearTo) {
        for (CustomVariantData cv : customVariants) {
            if (cv.getMake().equalsIgnoreCase(make) &&
                    cv.getModel().equalsIgnoreCase(model) &&
                    cv.getYearFrom().equals(yearFrom) &&
                    cv.getYearTo().equals(yearTo)) {
                return cv;
            }
        }
        return null;
    }
}