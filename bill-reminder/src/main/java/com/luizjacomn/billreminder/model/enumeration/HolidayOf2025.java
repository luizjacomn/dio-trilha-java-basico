package com.luizjacomn.billreminder.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum HolidayOf2025 {

    NEW_YEAR(LocalDate.of(2025, 1, 1), true),
    CARNIVAL_MONDAY(LocalDate.of(2025, 3, 3), false),
    CARNIVAL_TUESDAY(LocalDate.of(2025, 3, 4), false),
    GOOD_FRIDAY(LocalDate.of(2025, 4, 18), true),
    TIRADENTES(LocalDate.of(2025, 4, 21), true),
    LABOR_DAY(LocalDate.of(2025, 5, 1), true),
    CORPUS_CHRISTI(LocalDate.of(2025, 6, 19), false),
    INDEPENDENCE_DAY(LocalDate.of(2025, 9, 7), true),
    OUR_LADY_OF_APARECIDA(LocalDate.of(2025, 10, 12), true),
    ALL_SOULS_DAY(LocalDate.of(2025, 11, 2), true),
    REPUBLIC_DAY(LocalDate.of(2025, 11, 15), true),
    BLACK_CONSCIOUSNESS_DAY(LocalDate.of(2025, 11, 20), false),
    CHRISTMAS(LocalDate.of(2025, 12, 25), true);

    private final LocalDate date;

    private final boolean national;

    public static boolean isHolidayOrWeekend(Integer day) {
        return isHolidayOrWeekend(day, YearMonth.now());
    }

    public static boolean isHolidayOrWeekend(Integer day, YearMonth yearMonth) {
        var date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);

        return isHoliday(date) || isWeekend(date);
    }

    private static boolean isHoliday(LocalDate date) {
        return Stream.of(values()).anyMatch(holiday -> holiday.date.equals(date));
    }

    private static boolean isWeekend(LocalDate date) {
        return DayOfWeek.SATURDAY.equals(date.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(date.getDayOfWeek());
    }

}

