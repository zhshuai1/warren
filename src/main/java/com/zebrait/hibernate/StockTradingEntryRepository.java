package com.zebrait.hibernate;

import com.zebrait.model.StockTradingEntry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class StockTradingEntryRepository {
    private SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public void save(StockTradingEntry entry) {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().save(entry);
        transaction.commit();
    }

    public List<StockTradingEntry> getStockTradingEntryByStrategyAndTime(Class<?> clazz, Date time) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM StockTradingEntry AS rec WHERE rec.time > :time and rec.strategy = :strategy";
        Query query = session.createQuery(hql);
        query.setParameter("time", time);
        query.setParameter("strategy", clazz.getName());
        List<StockTradingEntry> entries = query.list();
        transaction.commit();
        return entries;
    }
}
