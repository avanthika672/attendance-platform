package com.zepic.attendance_platform.util;

public final class CityNormalizer {
    private CityNormalizer(){}
    public static String normalize(String city){
        return city
                .trim()
                .replaceAll("[_-]+"," ")
                .replaceAll("\\s+"," ")
                .toLowerCase();
    }
}
