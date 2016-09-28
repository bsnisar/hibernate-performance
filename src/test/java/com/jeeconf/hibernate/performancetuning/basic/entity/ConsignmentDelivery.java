package com.jeeconf.hibernate.performancetuning.basic.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AssociationOverrides({
        @AssociationOverride(name = "id.consignment",
                joinColumns = @JoinColumn(name = "CONS_ID")),
        @AssociationOverride(name = "id.delivery",
                joinColumns = @JoinColumn(name = "DELIVERY_ID"))
})
public class ConsignmentDelivery {

    @EmbeddedId
    private DeliveryID id = new DeliveryID();

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date registration = new Date();

    @Getter @Setter
    @EqualsAndHashCode
    @Embeddable
    public static class DeliveryID  implements Serializable {
        @ManyToOne
        private Consignment consignment;
        @ManyToOne
        private Delivery delivery;
    }
}
