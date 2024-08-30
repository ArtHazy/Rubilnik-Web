package org.rubilnik.webAPI.utilsAndControllers;
import org.rubilnik.basicLogic.users.Player;
import org.rubilnik.basicLogic.users.User;

import java.io.IOException;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.rubilnik.webAPI.WebAPI;
import org.rubilnik.webAPI.WsJsonResponces.*;
import org.rubilnik.basicLogic.BasicLogic;


@Configuration
@EnableWebSocket
public class WS_Configuration implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWSHandler(), "/room").setAllowedOrigins("*");

    }

    class MyWSHandler extends BinaryWebSocketHandler{
        WebSocketSession session;
        JSONObject data;

        final private static int START = 0;
        final private static int NEXT = 1;
        final private static int END = 2;
        final private static int JOIN = 0;
        final private static int CREATE = 1;

        private void messageTo(Set<User> users, TextMessage message) throws IOException{
            for (User user : users){
                messageTo(user, message);
            }
        }
        private void messageTo(User user, TextMessage message){
            try {
                var session = WebAPI.getSession(user);
                session.sendMessage(message);
            } catch (Exception e) {
                // TODO 
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            this.session = session;
            this.data = null;
            handleLeave();
            super.afterConnectionClosed(session, status);
        }
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            super.afterConnectionEstablished(session);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) {
            session.getUri();
            String payload = message.getPayload();
            var json = new JSONObject(payload);
            var event = json.getString("event");
            this.data = json.getJSONObject("data");
            this.session = session;
            switch(event){
                case "create": handleJoinOrCreate(CREATE); break;
                case "join": handleJoinOrCreate(JOIN); break;
                case "bark": handleBark(); break;
                case "choice": handleChoice(); break;
                case "start": handleStartOrNextorEnd(START); break;
                case "next": handleStartOrNextorEnd(NEXT); break;
                case "end": handleStartOrNextorEnd(END); break;
                default: break;
            }

            System.out.println("Got payload: " + payload);
            
            // super.handleTextMessage(session, message);
        }


        private void handleJoinOrCreate(int joinOrCreate){
            try {
                var userId = data.getString("id");
                var userPassword = data.getString("password");
                var roomId = data.getString("roomId"); // TODO URL parsed value
                
                var user = DatabaseUtil.getFirst(User.class, "id="+"'"+userId+"'"+" "+"password="+" "+"'"+userPassword+"'");
                WebAPI.putUser(this.session, user);

                if (joinOrCreate == JOIN){
                    var room = BasicLogic.getRoom(roomId);
                    room.joinUser(user);
                    messageTo(room.getUsers(), new TextMessage(new JSONObjectjWsReplyOnJoin(user.getId(), user.getName()).toString()));
                }
                if (joinOrCreate == CREATE){
                    var quizInd = data.getInt("quizInd");
                    var quiz = user.getQuiz(quizInd);
                    var host = user.createRoom(quiz); // TODO: create 'create room' int WebAPI, replace user with host
                    // var room = host.getRoom();
                    messageTo(host, new TextMessage(new JSONObjectjWsReplyOnCreate(true).toString()));
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                try {
                    this.session.sendMessage(new TextMessage(new JSONObjectjWsReplyOnCreate(false).toString()));
                } catch (IOException e1) { 
                    // TODO: handle exception
                    e1.printStackTrace(); 
                }
            }
        }
        private void handleLeave(){
            try {
                var user = WebAPI.getUser(this.session);
                var room = user.leaveRoom();
                messageTo(room.getUsers(), new TextMessage(new JSONObjectWsMessageLeft(user.getId(), user.getName()).toString()));


            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
            

        }
        private void handleBark(){
            try {
                var roomId = data.getString("roomId");
                var room = BasicLogic.getRoom(roomId);
                messageTo(room.getUsers(), new TextMessage(new JSONObjectjWsReplyOnBark().toString()));
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }

        private void handleStartOrNextorEnd(int startNextEnd){
            try {
                var roomId = this.data.getString("roomId");
                var userId = this.data.getString("userId");
                var password = this.data.getString("password");

                var room = BasicLogic.getRoom(roomId);
                var host = room.getHost();

                if (userId.equals(host.getId()) && password.equals(host.getPassword())){
                    if (startNextEnd == START){
                        var question = host.startRoom();
                        messageTo(room.getUsers(), new TextMessage(new JSONObjectjWsReplyOnStart(question).toString()));
                    }
                    if (startNextEnd == NEXT){
                        var question = host.nextQuestion();
                        messageTo(room.getUsers(), new TextMessage(new JSONObjectjWsReplyOnNext(question).toString()));  
                    }
                    if (startNextEnd == END){
                        var scores = host.endRoom();
                        messageTo(room.getUsers(), new TextMessage(new JSONObjectjWsReplyOnEnd(scores).toString()));
                    }
                } else {
                    //TODO
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }

        private void handleChoice(){
            try {
                var roomId = this.data.getString("roomId");
                var questionInd = this.data.getInt("questionId");
                var choiceInd = this.data.getInt("choiceId");

                var user = WebAPI.getUser(session);
                var room = BasicLogic.getRoom(roomId);

                if (user.getRoom().getId()!=roomId) throw new Exception("Invalid room id: "+roomId);
                if (user instanceof Player) throw new Exception("user "+user.getId()+" is not a player");

                var player = (Player) user;
                var question = room.getQuiz().getQuestions().get(questionInd);
                var choice = question.getChoices().get(choiceInd);
                player.choose(question, choice);
                messageTo(room.getHost(), new TextMessage(new JSONObjectjWsReplyOnChoice(questionInd, choiceInd).toString()));
                
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }

        }
    }

}
