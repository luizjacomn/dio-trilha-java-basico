package com.luizjacomn.billreminder.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    HOBBY("Hobby"),
    HEALTH("Health"),
    FOOD("Food"),
    TRANSPORTATION("Transportation"),
    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    HOUSING("Housing"),
    UTILITIES("Utilities"),
    TRAVEL("Travel"),
    CLOTHING("Clothing"),
    INSURANCE("Insurance"),
    INVESTMENT("Investment"),
    SAVINGS("Savings"),
    GIFTS("Gifts"),
    SUBSCRIPTIONS("Subscriptions"),
    OTHER("Other");

    private final String description;

}
