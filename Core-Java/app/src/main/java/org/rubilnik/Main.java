package org.rubilnik;

import java.util.List;

import org.rubilnik.basicLogic.users.User;
import org.rubilnik.webAPI.WebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        // Dependency Injection
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dependencies.xml");
        Strings strings = context.getBean("Strings", Strings.class);
        context.close();
        System.out.println(strings.getGreeting());
        
        // DatabaseUtil.test();
        try {
            DatabaseUtil.put(new User("Pentapus"));
            List<User> uList = DatabaseUtil.get(User.class);
    
            System.out.println("Users: ");
            for (User user : uList){
                System.out.println(user.getName());
            }
        } catch (Exception e) {
            System.err.println(e);
            // TODO: handle exception
        }


        //Spring Boot
        SpringApplication.run(WebServer.class, args);
    }
}
