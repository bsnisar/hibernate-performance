package com.jeeconf.hibernate.performancetuning.basic.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Discount {

    @Id
    private String discountId;

    @Column
    private int appliedQuantity;

    @Column
    private Double saving;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return appliedQuantity == discount.appliedQuantity &&
                Objects.equals(discountId, discount.discountId) &&
                Objects.equals(saving, discount.saving);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountId, appliedQuantity, saving);
    }
}
