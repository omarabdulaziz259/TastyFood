package com.example.tastyfood.util;

import java.util.HashMap;

public class CountryCode {
    private static final HashMap<String, String> COUNTRY_CODES = new HashMap<>();

    static {
        COUNTRY_CODES.put("american", "us");
        COUNTRY_CODES.put("british", "gb");
        COUNTRY_CODES.put("canadian", "ca");
        COUNTRY_CODES.put("chinese", "cn");
        COUNTRY_CODES.put("croatian", "hr");
        COUNTRY_CODES.put("dutch", "nl");
        COUNTRY_CODES.put("egyptian", "eg");
        COUNTRY_CODES.put("french", "fr");
        COUNTRY_CODES.put("greek", "gr");
        COUNTRY_CODES.put("indian", "in");
        COUNTRY_CODES.put("irish", "ie");
        COUNTRY_CODES.put("italian", "it");
        COUNTRY_CODES.put("jamaican", "jm");
        COUNTRY_CODES.put("japanese", "jp");
        COUNTRY_CODES.put("kenyan", "ke");
        COUNTRY_CODES.put("malaysian", "my");
        COUNTRY_CODES.put("mexican", "mx");
        COUNTRY_CODES.put("moroccan", "ma");
        COUNTRY_CODES.put("polish", "pl");
        COUNTRY_CODES.put("portuguese", "pt");
        COUNTRY_CODES.put("russian", "ru");
        COUNTRY_CODES.put("spanish", "es");
        COUNTRY_CODES.put("thai", "th");
        COUNTRY_CODES.put("tunisian", "tn");
        COUNTRY_CODES.put("turkish", "tr");
        COUNTRY_CODES.put("vietnamese", "vn");
    }

    public static String getCountryCode(String countryName) {
        if (countryName == null) return "";
        return COUNTRY_CODES.get(countryName.toLowerCase());
    }
}
