package org.example.validator;

import lombok.experimental.UtilityClass;
import org.example.util.DateTimeUtil;
import org.example.value_object.datetime.DatePattern;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@UtilityClass
public class CsvDaggerValidator {
    public static void validateFetchDate(String fetchDate) {
        try {
            DateTimeUtil.toLocalDate(fetchDate, DatePattern.YYYY_MM_DD);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Fetch date is wrong format");
        }
    }

    public static boolean isFetchDateAfterEntryDate(String fetchDateStr, String entryDateStr) {
        LocalDate fetchDate = DateTimeUtil.toLocalDate(fetchDateStr, DatePattern.YYYY_MM_DD);
        LocalDate entryDate = DateTimeUtil.toLocalDate(entryDateStr, DatePattern.YYYY_MM_DD);
        return DateTimeUtil.isDateAfter(fetchDate, entryDate);
    }
}
