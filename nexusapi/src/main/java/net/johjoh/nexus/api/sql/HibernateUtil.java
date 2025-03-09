package net.johjoh.nexus.api.sql;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import net.johjoh.nexus.api.tables.NexusCalendar;
import net.johjoh.nexus.api.tables.NexusCalendarLine;
import net.johjoh.nexus.api.tables.NexusCalendarPermission;
import net.johjoh.nexus.api.tables.NexusFamily;
import net.johjoh.nexus.api.tables.NexusList;
import net.johjoh.nexus.api.tables.NexusListLine;
import net.johjoh.nexus.api.tables.NexusListPermission;
import net.johjoh.nexus.api.tables.NexusUser;

public class HibernateUtil {
	
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
            		.addAnnotatedClass(NexusCalendar.class)
            		.addAnnotatedClass(NexusCalendarLine.class)
            		.addAnnotatedClass(NexusCalendarPermission.class)
            		.addAnnotatedClass(NexusFamily.class)
            		.addAnnotatedClass(NexusList.class)
            		.addAnnotatedClass(NexusListLine.class)
            		.addAnnotatedClass(NexusListPermission.class)
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
