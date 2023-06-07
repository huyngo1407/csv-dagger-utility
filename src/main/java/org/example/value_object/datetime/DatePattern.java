package org.example.value_object.datetime;

import lombok.Getter;

@Getter
public enum DatePattern {
    SUBTRACT_YYYY_MM_DD("yyyy-MM-dd"),
    SUBTRACT_DD_MM_YYYY("dd-MM-yyyy"),
    SLASH_YYYY_MM_DD("yyyy/MM/dd"),
    SLASH_DD_MM_YYYY("dd/MM/yyyy"),
    YYYY_MM_DD("yyyyMMdd"),
    DD_MM_YYYY("ddMMyyyy");

    private static final DatePattern[] VALUES = DatePattern.values();
    private final String value;

    DatePattern(String value) {
        this.value = value;
    }

    public static DatePattern[] getValues() {
        return VALUES;
    }
}
