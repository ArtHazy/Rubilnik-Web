package org.rubilnik;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.rubilnik.basicLogic.users.User;
import org.rubilnik.webAPI.HTTP_Controller;
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
            DatabaseUtil.put(new User("Pentapus","pent@mail", "123123"));
            List<User> uList = DatabaseUtil.get(User.class, "email = 'pent@mail' and password = '123123'" );
            System.out.println("users selected: ");
            uList.forEach(user->{
                System.out.println(user.getName()+" : "+user.getPassword());
            });
            
    
            System.out.println("Users: ");
            for (User user : uList){
                System.out.println(user.getName());
            }
        } catch (Exception e) {
            System.err.println(e);
            // TODO: handle exception
        }


        //Spring Boot
        SpringApplication.run(HTTP_Controller.class, args);
    }
}