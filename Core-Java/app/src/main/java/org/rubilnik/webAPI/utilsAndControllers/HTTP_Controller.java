package org.rubilnik.webAPI.utilsAndControllers;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.users.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
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
    @GetMapping("/hi")
    String greeting() {
        System.out.println("GET /hi request");
        return "Hello Spring!";
    }

    @GetMapping
    String error() {return "error!";}

    @CrossOrigin("*")
    @PostMapping("/user")
    ResponseEntity<?> postUser(@RequestBody String jsonString) throws Exception{
        var json = new JSONObject(jsonString);
        var newUser = new User(json.getString("name"), json.getString("email"), json.getString("password"));
        DatabaseUtil.put(newUser);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new JSONObject().put("id",newUser.getId()).toString());
    }
    @CrossOrigin("*")
    @DeleteMapping("/user")
    ResponseEntity<?> deleteUser(@RequestBody String jsonString) throws Exception{
        var json = new JSONObject(jsonString);
        var id = json.getString("id");
        var password = json.getString("password");

        Session session = DatabaseUtil.getSessionFactory().openSession();

        Transaction transactionGet = session.beginTransaction();
        User user = session.get(User.class, id);
        if (!user.getPassword().equals(password)) throw new Exception("Invalid password for user");
        transactionGet.commit();

        Transaction transactionRemove = session.getTransaction();
        session.remove(user);
        transactionRemove.commit();

        session.close();

        return ResponseEntity.ok().build();
    }
    @CrossOrigin("*")
    @PutMapping("/user")
    void putUser(@RequestBody String jsonString) throws Exception{
        var json = new JSONObject(jsonString);

        var validation = json.getJSONObject("validation");
        var validationId = validation.getString("id");
        var validationPassword = validation.getString("password");

        var user = json.getJSONObject("user");
        var userName = user.getString("name");
        var userEmail = user.getString("email");
        var userPassword = user.getString("password");

        var dbUser = DatabaseUtil.getFirst(User.class, "id="+"'"+validationId+"'"+" and "+"password="+"'"+validationPassword+"'");
        dbUser.setName(userName);
        dbUser.setEmail(userEmail);
        dbUser.setPassword(userPassword);
        DatabaseUtil.update(dbUser);
    }
    @CrossOrigin("*")
    @PostMapping("/user/verify")
    ResponseEntity<?> postUserVerification(@RequestBody String jsonString) throws Exception {
        var jsonData = new JSONObject(jsonString);
        var email = jsonData.getString("email");
        var password = jsonData.getString("password");
        var user = DatabaseUtil.getFirst(User.class, "password="+"'"+password+"'"+" and "+"email="+"'"+email+"';");
        System.out.println(user.getId()+" "+user.getName()+" ");
        return ResponseEntity.ok().build();
    }

    @CrossOrigin("*")
    @PostMapping("/user/get")
    ResponseEntity<?> postUserGet(@RequestBody String jsonString) throws Exception {
        var jsonData = new JSONObject(jsonString);
        var email = jsonData.getString("email");
        var password = jsonData.getString("password");
        var user = DatabaseUtil.getFirst(User.class, "password="+"'"+password+"'"+" and "+"email="+"'"+email+"';");
        System.out.println(user.getId()+" "+user.getName()+" ");
        ObjectMapper mapper = new ObjectMapper();
        String jsonStrRes = mapper.writeValueAsString(user);
        return ResponseEntity.ok().body(jsonStrRes.toString());
    }

    @CrossOrigin("*")
    @PostMapping("/quiz")
    ResponseEntity<?> postQuiz(@RequestBody String jsonString) throws Exception {
        var json = new JSONObject(jsonString);

        var validation = json.getJSONObject("validation");
        var id = validation.getString("id");
        var password = validation.getString("password");

        var user = DatabaseUtil.getFirst(User.class, "id="+"'"+id+"'"+" and "+"password="+"'"+password+"'");
        
        var jsonQuiz = json.getJSONObject("quiz");
        var title = jsonQuiz.getString("title");
        var quiz = user.createQuiz(title);
        var questions = jsonQuiz.getJSONArray("questions");
        for (Object objQuestion : questions) {
            if (!(objQuestion instanceof JSONObject)) throw new JSONException("invalid json");
            var jsonQuestion = (JSONObject) objQuestion;
            var question = quiz.addQuestion(jsonQuestion.getString("title"));
            var choices = jsonQuestion.getJSONArray("choices");
            for (Object objChoice : choices) {
                if (!(objChoice instanceof JSONObject)) throw new JSONException("invalid json");
                var jsonChoice = (JSONObject) objChoice;
                question.addChoice(jsonChoice.getString("title"), jsonChoice.getBoolean("correct"));
            }
        }
        DatabaseUtil.put(quiz);//TODO!
        return ResponseEntity.ok().body(new JSONObject().put("id",quiz.getId()).toString()); 
    }
    @CrossOrigin("*")
    @PutMapping("/quiz")
    ResponseEntity<?> putQuiz(@RequestBody String jsonString) throws Exception {
        var json = new JSONObject(jsonString);

        var validation = json.getJSONObject("validation");
        var id = validation.getString("id");
        var password = validation.getString("password");

        var user = DatabaseUtil.getFirst(User.class, "id="+"'"+id+"'"+" and "+"password="+"'"+password+"'");
        
        var jsonQuiz = json.getJSONObject("quiz");
        var quizId = jsonQuiz.getLong("id");
        var title = jsonQuiz.getString("title");
        var questions = jsonQuiz.getJSONArray("questions");

        var quiz = DatabaseUtil.getFirst(Quiz.class, "id="+"'"+quizId+"'");
        if (!quiz.getAuthor().getId().equals(user.getId())) throw new Exception("Invalid quiz owner");

        quiz.setTitle(title);
        quiz.setQuestions(new LinkedList<Question>());

        for (Object objQuestion : questions) {
            if (!(objQuestion instanceof JSONObject)) throw new JSONException("invalid json");
            var jsonQuestion = (JSONObject) objQuestion;
            var question = quiz.addQuestion(jsonQuestion.getString("title"));
            var choices = jsonQuestion.getJSONArray("choices");
            for (Object objChoice : choices) {
                if (!(objChoice instanceof JSONObject)) throw new JSONException("invalid json");
                var jsonChoice = (JSONObject) objChoice;
                question.addChoice(jsonChoice.getString("title"), jsonChoice.getBoolean("correct"));
            }
        }
        DatabaseUtil.update(quiz);
        return ResponseEntity.ok().body(new JSONObject().put("id",quiz.getId()).toString()); 
    }

    @CrossOrigin("*")
    @DeleteMapping("/quiz")
    ResponseEntity<?> deleteQuiz(@RequestBody String jsonString) throws Exception {
        var json = new JSONObject(jsonString);

        var validation = json.getJSONObject("validation");
        var id = validation.getString("id");
        var password = validation.getString("password");
        var quizId = json.getLong("id");

        Session session = DatabaseUtil.getSessionFactory().openSession();

        Transaction transactionGet = session.beginTransaction();
        User user = session.get(User.class, id);
        Quiz quiz = session.get(Quiz.class, quizId);
        if (!user.getPassword().equals(password)) throw new Exception("Invalid password");
        if (!quiz.getAuthor().getId().equals(quiz.getAuthor().getId())) throw new Exception("Invalid quiz owner");
        session.remove(quiz);
        transactionGet.commit();

        session.close();

        return ResponseEntity.ok().build(); 
    }

}