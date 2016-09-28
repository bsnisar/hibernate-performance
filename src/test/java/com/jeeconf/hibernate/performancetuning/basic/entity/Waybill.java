package com.jeeconf.hibernate.performancetuning.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Waybill {

    @Id
    @Column(name = "DELIVERY_CODE")
    private String deliveryCode;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}
