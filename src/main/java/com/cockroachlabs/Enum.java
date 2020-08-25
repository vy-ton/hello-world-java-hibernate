package com.cockroachlabs;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.Type;
import org.hibernate.cfg.Configuration;

import javax.persistence.*;
import java.util.Random;
import java.util.function.Function;

public class Enum {

    public enum Rating {
        ONE, TWO, THREE, FOUR, FIVE
    }

    @Entity
    @Table(name="reviews")
    public static class Review {
        @Id
        private Long id;

        private String message;

        @Enumerated(EnumType.STRING)
        @Column(columnDefinition = "rating")
        @Type(type="com.cockroachlabs.EnumTypePostgreSql")
        private Rating rating;

        public Review (Long id, String message, Rating rating) {
            this.id = id;
            this.message = message;
            this.rating = rating;
        }

        public Review () {}
    }

    public static void main(String[] args) {
        // Create a SessionFactory based on our hibernate.cfg.xml configuration
        // file, which defines how to connect to the database.
        SessionFactory sessionFactory =
                new Configuration()
                        .configure("hibernate.cfg.xml")
//                        .configure("pg-hibernate.cfg.xml") // uncomment to run against PG
                        .addAnnotatedClass(Review.class)
                        .buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new Review(1L, "horrible movie", Rating.TWO));
            session.getTransaction().commit();

        } finally {
            sessionFactory.close();
        }
    }
}
