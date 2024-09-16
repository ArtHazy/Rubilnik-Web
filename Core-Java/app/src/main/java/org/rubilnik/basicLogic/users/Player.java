package org.rubilnik.basicLogic.users;

import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;

public class Player extends User {
    public Player(User user){
        super(user);
    }
    
    public void choose(Question question, Choice choice) throws RuntimeException{
        checkRoomForNull();
        room.regPlayerChoice(this, question, choice);
    }
}