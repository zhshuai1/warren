package com.warren.model.repository;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DynamicSessionFactoryProvider {
    private static SessionFactory sessionFactory = null;

    public static synchronized SessionFactory getSessionFactory() {
        if (null == sessionFactory) {
            Configuration configuration = new Configuration().configure("hibernate-dynamic.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void destroy() {
        sessionFactory.close();
    }
}
