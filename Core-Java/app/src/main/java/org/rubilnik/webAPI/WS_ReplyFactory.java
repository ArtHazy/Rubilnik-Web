package org.rubilnik.webAPI;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;
import org.rubilnik.basicLogic.users.Player;
import org.rubilnik.basicLogic.users.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map.Entry;

@SuppressWarnings("unused")
public class WS_ReplyFactory {

    static class WS_EventMessage{
        private String event;
        private Object data;
        public Object getData() {
            return data;
        }
        public String getEvent() {
            return event;
        }
        WS_EventMessage(String event, Object data) {
            this.event = event;
            this.data = data;
        }
        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            String result;
            try {
                result = mapper.writeValueAsString(this);
            } catch (Exception e) {
                System.err.println(e.getCause()+" "+e.getMessage());
                result = "";
            }
            return result;
        }
    }

    public static String onChoice(String userId, String userName, int questionInd_, int choiceInd_) {
        return new WS_EventMessage("choice", new Object() {
            public Object user = new Object(){
                public String id = userId;
                public String name = userName;
            };
            public int questionInd = questionInd_;
            public int choiceInd = choiceInd_;
        }).toString();
    }

    public static String onCreate(Set<User> roommates_) {
        return new WS_EventMessage("create", new Object() {
            public Set<User> roommates = roommates_;
        }).toString();
    }

    public static String onBark(String userId, String userName) {
        return new WS_EventMessage("bark", new Object() {
            public Object user = new Object(){
                public String id = userId;
                public String name = userName;
            };
        }).toString();
    }

    public static String onEnd(List<Entry<Player, Integer>> scores) {

        var sscoress = new ArrayList<Object>();
        scores.forEach((entry)->{
            sscoress.add(new Object(){
                public String id = entry.getKey().getId();
                public String name = entry.getKey().getName();
                public Integer score = entry.getValue();
            });
        });

        return new WS_EventMessage("end", new Object() {
            public List<Object> results = sscoress;
        }).toString();
    }

    public static String onJoin(String userId, String userName, Set<User> roommates) {
        var roommateRecords = usersToPublicRecords(roommates);
        return new WS_EventMessage("join", new Object() {
            public Object user = new Object(){
                public String id = userId;
                public String name = userName;
            };
            public List<Object> roommates = roommateRecords;
        }).toString();
    }

    public static String onNext(Question question_, int index_, int quizLength_) {
        return new WS_EventMessage("next", new Object() {
            public Question question = question_;
            public int index = index_;
            public int quizLength = quizLength_;
        }).toString();
    }

    public static String onReveal(List<Choice> revealedChoices_) {
        return new WS_EventMessage("reveal", new Object() {
            public List<Choice> revealedChoices = revealedChoices_;
        }).toString();
    }

    public static String onStart(Question question_,int index_, int quizLength_) {
        return new WS_EventMessage("start", new Object() {
            public Question question = question_;
            public int quizLength = quizLength_;
            public int index = index_;
        }).toString();
    }

    public static String onError(String message_) {
        return new WS_EventMessage("error", new Object() {
            public String message = message_;
        }).toString();
    }

    public static String onJoined(String userId, String userName, Set<User> roommates) {
        var roommateRecords = usersToPublicRecords(roommates);
        return new WS_EventMessage("joined", new Object() {
            public Object user = new Object(){
                public String id = userId;
                public String name = userName;
            };
            public List<Object> roommates = roommateRecords;
        }).toString();
    }

    // map public data safe records from users
    private static List<Object> usersToPublicRecords(Set<User> users){ 
        List<Object> records = new ArrayList<>();
        users.forEach((user)->{
            records.add(new Object(){
                public String id = user.getId();
                public String name = user.getName();
            });
        });
        return records;
    }

    public static String onLeft(String userId, String userName, Set<User> roommates) {
        var roommateRecords = usersToPublicRecords(roommates);
        return new WS_EventMessage("left", new Object() {
            public Object user = new Object(){
                public String id = userId;
                public String name = userName;
            };
            public List<Object> roommates = roommateRecords;

        }).toString();
    }
}
