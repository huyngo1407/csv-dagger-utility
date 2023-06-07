package org.example.value_object.datetime;

import lombok.Getter;

@Getter
public enum DateTimePattern {
    SUBTRACT_YYYY_MM_DD_HH_MM_SS_SSS_Z("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    SUBTRACT_DD_MM_YYYY_HH_MM_SS_SSS_Z("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'"),
    SLASH_YYYY_MM_DD_HH_MM_SS_SSS_Z("yyyy/MM/dd'T'HH:mm:ss.SSS'Z'"),
    SLASH_DD_MM_YYYY_HH_MM_SS_SSS_Z("dd/MM/yyyy'T'HH:mm:ss.SSS'Z'"),
    SUBTRACT_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    SUBTRACT_DD_MM_YYYY_HH_MM_SS("dd-MM-yyyy HH:mm:ss"),
    SLASH_YYYY_MM_DD_HH_MM_SS("yyyy/MM/dd HH:mm:ss"),
    SLASH_DD_MM_YYYY_HH_MM_SS("dd/MM/yyyy HH:mm:ss"),
    SUBTRACT_YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    SUBTRACT_DD_MM_YYYY_HH_MM("dd-MM-yyyy HH:mm"),
    SLASH_YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm"),
    SLASH_DD_MM_YYYY_HH_MM("dd/MM/yyyy HH:mm");

    private static final DateTimePattern[] VALUES = DateTimePattern.values();
    private final String value;

    DateTimePattern(String value) {
        this.value = value;
    }

    public static DateTimePattern[] getValues() {
        return VALUES;
    }
}
