package org.rubilnik.webAPI;

// import java.util.List;
import java.util.Map;
// import java.net.http.WebSocket;
import java.util.HashMap;

import org.rubilnik.basicLogic.Room;
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

    static private void _log_users(){
        System.out.println("Users:");
        webSocketUsers.forEach((session,user)->{
            System.out.println("  "+user.getId()+":"+user.getName()+"  room: "+user.getRoom().getId());
        });
    }

    static public void putUser(WebSocketSession session, User user) {
        webSocketUsers.put(session, user);
        webSocketSessions.put(user, session);
        _log_users();
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
        _log_users();
    }
    static public void removeUser(User user){
        var session = webSocketSessions.remove(user);
        webSocketUsers.remove(session);
        _log_users();
    }

    public static void main(String[] args) {
        // Dependency Injection
        // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dependencies.xml");
        // Strings strings = context.getBean("Strings", Strings.class);
        // context.close();
        // System.out.println(strings.getGreeting());
        
        // DatabaseUtil.test();
        try {
            if (System.getenv("HIBERNATE_HBM2DDL_AUTO").equals("create")){
                var u = new User("admin","admin", "admin");
                u.createQuiz("test").addQuestion("question1").addChoice("true", true).addChoice("false", false);
                var u2 = new User("player","player", "player");
                DatabaseUtil.put(u);
                DatabaseUtil.put(u2);
            }
        } catch (Exception e) { e.printStackTrace(); System.err.println("Failed to put user into database");}
        // Spring Boot
        SpringApplication.run(HTTP_Controller.class, args);
    }
}
