package com.jeeconf.hibernate.performancetuning.basic.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String accountNumber;

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "id.delivery", cascade = CascadeType.ALL)
    private Set<ConsignmentDelivery> deliveries = new HashSet<>();

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "delivery", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderColumn(name = "waybill_seq", columnDefinition = "INT")
    private List<Waybill> waybills = new ArrayList<>();

    public Delivery addConsignment(Consignment consignment) {
        ConsignmentDelivery consignmentDelivery = new ConsignmentDelivery();

        consignmentDelivery.getId().setConsignment(consignment);
        consignmentDelivery.getId().setDelivery(this);

        consignment.getDeliveries().add(consignmentDelivery);
        this.getDeliveries().add(consignmentDelivery);

        return this;
    }

    public Delivery addWaybills(Waybill...waybills) {
        for (Waybill waybill : waybills) {
            this.waybills.add(waybill);
            waybill.setDelivery(this);
        }
        return this;
    }
}
