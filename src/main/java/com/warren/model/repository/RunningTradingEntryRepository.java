package com.warren.model.repository;


import com.warren.model.RunningTradingEntry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class RunningTradingEntryRepository {
    private SessionFactory sessionFactory = DynamicSessionFactoryProvider.getSessionFactory();

    public void save(RunningTradingEntry entry) {
        Transaction transaction = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().save(entry);
        transaction.commit();
    }

    public List<RunningTradingEntry> getTradingEntryByStrategyAndTime(Class<?> clazz, Date time) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM RunningTradingEntry AS rec WHERE rec.time > :time AND rec.strategy = :strategy";
        Query<RunningTradingEntry> query = session.createQuery(hql, RunningTradingEntry.class);
        query.setParameter("strategy", clazz.getName());
        query.setParameter("time", time);
        List<RunningTradingEntry> entries = query.list();
        transaction.commit();
        return entries;
    }

    public List<RunningTradingEntry> getTradingEntryByCodeAndStrategyOrderByDateDesc(String code, Class<?> clazz) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM RunningTradingEntry AS rec WHERE rec.code = :code AND rec.strategy = :strategy ORDER BY date DESC";
        Query<RunningTradingEntry> query = session.createQuery(hql, RunningTradingEntry.class);
        query.setParameter("code", code);
        query.setParameter("strategy", clazz.getName());
        List<RunningTradingEntry> entries = query.list();
        transaction.commit();
        return entries;
    }

    public List<RunningTradingEntry> getLastTradingEntryByCodeAndStrategy(String code, Class<?> clazz) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM TradingEntry AS rec WHERE rec.code = :code AND rec.strategy = :strategy ORDER BY date DESC LIMIT 1";
        Query<RunningTradingEntry> query = session.createQuery(hql, RunningTradingEntry.class);
        query.setParameter("code", code);
        query.setParameter("strategy", clazz.getName());
        List<RunningTradingEntry> entries = query.list();
        transaction.commit();
        return entries;
    }
}
