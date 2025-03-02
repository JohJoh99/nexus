package net.johjoh.nexus.api.sql;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import net.johjoh.nexus.api.tables.NexusUser;

public class HibernateUtil {
	
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
            		.addAnnotatedClass(NexusUser.class)
            		.configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

}
