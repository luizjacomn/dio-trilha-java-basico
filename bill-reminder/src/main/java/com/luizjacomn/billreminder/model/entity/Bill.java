package com.luizjacomn.billreminder.model.entity;

import com.luizjacomn.billreminder.model.enumeration.AppellantType;
import com.luizjacomn.billreminder.model.enumeration.Category;
import com.luizjacomn.billreminder.util.DateUtil;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "tb_bill")
@SequenceGenerator(name = "BILL_SG", sequenceName = "BILL_SEQ_ID", allocationSize = 1)
public class Bill implements Serializable {

    @Serial
    private static final long serialVersionUID = 3366561574327888776L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BILL_SG")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_day_of_month")
    private Integer dueDayOfMonth;

    @Column(name = "adjust_to_next_business_day")
    private Boolean adjustToNextBusinessDay;

    @Column(name = "business_day_of_month")
    private Integer businessDayOfMonth;

    @Enumerated(EnumType.STRING)
    @Column(name = "appellant_type")
    private AppellantType appellantType;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    @CollectionTable(name = "tb_bill_categories", joinColumns = @JoinColumn(name = "bill_id"))
    private List<Category> categories = new ArrayList<>();

    public Integer adjustDueDate() {
        return this.adjustDueDate(YearMonth.now());
    }

    public Integer adjustDueDate(YearMonth yearMonth) {
        var day = dueDayOfMonth;

        if (Objects.nonNull(day) && Boolean.TRUE.equals(adjustToNextBusinessDay)) {
            day = DateUtil.nextBusinessDayOf(day, yearMonth);
        } else if (Objects.nonNull(businessDayOfMonth)) {
            day = DateUtil.businessDayFor(businessDayOfMonth, yearMonth);
        }

        return day;
    }

    public LocalDate nextPaymentDate() {
        if (AppellantType.ANNUAL.equals(appellantType)) {
            return null;
        }

        var nextMonth = YearMonth.now().plusMonths(1);

        var nextDay = this.adjustDueDate(nextMonth);

        return LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), nextDay);
    }

    public String frequency() {
        var frequency = new StringBuilder();

        if (AppellantType.MONTHLY.equals(appellantType)) {
            if (Objects.nonNull(dueDayOfMonth)) {
                frequency.append("Os pagamentos são realizados todos os dias ").append(dueDayOfMonth).append(" do mês");
            } else if (Objects.nonNull(businessDayOfMonth)) {
                frequency.append("Os pagamentos são realizados no ").append(businessDayOfMonth).append("° dia útil do mês");
            } else {
                return "O pagamento só será realizado um vez!";
            }
        } else if (AppellantType.ANNUAL.equals(appellantType)) {
            frequency.append("Os pagamentos são realizados todos os anos");
        } else {
            return "O pagamento só será realizado um vez!";
        }

        return frequency.toString();
    }
}
