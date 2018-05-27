package com.zebrait.hibernate;

import com.zebrait.model.StockStatus;
import com.zebrait.model.StockStatusEntry;
import com.zebrait.strategy.Strategy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StockStatusEntryRepository {
    private SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public StockStatusEntry get(String code) {
        return sessionFactory.getCurrentSession().get(StockStatusEntry.class, code);
    }

    public void saveOrUpdate(StockStatusEntry stockStatusEntry) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(stockStatusEntry);
        transaction.commit();
    }

    public List<StockStatusEntry> getStocksByStockStatusAndStrategy(StockStatus stockStatus, Class<? extends
            Strategy> clazz) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM StockStatusEntry AS rec WHERE rec.stockStatus = :status and rec.strategy = :strategy";
        Query query = session.createQuery(hql);
        query.setParameter("status", stockStatus);
        query.setParameter("strategy", clazz.getName());
        List<StockStatusEntry> entries = query.list();
        transaction.commit();
        return entries;
    }

    public List<StockStatusEntry> getStocksByCodeAndStrategy(String code, Class<? extends
            Strategy> clazz) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM StockStatusEntry AS rec WHERE rec.code = :code and rec.strategy = :strategy";
        Query query = session.createQuery(hql);
        query.setParameter("code", code);
        query.setParameter("strategy", clazz.getName());
        List<StockStatusEntry> entries = query.list();
        transaction.commit();
        return entries;
    }

    public void freeStock(StockStatusEntry stockStatusEntry) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String hql = "DELETE FROM StockStatusEntry AS rec WHERE rec.code= :code AND rec.strategy = :strategy";
        Query query = session.createQuery(hql);
        query.setParameter("strategy", stockStatusEntry.getStrategy());
        query.setParameter("code", stockStatusEntry.getCode());
        query.executeUpdate();
        transaction.commit();
    }
}
