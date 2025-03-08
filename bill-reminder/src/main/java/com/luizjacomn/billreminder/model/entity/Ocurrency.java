package com.luizjacomn.billreminder.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

@Getter
@Setter
@Entity
@Table(name = "tb_ocurrency")
@SequenceGenerator(name = "OCURRENCY_SG", sequenceName = "OCURRENCY_SEQ_ID", allocationSize = 1)
public class Ocurrency implements Serializable {

    @Serial
    private static final long serialVersionUID = 6695560609007548571L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OCURRENCY_SG")
    private Long id;

    @Column(name = "paid_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "referent_to_month")
    private Month referentToMonth;

    @Column(name = "referent_to_year")
    private Year referentToYear;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

}