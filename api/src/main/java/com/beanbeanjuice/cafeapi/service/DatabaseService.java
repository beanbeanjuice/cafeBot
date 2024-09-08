package com.beanbeanjuice.cafeapi.service;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Getter
public class DatabaseService {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // A SessionFactory is set up once for an application!
//            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//                    .configure() // configures settings from hibernate.cfg.xml
//                    .build();

            Configuration c = new Configuration();
            c.configure();

            String url = String.format("jdbc:mysql://%s:%s/cafeBot-v2", System.getenv("MYSQL_URL"), System.getenv("MYSQL_PORT"));

            c.setProperty("hibernate.connection.url", url);
            c.setProperty("hibernate.connection.username", System.getenv("MYSQL_USERNAME"));
            c.setProperty("hibernate.connection.password", System.getenv("MYSQL_PASSWORD"));

            try {
//                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                sessionFactory = c.buildSessionFactory();
            }
            catch (Exception e) {
                // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
                // so destroy it manually.
                e.printStackTrace();
//                StandardServiceRegistryBuilder.destroy( registry );
            }
        }

        return sessionFactory;
    }

}
