package org.rubilnik.basicLogic.users;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.rubilnik.basicLogic.IdManager;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Room;
import org.rubilnik.basicLogic.interfaces.UniqueObject;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "users") // "User" is reserved keyword in psql
public class User implements Serializable, UniqueObject {
    private static IdManager idManager = new IdManager(4, null);
    
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;
    @JoinColumn @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new LinkedList<>();
    protected Room room;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    protected void checkRoomForNull() throws RuntimeException{
        if (room == null) throw new RuntimeException("Room is null");
    }
    
    protected User(){} //

    public User(String name, String email, String password) throws RuntimeException {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = idManager.getFreeId();
    }
    User(User user){
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
        this.password = user.password;
        this.quizzes = user.quizzes;
        this.room = user.room;
    }
    public Player joinRoom(Room room){
        var player = new Player(this);
        room.joinUser(player);
        return player;
    }
    public Room leaveRoom() {
        room.leaveUser(this);
        return room;
    }
    public Quiz createQuiz(String title){
        var quiz = new Quiz(this,title);
        quizzes.add(quiz);
        return quiz;
    }
    public Host createRoom(Quiz quiz){
        var host = new Host(this);
        this.room = new Room(host, quiz);
        return host;
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

