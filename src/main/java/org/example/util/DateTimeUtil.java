package org.example.util;

import lombok.experimental.UtilityClass;
import org.example.value_object.datetime.DatePattern;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {
    public static LocalDate toLocalDate(String value, DatePattern datePattern) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(datePattern.getValue()));
    }

    public static boolean isDateAfter(LocalDate firstDate, LocalDate secondDate) {
        return firstDate.isAfter(secondDate);
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDate toLocalDate(Timestamp timestamp) {
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}