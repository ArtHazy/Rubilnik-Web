package org.arthazy.rubilnik.users;

import org.arthazy.rubilnik.Quiz.Question;
import org.arthazy.rubilnik.Quiz.Question.Choice;
import org.arthazy.rubilnik.Room;

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