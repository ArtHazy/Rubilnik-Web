package org.rubilnik;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;
import org.rubilnik.basicLogic.users.User;



public class DatabaseUtil {
    static SessionFactory sf;
    static {
        Configuration conf = new Configuration();
        conf.addAnnotatedClass(User.class);
        conf.addAnnotatedClass(Quiz.class);
        conf.addAnnotatedClass(Question.class);
        conf.addAnnotatedClass(Choice.class);
        DatabaseUtil.sf = conf.configure().buildSessionFactory();
    }
    
    public static void put(Object o) throws Exception {
        sf.inTransaction((session)->{
            session.persist(o);
        });
    }

    public static <R> List<R> get(Class<R> resultClass, String where) throws Exception {
        resultClass.getName();
        String tableName;
        if (resultClass.getSimpleName().equals("User")) tableName = "users";
        else tableName = resultClass.getSimpleName();
        Session s = sf.openSession();
        NativeQuery query = s.createNativeQuery("select * from "+tableName+" where "+where, resultClass);
        return query.list();
    }
    
    public static <R> List<R> getAllOf(Class<R> resultClass) throws Exception {
        resultClass.getName();
        String tableName;
        if (resultClass.getSimpleName().equals("User")) tableName = "users";
        else tableName = resultClass.getSimpleName();
        Session s = sf.openSession();
        return s.createNativeQuery("select * from "+tableName, resultClass).list();
    }


    
    // DatabaseUtil(){}
    public static void test(){
        User u = new User("Jacob", "jaaa@mail", "123");
        // or
        // sf.inTransaction((session)->{
        //     session.persist(u);
        // });
        // or
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        s.persist(u);
        t.commit();
        s.close();
        //
    }
}
