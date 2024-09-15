package org.rubilnik.webAPI.utilsAndControllers;

import org.json.JSONObject;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.users.User;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@RestController
public class HTTP_Controller {
    private ObjectMapper objectMapper_Json = new ObjectMapper();
    static class UserValidationInfo{
        public String id,password,email;
    }

    @GetMapping("/hi")
    String greeting() {
        System.out.println("GET /hi request");
        return "Hello Spring!";
    }

    @GetMapping
    String error() {return "error!";}


    static class PostUserJsonBody{
        public User user;
    }
    @CrossOrigin("*")
    @PostMapping("/user")
    ResponseEntity<?> postUser(@RequestBody String jsonString) throws Exception{

        var body = objectMapper_Json.readValue(jsonString, PostUserJsonBody.class);
        var user = new User(body.user.getName(),body.user.getEmail(),body.user.getPassword());
        DatabaseUtil.put(user);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new JSONObject().put("id",body.user.getId()).toString());
    }


    static class DeleteUserJsonBody{
        public UserValidationInfo validation;
    }
    @CrossOrigin("*")
    @DeleteMapping("/user")
    ResponseEntity<?> deleteUser(@RequestBody String jsonString) throws Exception{

        var body = objectMapper_Json.readValue(jsonString, DeleteUserJsonBody.class);

        Session session = DatabaseUtil.getSessionFactory().openSession();

        Transaction transactionGet = session.beginTransaction();
        User user = session.get(User.class, body.validation.id);
        if (!user.getPassword().equals(body.validation.password)) throw new Exception("Invalid password for user");
        transactionGet.commit();

        Transaction transactionRemove = session.getTransaction();
        session.remove(user);
        transactionRemove.commit();

        session.close();

        return ResponseEntity.ok().build();
    }


    static class PutUserJsonBody{
        public UserValidationInfo validation;
        public User user;
    }
    @CrossOrigin("*")
    @PutMapping("/user")
    void putUser(@RequestBody String jsonString) throws Exception{

        var body = objectMapper_Json.readValue(jsonString, PutUserJsonBody.class);

        var dbUser = DatabaseUtil.getFirst(User.class, "id="+"'"+body.validation.id+"'"+" and "+"password="+"'"+body.validation.password+"'");

        dbUser.setName(body.user.getName());
        dbUser.setEmail(body.user.getEmail());
        dbUser.setPassword(body.user.getPassword());

        DatabaseUtil.update(dbUser);
    }


    static class PostUserVerificationJsonBody{
        public UserValidationInfo validation;
    }
    @CrossOrigin("*")
    @PostMapping("/user/verify")
    ResponseEntity<?> postUserVerification(@RequestBody String jsonString) throws Exception {

        var body = objectMapper_Json.readValue(jsonString, PostUserVerificationJsonBody.class);

        var user = DatabaseUtil.getFirst(User.class, "password="+"'"+body.validation.password+"'"+" and "+"email="+"'"+body.validation.email+"';");
        System.out.println(user.getId()+" "+user.getName()+" ");
        return ResponseEntity.ok().build();
    }


    static class postUserGetJsonBody{
        public UserValidationInfo validation;
    }
    @CrossOrigin("*")
    @PostMapping("/user/get")
    ResponseEntity<?> postUserGet(@RequestBody String jsonString) throws Exception {

        var body = objectMapper_Json.readValue(jsonString, postUserGetJsonBody.class);

        var user = DatabaseUtil.getFirst(User.class, "password="+"'"+body.validation.password+"'"+" and "+"email="+"'"+body.validation.email+"';");
        System.out.println(user.getId()+" "+user.getName()+" ");
        return ResponseEntity.ok().body( objectMapper_Json.writeValueAsString(user));
    }

    static class PostQuizJsonBody{ // contained values can be insuffient
        public UserValidationInfo validation;
        public Quiz quiz; // not fully initialized
    }
    @CrossOrigin("*")
    @PostMapping("/quiz")
    ResponseEntity<?> postQuiz(@RequestBody String jsonString) throws Exception {
        var body = objectMapper_Json.readValue(jsonString, PostQuizJsonBody.class);
        var user = DatabaseUtil.getFirst(User.class, "id="+"'"+body.validation.id+"'"+" and "+"password="+"'"+body.validation.password+"'");
        var quiz = user.createQuiz(body.quiz.getTitle(), body.quiz.getQuestions());

        // TODO?
        
        DatabaseUtil.put(quiz);

        return ResponseEntity.ok().body(objectMapper_Json.writeValueAsString(quiz)); 
    }


    static class PutQuizJsonBody{
        public UserValidationInfo validation;
        public Quiz quiz;
    }
    @CrossOrigin("*")
    @PutMapping("/quiz")
    ResponseEntity<?> putQuiz(@RequestBody String jsonString) throws Exception {
        var body = objectMapper_Json.readValue(jsonString, PutQuizJsonBody.class);

        var quiz = DatabaseUtil.getById(Quiz.class, body.quiz.getId());
        if (!quiz.getAuthor().getId().equals(body.validation.id)) throw new Exception("Invalid quiz owner");

        body.quiz.setAuthor(quiz.getAuthor());
        body.quiz.setDateSaved(new Date());

        // var session = DatabaseUtil.getSessionFactory().openSession();
        // var transaction = session.beginTransaction();
        // session.merge(body.quiz);
        // transaction.commit();
        // session.close();

        DatabaseUtil.update(body.quiz); //TODO var quiz

        return ResponseEntity.ok().body(objectMapper_Json.writeValueAsString(body.quiz)); 
    }


    static class DeleteQuizJsonBody{
        public UserValidationInfo validation;
        public int id;
    }
    @CrossOrigin("*")
    @DeleteMapping("/quiz")
    ResponseEntity<?> deleteQuiz(@RequestBody String jsonString) throws Exception {

        var body = objectMapper_Json.readValue(jsonString, DeleteQuizJsonBody.class);

        Session session = DatabaseUtil.getSessionFactory().openSession();

        Transaction transactionGet = session.beginTransaction();
        User user = session.get(User.class, body.validation.id);
        Quiz quiz = session.get(Quiz.class, body.id);
        if (!user.getPassword().equals(body.validation.password)) throw new Exception("Invalid password");
        if (!quiz.getAuthor().getId().equals(quiz.getAuthor().getId())) throw new Exception("Invalid quiz owner");
        session.remove(quiz);
        transactionGet.commit();

        session.close();

        return ResponseEntity.ok().build(); 
    }

}