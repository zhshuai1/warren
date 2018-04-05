package com.zebrait.hibernate;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryProvider {
    private static SessionFactory sessionFactory = null;

    public static synchronized SessionFactory getSessionFactory() {
        if (null == sessionFactory) {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void destroy() {
        sessionFactory.close();
    }
}
