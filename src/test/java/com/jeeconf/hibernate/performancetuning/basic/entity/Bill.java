package com.jeeconf.hibernate.performancetuning.basic.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bill {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private BigDecimal amount;

    @Column
    private String transactionId;

    @Setter(AccessLevel.PROTECTED)
    @JoinTable(name = "billed_consignments")
    @ManyToMany
    private Set<Consignment> consignments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(amount, bill.amount) &&
                Objects.equals(transactionId, bill.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, transactionId);
    }
}
