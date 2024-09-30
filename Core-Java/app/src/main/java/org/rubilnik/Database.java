package org.rubilnik;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;
import org.rubilnik.basicLogic.users.User;

import jakarta.persistence.Table;

public class Database {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // configure database structure with annotated classes
    static {
        var config = new Configuration();
        config.addAnnotatedClass(User.class);
        config.addAnnotatedClass(Quiz.class);
        config.addAnnotatedClass(Question.class);
        config.addAnnotatedClass(Choice.class);
        sessionFactory = config.configure().buildSessionFactory();
    }
    
    
    // get default or annotated database table name (for sql queries)
    public static <T> String getTableName(Class<T> c){
        var annotation = c.getAnnotation(Table.class);
        if (annotation != null) {
            return annotation.name();
        } else { 
            return c.getSimpleName();
        }
    }
}
