package com.jeeconf.hibernate.performancetuning.basic;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.jeeconf.hibernate.performancetuning.BaseTest;
import com.jeeconf.hibernate.performancetuning.basic.entity.Consignment;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.annotation.Commit;

import java.io.Serializable;

import javax.persistence.OptimisticLockException;

public class SessionBasicTest extends BaseTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Commit
    @Test
    public void save() {
        for (String num : new String[]{"12000004571112", "12000004571115", "1250000357341"}) {
            Consignment order = new Consignment();
            order.setNumber(num);
            Serializable id = getSession().save(order);
            System.err.println("Generated ID# " + id);
            Assert.assertNotNull(id);
        }
    }

    @Commit
    @Test
    public void persist() {
        Session session = getSession();
        for (String num : new String[]{"12000004571112", "12000004571115", "1250000357341"}) {
            Consignment order = new Consignment();
            order.setNumber(num);
            session.persist(order);
            System.err.println("Generated ID# " + order.getId());
            Assert.assertNotNull(order.getId());
        }
    }

    @DatabaseSetup("/basic.xml")
    @Commit
    @Test
    public void update() {
        Session session = getSession();
        Consignment order = new Consignment();
        order.setId(1001);
        order.setNumber("55500010151");
        session.update(order);
    }

    @Test
    public void updateNotExist_NoCommit() {
        Session session = getSession();
        Consignment order = new Consignment();
        order.setId(8001);
        order.setNumber("55500010151");
        session.update(order);
    }

    @DatabaseSetup("/basic.xml")
    @Test(expected = OptimisticLockException.class)
    public void updateNotExist_Commit() {
        Session session = getSession();
        Transaction txn = session.getTransaction();

        Consignment order = new Consignment();
        order.setId(8001);
        order.setNumber("55500010151");
        session.update(order);

        txn.commit();
    }

    @Test(expected = OptimisticLockException.class)
    public void saveOrUpdate_FailedOnUpdateNotExist() {
        Session session = getSession();
        Consignment order = new Consignment();
        order.setId(8001);
        order.setNumber("55500010151");
        session.saveOrUpdate(order);
    }

    @Test
    public void merge_NotExist() {
        Session session = getSession();
        Consignment order = new Consignment();
        order.setId(8001);
        order.setNumber("55500010151");
        Object merged = session.merge(order);
        Assert.assertNotSame(order, merged);
    }

    @DatabaseSetup("/basic.xml")
    @Commit
    @Test
    public void merge_updateMerged() {
        Session session = getSession();

        Consignment order = new Consignment();
        order.setId(1003);

        Consignment merged = (Consignment) session.merge(order);
        merged.setNumber("22252070151");

        Assert.assertNotSame(order, merged);
    }

    @DatabaseSetup("/basic.xml")
    @Commit
    @Test
    public void get_modifyExist() {
        Session session = getSession();
        Consignment sameConsignmentInDB = session.get(Consignment.class, 1003);
        sameConsignmentInDB.setNumber("22252070151");
    }
}
