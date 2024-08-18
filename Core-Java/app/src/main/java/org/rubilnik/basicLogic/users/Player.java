package org.rubilnik.basicLogic.users;

import org.rubilnik.basicLogic.Room;
import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;

public class Player extends User {
    public Player(User user){super(user);}
    

    public void joinRoom(Room room){
        this.room = room;
        room.joinPlayer(this);
    }
    public void choose(Question question, Choice choice){
        room.regPlayerChoice(this, question, choice);
    }
}