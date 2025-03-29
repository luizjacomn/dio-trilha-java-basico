package com.luizjacomn.billreminder.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.luizjacomn.billreminder.model.entity.Bill;
import com.luizjacomn.billreminder.model.enumeration.AppellantType;
import com.luizjacomn.billreminder.model.enumeration.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
public record BillResponse(
    Long id,
    String title,
    Boolean isAppellant,
    AppellantType appellantType,
    BigDecimal amount,
    String frequency,
    LocalDate nextPaymentDate,
    List<Category> categories
) {

    public BillResponse(Bill bill) {
        this(
            bill.getId(),
            bill.getTitle(),
            Objects.nonNull(bill.getAppellantType()),
            bill.getAppellantType(),
            bill.getAmount(),
            bill.frequency(),
            bill.nextPaymentDate(),
            bill.getCategories()
        );
    }

}
