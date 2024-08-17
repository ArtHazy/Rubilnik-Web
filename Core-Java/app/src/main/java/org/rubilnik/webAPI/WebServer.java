package org.rubilnik.webAPI;

// import org.apache.logging.log4j.message.Message;
import org.rubilnik.DatabaseUtil;
import org.rubilnik.basicLogic.users.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
@Controller
public class WebServer {
    @GetMapping("/hi")
    String greeting() {
        System.out.println("GET /hi request");
        return "Hello Spring!";
    }

    @GetMapping
    String error() {return "error!";}

    @PostMapping("/user")
    void postUser(@RequestBody User user) throws Exception{
        DatabaseUtil.put(user);
    }

    // @MessageMapping("/bark")
    // @SendTo("/bark")
    // String bark(Message msg){
    //     System.out.println("message: "+msg.getFormattedMessage());
    //     return "Server bark";
    // }
    
}