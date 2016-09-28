package com.jeeconf.hibernate.performancetuning.basic;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.jeeconf.hibernate.performancetuning.BaseTest;
import com.jeeconf.hibernate.performancetuning.basic.entity.Bill;
import com.jeeconf.hibernate.performancetuning.basic.entity.Consignment;
import com.jeeconf.hibernate.performancetuning.basic.entity.Delivery;
import com.jeeconf.hibernate.performancetuning.basic.entity.Discount;
import com.jeeconf.hibernate.performancetuning.basic.entity.Item;
import com.jeeconf.hibernate.performancetuning.basic.entity.Waybill;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.annotation.Commit;

import java.math.BigDecimal;

public class AssociationsTest extends BaseTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Commit
    @Test
    public void save_oneToMany() {
        Consignment order = new Consignment();
        order.setNumber("1288800151");

        Item item = new Item();
        item.setSku("_42FJVI399D");
        item.setQuantity(1);

        order.withItem(item);

        Session session = getSession();
        session.save(order);
    }

    @DatabaseSetup("/basic.xml")
    @Test
    public void delete_oneToManyNotPersist() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Removing a detached instance com.jeeconf.hibernate.performancetuning.basic.entity.Consignment#1002");

        new Txn(getSession()).inTxn(session -> {
            Consignment cons = new Consignment();
            cons.setId(1002);
            session.delete(cons);
        });
    }

    @DatabaseSetup("/basic.xml")
    @Commit
    @Test
    public void delete_oneToManyPersist() {
        Consignment cons = getSession().get(Consignment.class, 1002);
        getSession().delete(cons);
    }

    @Commit
    @Test
    public void oneToMany_unidirectional() {
        Consignment cons = new Consignment();
        cons.setNumber("80000001002");

        Discount discount = new Discount();
        discount.setDiscountId("1255501");
        discount.setAppliedQuantity(4);
        discount.setSaving(400D);

        cons.withDiscount(discount);

        getSession().save(cons);
    }

    @Commit
    @Test
    public void manyToMany_simpleColumn() {
        Consignment cons = new Consignment();
        cons.setNumber("80000001003");

        Bill bill = new Bill();
        bill.setAmount(BigDecimal.TEN);
        bill.setTransactionId("20160928100001");

        cons.withBill(bill);

        getSession().save(cons);
    }

    @Test
    @Commit
    public void manyToMany_extraColumns() {
        Session session = getSession();

        Consignment cons = new Consignment();
        cons.setNumber("80000001004");
        session.save(cons);

        Delivery delivery = new Delivery();
        delivery.setAccountNumber("F1500DRJ-1000");
        delivery.addConsignment(cons);

        session.save(delivery);
    }


    @Test
    @Commit
    public void oneToMany_listCollection() {
        Delivery delivery = new Delivery();
        delivery.setAccountNumber("F1500DRJ-1000");

        Waybill first = new Waybill();
        first.setDeliveryCode("WB001");

        Waybill second = new Waybill();
        second.setDeliveryCode("WB002");

        delivery.addWaybills(first, second);

        getSession().save(delivery);
    }


    @DatabaseSetup("/basic.xml")
    @Test
    @Commit
    public void oneToMany_deleteOrphan() {
        Delivery delivery = getSession().get(Delivery.class, 5001L);
        Assert.assertEquals(2, delivery.getWaybills().size());

        delivery.getWaybills().remove(1);
    }
}
