package com.jeeconf.hibernate.performancetuning.basic.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Consignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String number;

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="consignment_id", referencedColumnName="id")
    private Set<Discount> discounts = new HashSet<>();

    @Setter(AccessLevel.PROTECTED)
    @ManyToMany(mappedBy = "consignments", cascade = CascadeType.ALL)
    private Set<Bill> bills = new HashSet<>();

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "id.consignment", cascade = CascadeType.ALL)
    private Set<ConsignmentDelivery> deliveries = new HashSet<>();

    public Consignment withItem(Item item) {
        items.add(item);
        item.setOrder(this);
        return this;
    }

    public Consignment withDiscount(Discount discount) {
        discounts.add(discount);
        return this;
    }

    public Consignment withBill(Bill bill) {
        bills.add(bill);
        bill.getConsignments().add(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consignment that = (Consignment) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
