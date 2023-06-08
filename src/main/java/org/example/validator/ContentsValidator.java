package org.example.validator;

import lombok.experimental.UtilityClass;
import org.example.util.DateTimeUtil;
import org.example.value_object.datetime.DatePattern;

import java.time.LocalDate;

@UtilityClass
public class ContentsValidator {
    public static boolean isContentTypeValid(String contentType) {
        return contentType.equals("label") || contentType.equals("error");
    }

    public static boolean isLastFetchDateAfterEntryDate(String lastFetchDateStr, String entryDateStr) {
        LocalDate lastFetchDate = DateTimeUtil.toLocalDate(lastFetchDateStr, DatePattern.YYYY_MM_DD);
        LocalDate entryDate = DateTimeUtil.toLocalDate(entryDateStr, DatePattern.YYYY_MM_DD);
        return DateTimeUtil.isDateAfter(lastFetchDate, entryDate);
    }
}
