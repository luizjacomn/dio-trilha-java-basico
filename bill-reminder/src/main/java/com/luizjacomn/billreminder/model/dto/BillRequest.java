package com.luizjacomn.billreminder.model.dto;

import com.luizjacomn.billreminder.model.entity.Bill;
import com.luizjacomn.billreminder.model.enumeration.AppellantType;
import com.luizjacomn.billreminder.model.enumeration.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

public record BillRequest(
    @NotBlank String title,
    AppellantType appellantType,
    @Positive BigDecimal amount,
    @Positive @Max(31) Integer dueDayOfMonth,
    @Positive @Max(30) Integer businessDayOfMonth,
    Boolean adjustToNextBusinessDay,
    Set<Category> categories
) {

    public Bill toEntity() {
        var bill = new Bill();
        bill.setTitle(title);
        bill.setAppellantType(appellantType);
        bill.setAmount(amount);
        bill.setDueDayOfMonth(dueDayOfMonth);
        bill.setBusinessDayOfMonth(businessDayOfMonth);
        bill.setCategories(new ArrayList<>(categories));

        return bill;
    }

}
