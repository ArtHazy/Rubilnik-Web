package org.rubilnik.webAPI;

import org.rubilnik.DatabaseUtil;
import org.rubilnik.basicLogic.users.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebServer {
    @GetMapping(value = "/hi")
    String greeting() {return "Hello Spring!";}

    @GetMapping
    String error() {return "error!";}

    @PostMapping(value = "/user")
    void postUser(@RequestBody User user) throws Exception{
        DatabaseUtil.put(user);
    }
}