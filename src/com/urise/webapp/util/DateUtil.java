package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * gkislin
 * 20.07.2016
 */
public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String notNullDate(LocalDate date) {
        if (date == null) return "";
        return date.isAfter(LocalDate.now()) ? "По настоящее время" : date.format(DateTimeFormatter.ofPattern("MM-yyyy"));
    }

    public static LocalDate parse(String value){
        if (value.equals("По настоящее время")) return DateUtil.NOW;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth yearMonth = YearMonth.parse(value, formatter);
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }
}
