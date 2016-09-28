package com.jeeconf.hibernate.performancetuning.basic;


import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Consumer;

public class Txn {
    private final Session session;

    public Txn(Session session) {
        this.session = session;
    }

    void inTxn(Consumer<Session> callback) {
        Transaction txn = session.getTransaction();
        callback.accept(session);
        txn.commit();
    }
}
