package org.example.mapper;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;

@UtilityClass
public class KeyValueMapper {
    public static LinkedHashMap forContent(String[] data) {
        String key = data[4];
        String value = data[5];
        String version = data[6];

        LinkedHashMap keyValue = new LinkedHashMap();
        keyValue.put("version", version);
        keyValue.put("key", key);
        keyValue.put("value", value);
        return keyValue;
    }

    public static LinkedHashMap forImage(String[] data) {
        String key = data[1];
        String value = data[2];

        LinkedHashMap keyValue = new LinkedHashMap();
        keyValue.put("key", key);
        keyValue.put("value", value);
        return keyValue;
    }
}
