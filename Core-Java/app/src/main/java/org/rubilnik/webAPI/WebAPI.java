package org.rubilnik.webAPI;

// import java.util.List;
import java.util.Map;
// import java.net.http.WebSocket;
import java.util.HashMap;

import org.rubilnik.basicLogic.users.User;
import org.rubilnik.webAPI.utilsAndControllers.DatabaseUtil;
import org.rubilnik.webAPI.utilsAndControllers.HTTP_Controller;
import org.springframework.boot.SpringApplication;
// import org.rubilnik.webAPI.HTTP_Controller;
// import org.springframework.boot.SpringApplication;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.socket.WebSocketSession;

public class WebAPI {
    static Map<WebSocketSession, User> webSocketUsers = new HashMap<>();
    static Map<User, WebSocketSession> webSocketSessions = new HashMap<>();

    static public void putUser(WebSocketSession session, User user) {
        webSocketUsers.put(session, user);
        webSocketSessions.put(user, session);
    }
    static public User getUser(WebSocketSession session) throws Exception{
        var res = webSocketUsers.get(session);
        if (res == null) throw new Exception("Could not find user");
        return res;
    }
    static public WebSocketSession getSession(User user) throws Exception{
        var res = webSocketSessions.get(user);
        if (res == null) throw new Exception("Could not find user");
        return res;
    }
    static public void removeUser(WebSocketSession session){
        var user = webSocketUsers.remove(session);
        webSocketSessions.remove(user);
    }
    static public void removeUser(User user){
        var session = webSocketSessions.remove(user);
        webSocketUsers.remove(session);
    }
    


    public static void main(String[] args) {
        // Dependency Injection
        // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dependencies.xml");
        // Strings strings = context.getBean("Strings", Strings.class);
        // context.close();
        // System.out.println(strings.getGreeting());
        
        // DatabaseUtil.test();
        try {
            DatabaseUtil.put(new User("Pentapus","pent@mail", "123123"));

        } catch (Exception e) { e.printStackTrace(); System.err.println("Failed to put user into database");}
        // try {
        //     DatabaseUtil.put(new User("Pentapus","pent@mail", "123123"));
        //     List<User> uList = DatabaseUtil.get(User.class, "email = 'pent@mail' and password = '123123'" );
        //     System.out.println("users selected: ");
        //     uList.forEach(user->{
        //         System.out.println(user.getName()+" : "+user.getPassword());
        //     });
            
    
        //     System.out.println("Users: ");
        //     for (User user : uList){
        //         System.out.println(user.getName());
        //     }
        // } catch (Exception e) {
        //     System.err.println(e);
        //     // TODO: handle exception
        // }


        // Spring Boot
        SpringApplication.run(HTTP_Controller.class, args);
    }
}
