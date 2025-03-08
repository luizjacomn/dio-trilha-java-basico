package com.luizjacomn.billreminder.util;

import com.luizjacomn.billreminder.model.enumeration.HolidayOf2025;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    public static Integer businessDayFor(Integer businessDayOfMonth, YearMonth yearMonth) {
        var day = 1;
        var businessDaysCount = 0;

        while (businessDaysCount < businessDayOfMonth && day <= yearMonth.lengthOfMonth()) {
            if (!HolidayOf2025.isHolidayOrWeekend(day, yearMonth)) {
                businessDaysCount++;
            }

            if (businessDaysCount == businessDayOfMonth) {
                break;
            }

            day++;
        }

        return day;
    }

    public static LocalDate businessDateFor(Integer businessDayOfMonth, YearMonth yearMonth) {
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), businessDayFor(businessDayOfMonth, yearMonth));
    }

    public static Integer nextBusinessDayOf(Integer dueDayOfMonth, YearMonth yearMonth) {
        while (HolidayOf2025.isHolidayOrWeekend(dueDayOfMonth, yearMonth)) {
            dueDayOfMonth++;
        }

        return dueDayOfMonth;
    }

    public static LocalDate nextBusinessDateOf(Integer dueDayOfMonth, YearMonth yearMonth) {
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), nextBusinessDayOf(dueDayOfMonth, yearMonth));
    }

}
