package com.luizjacomn.billreminder.model.filter;

import com.luizjacomn.billreminder.model.enumeration.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record BillFilter(
    String title,
    List<Category> categories,
    @Positive @Max(31) Integer dueDayOfMonth,
    @Positive @Max(30) Integer businessDayOfMonth
) {

    public static BillFilter empty() {
        return new BillFilter(null, null, null, null);
    }

}
