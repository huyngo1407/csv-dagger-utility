package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class MapUtil {
//    public static <K, V> Map<K, V> filterByKey(Map<K, V> map, Predicate<V> predicate) {
//        return map.entrySet()
//                .stream()
//                .filter(entry -> predicate.test(entry.getKey()))
//                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
//    }

    public static <K, V> Map<K, V> filterByValue(Map<K, V> map, Predicate<V> predicate) {
        return map.entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
