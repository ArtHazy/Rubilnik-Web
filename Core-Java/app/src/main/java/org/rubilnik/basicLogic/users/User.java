package org.rubilnik.basicLogic.users;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Room;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "users") // "User" is reserved keyword in psql
public class User implements Serializable {
    @Id @GeneratedValue
    private long id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;
    @JoinColumn @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new LinkedList<>();
    protected Room room;

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    
    protected User(){} //
    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    User(User user){
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
        this.password = user.password;
        this.quizzes = user.quizzes;
        this.room = user.room;
    }

    void leaveRoom() {
        room.leaveUser(this);
    }
    public void createQuiz(String title){
        quizzes.add(new Quiz(this,title));
    }

    public Quiz getQuiz(int index){
        try {
            return quizzes.get(index);
        } catch (IndexOutOfBoundsException e) {};
        return null;
    }
    void removeQuiz(Quiz quiz){
        quizzes.remove(quiz);
    }
}

