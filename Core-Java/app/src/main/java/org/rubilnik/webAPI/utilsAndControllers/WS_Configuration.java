package org.rubilnik.webAPI.utilsAndControllers;
import org.rubilnik.basicLogic.users.Host;
import org.rubilnik.basicLogic.users.Player;
import org.rubilnik.basicLogic.users.User;

import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.rubilnik.webAPI.WebAPI;
import org.rubilnik.basicLogic.BasicLogic;
import org.rubilnik.basicLogic.Quiz;

@Configuration
@EnableWebSocket
public class WS_Configuration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWSHandler(), "/socket").setAllowedOrigins("*");

    }

    class MyWSHandler extends BinaryWebSocketHandler{
        WebSocketSession session;
        String payload;
        ObjectMapper objectMapper_Json = new ObjectMapper();
        EventMessageBody body;

        static class UserValidationInfo{
            public String id, password;
        }

        final private static int START = 0;
        final private static int NEXT = 1;
        final private static int END = 2;

        private void messageTo_WS(Set<User> users, String message){
            for (User user : users){
                messageTo_WS(user, message);
            }
        }
        private void messageTo_WS(User user, String message){
            var text_message = new TextMessage(message);
            try {
                var session = WebAPI.getSession(user);
                session.sendMessage(text_message);
            } catch (Exception e) {
                // TODO 
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            try {
                this.session = session;
                User user = WebAPI.getUser(this.session);
                var room = user.leaveRoom();
                messageTo_WS(room.getUsers(), WS_EventJsonReplyFactory.onLeft(user.getId(), user.getName(), room.getUsers()) );
            } catch (Exception e) { System.out.println(e.getMessage());}

            WebAPI.removeUser(session);
            System.out.println("Socket connection closed");
            super.afterConnectionClosed(session, status);
        }
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("WS connection established");
            super.afterConnectionEstablished(session);
        }

        static class EventMessageBody{
            public String event;
            public Object data; // LinkedHashMap // mapper.convertValue(data, SOME.class);
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) {
            payload = message.getPayload();
            System.out.println("Got payload: " + payload);
            System.out.println("");

            try {                this.body = objectMapper_Json.readValue(payload, EventMessageBody.class);
                this.session = session;
                switch(body.event){
                    case "create": handleCreate();; break;
                    case "join": handleJoin();; break;
                    case "bark": handleBark(); break;
                    case "choice": handleChoice(); break;
                    case "start": handleStartOrNextorEnd(START); break;
                    case "next": handleStartOrNextorEnd(NEXT); break;
                    case "end": handleStartOrNextorEnd(END); break;
                    case "reveal": handleReveal(); break;
                    default: break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            // super.handleTextMessage(session, message);
        }

        private void handle_WS_requestException(Exception e){
            try {
                System.err.println(e.getMessage());
                session.sendMessage(new TextMessage(WS_EventJsonReplyFactory.onError(e.getMessage())));
            } catch (Exception ex) { 
                System.err.println(ex.getMessage());
            }
        }
        
        private User validateUserFromDB(UserValidationInfo validation) throws Exception {
            var user = DatabaseUtil.getFirst(User.class, "id="+"'"+validation.id+"'"+" and "+"password="+" "+"'"+validation.password+"'");
            return user;
        }

        static class RevealRequest_Data{
        }
        private void handleReveal(){
            try {
                var bodyData = objectMapper_Json.convertValue(body.data, RevealRequest_Data.class); //!
                var user = WebAPI.getUser(session);
                if (!(user instanceof Host)) throw new Exception("User is not a host");
                var host = (Host)user;
                var revealedChoices = host.getRoom().getCurrentQuestion().getChoices();
                messageTo_WS(host.getRoom().getUsers(), WS_EventJsonReplyFactory.onReveal(revealedChoices));
                
            } catch (Exception e) {
                // TODO: handle exception
                handle_WS_requestException(e);
            }
        }

        static class JoinRequest_Data {
            public UserValidationInfo validation;
            public String roomId;
        }
        private void handleJoin(){
            try {
                var bodyData = objectMapper_Json.convertValue(body.data,JoinRequest_Data.class);
                    
                var user = validateUserFromDB(bodyData.validation);
                var room = BasicLogic.getRoom(bodyData.roomId);
                var player = user.joinRoom(room);
                WebAPI.putUser(session, player);


                messageTo_WS(player, WS_EventJsonReplyFactory.onJoin(player.getId(), player.getName(), room.getUsers()));
                var otherUsers = room.getUsers();
                otherUsers.remove(player);
                
                messageTo_WS(otherUsers, WS_EventJsonReplyFactory.onJoined(player.getId(), player.getEmail(), room.getUsers()));

            } catch (Exception e) {
                handle_WS_requestException(e);
            }
        }

        static class CreateRequest_Data{
            public UserValidationInfo validation;
            public Quiz quiz;
        }
        private void handleCreate(){
            try {
                var bodyData = objectMapper_Json.convertValue(body.data, CreateRequest_Data.class);
                
                var user = validateUserFromDB(bodyData.validation);
                var host = user.createRoom(bodyData.quiz);
                WebAPI.putUser(session, host);

                BasicLogic.putRoom(host.getId(), host.getRoom());
                var msg = WS_EventJsonReplyFactory.onCreate(host.getRoom().getUsers());
                messageTo_WS(host, msg );
            } catch (Exception e) {
                handle_WS_requestException(e);
            }
        }
        
        static class BarkRequest_Data{
        }
        private void handleBark(){
            User user = null;
            try {
                user = WebAPI.getUser(session);
                var room = user.getRoom();
                if (room==null) throw new RuntimeException("user is not in the room");

                messageTo_WS( room.getUsers(), WS_EventJsonReplyFactory.onBark(user.getId(), user.getName()) );
            } catch (Exception e) {handle_WS_requestException(e);}
        }

        private void handleStartOrNextorEnd(int startNextEnd){
            try {
                var user = WebAPI.getUser(session);

                if (!(user instanceof Host)) throw new RuntimeException("user is not a host");
                var host = (Host) user;
                var room = host.getRoom();
                if (room == null) throw new RuntimeException("user doesnt have a room");

                if (startNextEnd == START){
                    var question = host.startRoom();
                    messageTo_WS(room.getUsers(), WS_EventJsonReplyFactory.onStart(question, room.getQuiz().getQuestions().indexOf(room.getCurrentQuestion()), room.getQuiz().getQuestions().size()) );
                }
                if (startNextEnd == NEXT){
                    var question = host.nextQuestion();
                    messageTo_WS(room.getUsers(), WS_EventJsonReplyFactory.onNext(question, room.getQuiz().getQuestions().indexOf(room.getCurrentQuestion()), room.getQuiz().getQuestions().size()));  
                }
                if (startNextEnd == END){
                    var scores = host.endRoom();
                    messageTo_WS(room.getUsers(), WS_EventJsonReplyFactory.onEnd(scores));
                }

            } catch (Exception e) { handle_WS_requestException(e);}
        }

        

        static class ChoiceRequest_Data{
            public int questionInd, choiceInd;
            // ChoiceRequest_Data(int questionInd, int choiceInd){this.questionInd = questionInd;this.choiceInd = choiceInd;}
        }
        private void handleChoice(){
            User user = null;
            try {
                var data = objectMapper_Json.convertValue(body.data, ChoiceRequest_Data.class);
                user = WebAPI.getUser(session);
                var room = user.getRoom();

                if (!(user instanceof Player)) throw new Exception("user "+user.getId()+" is not a player");

                var player = (Player) user;
                var question = room.getQuiz().getQuestions().get(data.questionInd);
                var choice = question.getChoices().get(data.choiceInd);
                player.choose(question, choice);
                
                messageTo_WS(room.getHost(), WS_EventJsonReplyFactory.onChoice(user.getId(), user.getEmail(), data.questionInd, data.choiceInd));

            } catch (Exception e) { handle_WS_requestException(e); }
        }

    }

}