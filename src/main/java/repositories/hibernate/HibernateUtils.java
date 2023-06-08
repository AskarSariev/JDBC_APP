package repositories.hibernate;

import models.Developer;
import models.Skill;
import models.Specialty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Developer.class)
                    .addAnnotatedClass(Skill.class)
                    .addAnnotatedClass(Specialty.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }
}
