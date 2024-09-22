package org.rubilnik.webAPI.utilsAndControllers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;
import org.rubilnik.basicLogic.interfaces.UniqueObject;
import org.rubilnik.basicLogic.users.User;


public class DatabaseUtil {
    static SessionFactory sessionFactory;

    static {
        Configuration conf = new Configuration();
        conf.addAnnotatedClass(User.class);
        conf.addAnnotatedClass(Quiz.class);
        conf.addAnnotatedClass(Question.class);
        conf.addAnnotatedClass(Choice.class);
        
        conf.setProperty("hibernate.connection.url", System.getenv("DB_URL"));
        conf.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
        conf.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
        
        conf.setProperty("hibernate.dialect", System.getenv("HIBERNATE_DIALECT"));
        conf.setProperty("hibernate.show_sql", System.getenv("HIBERNATE_SHOW_SQL"));
        conf.setProperty("hibernate.hbm2ddl.auto", System.getenv("HIBERNATE_HBM2DDL_AUTO"));
        
        System.out.println("MY: DB URL: "+System.getenv("DB_URL"));

        DatabaseUtil.sessionFactory = conf.configure().buildSessionFactory();
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void put(Object o) throws Exception {
        Session s = sessionFactory.openSession();
        Transaction t = s.beginTransaction();
        s.persist(o);
        t.commit();
        s.close();
    }

    private static <T> String checkTableName(Class<T> cls) {
        return (cls.getSimpleName().equals("User")) ? "users": cls.getSimpleName();
    }

    public static void update(Object o) {
        var s = sessionFactory.openSession();
        var t = s.beginTransaction();
        s.merge(o);
        t.commit();
        s.close();
    }

    public static <R> List<R> get(Class<R> resultClass, String where) throws Exception {
        resultClass.getName();
        String tableName = checkTableName(resultClass);
        Session s = sessionFactory.openSession();
        var query = s.createNativeQuery("select * from "+tableName+" where "+where, resultClass);
        return query.list();
    }
    public static <R> R getById(Class<R> resultClass, Object id) throws Exception {
        Session s = sessionFactory.openSession();
        R res = s.getReference(resultClass, id);
        return res;
    }

    public static <R> R getFirst(Class<R> resultClass, String where) throws Exception {
        resultClass.getName();
        String tableName = checkTableName(resultClass);
        Session s = sessionFactory.openSession();
        var query = s.createNativeQuery("select * from "+tableName+" where "+where, resultClass);
        R result;
        try { 
            result = (R)query.list().get(0);
        } catch (IndexOutOfBoundsException e) {throw new Exception("object wasn't found in database");};
        return result;
    }
    
    public static <R> List<R> getAllOf(Class<R> resultClass) throws Exception {
        resultClass.getName();
        String tableName = checkTableName(resultClass);
        Session s = sessionFactory.openSession();
        return s.createNativeQuery("select * from "+tableName, resultClass).list();
    }

    public static void delete(UniqueObject object){
        Session s = sessionFactory.openSession();
        Transaction t = s.beginTransaction();
        String tableName = checkTableName(object.getClass());
        var id = object.getId();
        if (id instanceof String) id = "'"+id+"'";
        var query = s.createNativeQuery("delete from "+tableName+" where id="+id, object.getClass());
        query.executeUpdate();
        t.commit();
        s.close();
    }

    public static void main(String[] args) {
        // var u = new User("Test","test","123");
        // var q = u.createQuiz("testquiz");
        var c = new User("Jora", "jj","123");
        //c.createQuiz("testquiz");
        // var js0 = new JSONObject(c);
        String[] names = {"name"};
        var js = new JSONObject(c,names);
        // try {
        //     put(u);
        //     var u2 = getFirst(User.class, "id="+"'"+u.getId()+"'");
        //     var q2 = u2.getQuiz(0);
        //     var js = new JSONObject(u2, "id","email","password").toString();
        //     System.out.println("user1: "+u.getQuizzes().size());
        //     System.out.println("user2: "+u2.getQuizzes().size());
        // } catch (Exception e) {System.out.println(e.getMessage());;}

    }
}