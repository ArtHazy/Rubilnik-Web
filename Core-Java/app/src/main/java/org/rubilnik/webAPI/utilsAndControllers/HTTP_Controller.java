package org.rubilnik.webAPI.utilsAndControllers;
import org.json.JSONObject;
import org.rubilnik.basicLogic.users.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
@Controller
public class HTTP_Controller {
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

    @SuppressWarnings("rawtypes")
    @CrossOrigin("*")
    @PostMapping("/user/verify")
    ResponseEntity postUserVerification(@RequestBody String jsonString) throws Exception {
        System.out.println("jsonString: " + jsonString);
        JSONObject jsonData = new JSONObject(jsonString);
        String email = jsonData.getString("email");
        String password = jsonData.getString("password");
        User user = DatabaseUtil.getFirst(User.class, "password="+"'"+password+"'"+" and "+"email="+"'"+email+"';");
        System.out.println(user.getId()+" "+user.getName()+" ");
        return ResponseEntity.ok().body(new JSONObject(user).toString());
    }
}