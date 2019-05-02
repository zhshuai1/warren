package com.warren.model.repository;


import com.warren.model.TradingEntry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class TradingEntryRepository {
    private SessionFactory sessionFactory = DynamicSessionFactoryProvider.getSessionFactory();

    public void save(TradingEntry entry) {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().save(entry);
        transaction.commit();
    }

    public List<TradingEntry> getTradingEntryByStrategyAndTime(Class<?> clazz, Date time) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM TradingEntry AS rec WHERE rec.time > :time AND rec.strategy = :strategy";
        Query<TradingEntry> query = session.createQuery(hql,TradingEntry.class);
        query.setParameter("strategy", clazz.getName());
        query.setParameter("time", time);
        List<TradingEntry> entries = query.list();
        transaction.commit();
        return entries;
    }

    public List<TradingEntry> getTradingEntryByCodeAndStrategyOrderByDateDesc(String code, Class<?> clazz) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM TradingEntry AS rec WHERE rec.code = :code AND rec.strategy = :strategy ORDER BY date DESC";
        Query<TradingEntry> query = session.createQuery(hql,TradingEntry.class);
        query.setParameter("code", code);
        query.setParameter("strategy", clazz.getName());
        List<TradingEntry> entries = query.list();
        transaction.commit();
        return entries;
    }
}
