package org.arthazy.rubilnik.users;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import org.arthazy.rubilnik.Quiz;
import org.arthazy.rubilnik.Room;

public class User {
    private int id;
    private String name;
    private List<Quiz> quizzes = new LinkedList<>();
    protected Room room;

    public String getName() {
        return name;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    

    public User(String name){
        this.name = name;
    }
    User(User user){
        this.id = user.id;
        this.name = user.name;
        this.quizzes = user.quizzes;
        this.room = user.room;
    }

    void leaveRoom() {
        room.leaveUser(this);
    }
    public void createQuiz(String title){
        quizzes.add(new Quiz(this,title));
    }
    
    @Nullable
    public
    Quiz getQuiz(int index){
        try {
            return quizzes.get(index);
        } catch (IndexOutOfBoundsException e) {};
        return null;
    }
    void removeQuiz(Quiz quiz){
        quizzes.remove(quiz);
    }
}

