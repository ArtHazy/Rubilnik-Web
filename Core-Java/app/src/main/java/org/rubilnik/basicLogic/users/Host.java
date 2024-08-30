package org.rubilnik.basicLogic.users;

import org.rubilnik.basicLogic.Quiz.Question;
import java.util.List;
import java.util.Map.Entry;


public class Host extends User {
    public Host(User user) {super(user);}


    public Question nextQuestion() throws RuntimeException{
        checkRoomForNull();
        return room.next();
    }
    public Question startRoom() throws RuntimeException{
        checkRoomForNull();
        return room.start();
    }
    public List<Entry<Player, Integer>> endRoom() throws RuntimeException{
        checkRoomForNull();
        return room.end();
    }
}