package com.zebrait.hibernate;

import com.zebrait.model.Price;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class PriceRepository {
    private SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public Price get(String code) {
        return sessionFactory.getCurrentSession().get(Price.class, code);
    }

    public void save(Price price) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(price);
        transaction.commit();
    }

}
