package com.warren.model.repository;

import com.warren.model.StockDayInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StockDayInfoRepository {
    private SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public StockDayInfo get(StockDayInfo.StockDayInfoKey key) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        StockDayInfo stockDayInfo = session.get(StockDayInfo.class, key);
        transaction.commit();
        return stockDayInfo;
    }

    public void save(StockDayInfo stockDayInfo) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(stockDayInfo);
        transaction.commit();
    }

    public List<StockDayInfo> getStockDayInfos(String code) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query<StockDayInfo> query = session.createQuery("from StockDayInfo where code =:code", StockDayInfo.class);
        query.setParameter("code", code);
        List<StockDayInfo> stockDayInfos = query.list();
        transaction.commit();
        return stockDayInfos;
    }

}
