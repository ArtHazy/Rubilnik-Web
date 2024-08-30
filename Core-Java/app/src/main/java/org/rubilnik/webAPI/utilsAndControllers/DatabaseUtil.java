package org.rubilnik.webAPI.utilsAndControllers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;
import org.rubilnik.basicLogic.interfaces.UniqueObject;
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

    private static <T> String checkTableName(Class<T> cls) {
        return (cls.getSimpleName().equals("User")) ? "users": cls.getSimpleName();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <R> List<R> get(Class<R> resultClass, String where) throws Exception {
        resultClass.getName();
        String tableName = checkTableName(resultClass);
        Session s = sf.openSession();
        NativeQuery query = s.createNativeQuery("select * from "+tableName+" where "+where, resultClass);
        return query.list();
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <R> R getFirst(Class<R> resultClass, String where) throws Exception {
        resultClass.getName();
        String tableName = checkTableName(resultClass);
        Session s = sf.openSession();
        NativeQuery query = s.createNativeQuery("select * from "+tableName+" where "+where, resultClass);
        R result;
        try {
            result = (R)query.list().get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new Exception("object wasn't found in database");
        }
        
        return result;
    }
    
    public static <R> List<R> getAllOf(Class<R> resultClass) throws Exception {
        resultClass.getName();
        String tableName = checkTableName(resultClass);
        Session s = sf.openSession();
        return s.createNativeQuery("select * from "+tableName, resultClass).list();
    }

    @SuppressWarnings("deprecation")
    public static void del(UniqueObject object){
        Session s = sf.openSession();
        String tableName = checkTableName(object.getClass()); 
        var query = s.createNativeQuery("delete from "+tableName+" where id="+"'"+object.getId()+"'");
        query.list();
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
