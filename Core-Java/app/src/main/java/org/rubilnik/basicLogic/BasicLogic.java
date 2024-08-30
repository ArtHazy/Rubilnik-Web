package org.rubilnik.basicLogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.InvalidAttributeValueException;
import org.rubilnik.basicLogic.users.*;

public class BasicLogic {
    private static Map<String,Room> rooms = new HashMap<>();
    private static Set<User> users = new HashSet<>();


    public static Room getRoom(String id) throws IllegalArgumentException {
        var room = rooms.get(id);
        if (room == null) throw new IllegalArgumentException("Could not find room with id "+id);
        return room;
    }


    public static void main(String[] args) {

        var user1 = new User("Joe","joe@mail", "123");
        var user2 = new User("May","may@mail", "123");
        var user3 = new User("Jas","jas@mail", "123");
        var user4 = new User("Suzu","suzu@mail", "123");
        var quiz = user1.createQuiz("quiz1");
        quiz.addQuestion("question 1?").addChoice("wrong choice", false).addChoice("correct choice", true);
        quiz.addQuestion("question 2?").addChoice("only choice", true);
        var host = user1.createRoom(quiz);
        var room = host.getRoom();
        var player1 = user2.joinRoom(host.getRoom());
        var player2 = user3.joinRoom(host.getRoom());
        var player3 = user4.joinRoom(host.getRoom());
        
        var question = host.startRoom();

        player1.choose(question, question.getChoices().get(1)); // index out of bounds
        player2.choose(question, question.getChoices().get(0)); // index out of bounds

        question = host.nextQuestion();

        player1.choose(question, question.getChoices().get(0));
        player3.choose(question, question.getChoices().get(0));


        host.endRoom();
        System.out.println("Choices: "+"\n"+room.printChoices()+"\n");
        System.out.println("Scores: "+"\n"+room.printScores()+"\n");
    }

    public static void addRoom(Room room){
        rooms.put(room.getId(), room);
    }
    public static void deleteRoom(String roomId) throws InvalidAttributeValueException {
        rooms.remove(roomId);

        //TODO? players.remove hosts.remove all room users? (obj1<->obj2)
    }
    public static Player joinRoom(User user, String roomId) throws InvalidAttributeValueException {
        var room = rooms.get(roomId);
        if (room == null) throw new InvalidAttributeValueException("Room with id " + roomId + "wasn't found");
        var player = new Player(user);
        users.add(player);

        room.joinUser(player);
        return player;

    }
    public static void leaveRoom(User user, String roomId) throws InvalidAttributeValueException {
        var room = rooms.get(roomId);
        if (room == null) throw new InvalidAttributeValueException("Room with id " + roomId + "wasn't found");

        users.remove(user);
        room.leaveUser(user);
    }
    
    public static String listRooms(){
        var sb = new StringBuilder();
        rooms.forEach((id, room)->{
            sb.append(room.toString());
        });
        return sb.toString();
    }
}
