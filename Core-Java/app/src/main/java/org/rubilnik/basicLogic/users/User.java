package org.rubilnik.basicLogic.users;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.rubilnik.basicLogic.IdManager;
import org.rubilnik.basicLogic.Quiz;
import org.rubilnik.basicLogic.Room;
import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.interfaces.UniqueObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Quiz> quizzes = new LinkedList<>();
    @JsonIgnore
    protected Room room;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<Quiz> getQuizzes() {
        return quizzes;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
        var room_ = room;
        room.leaveUser(this);
        return room_;
    }
    public Quiz createQuiz(String title){
        var quiz = new Quiz(this,title);
        quizzes.add(quiz);
        return quiz;
    }
    public Quiz createQuiz(String title, List<Question> questions){
        var quiz = new Quiz(this,title);
        quiz.setQuestions(questions);
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

