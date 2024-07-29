package org.arthazy.rubilnik;
import java.util.Set;

import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;
// import java.util.UUID;
import java.util.HashSet;

import org.arthazy.rubilnik.Quiz.Question;
import org.arthazy.rubilnik.Quiz.Question.Choice;
import org.arthazy.rubilnik.users.Host;
import org.arthazy.rubilnik.users.Player;
import org.arthazy.rubilnik.users.User;

public class Room {
    static String[] statuses = { "await", "progress", "complete" };
    
    // private UUID id;
    private User host;
    private Set<User> players = new HashSet<>();
    private Quiz quiz;
    private String status;
    private Map<Player,Map<Question,Choice>> playersChoices = new HashMap<>();
    private Map<Player,Integer> playersScores = new HashMap<>();
    private int currentQuestionIndex=-1;

    @Nullable
    public Question getCurrentQuestion() {
        try {
            return quiz.getQuestions().get(currentQuestionIndex);
        } catch (IndexOutOfBoundsException e) {}
        return null;
    }
    
    public Integer getPlayersScore(Player player) {
        return playersScores.get(player);
    }
    public Map<Question, Choice> getPlayersChoices(Player player) {
        return playersChoices.get(player);
    }
    public Quiz getQuiz() {
        return quiz;
    }

    public Room(Host host, Quiz quiz){
        // this.id = UUID.randomUUID();
        this.host = host;
        host.setRoom(this);
        this.quiz = quiz;
        this.status = statuses[0];
    }

    @Override
    public String toString() {
        return super.toString()+"\nhost: "+host.toString()+"\nstatus: "+status+"\nquiz: "+quiz.toString();
    }

    public void joinPlayer(Player newPlayer){
        newPlayer.setRoom(this);
        players.add(newPlayer);
        playersChoices.put(newPlayer, new HashMap<Question,Choice>());
    }
    public void leaveUser(User xUser){
        if (xUser instanceof Host){
            this.host = null;
            status = statuses[0];
        } else if (xUser instanceof Player){
            players.remove(xUser);
        }
    }
    public void start(){
        this.status = statuses[1];
        this.currentQuestionIndex = 0;
    }
    public void next(){
        try {
            if (this.quiz.getQuestions().get(currentQuestionIndex+1)!=null){
                currentQuestionIndex+=1;
            };
        } catch (Exception e) { System.err.println(e.getMessage()); }
    }
    public void end(){
        this.status = statuses[2];
        this.currentQuestionIndex = -1;
        calcScores();
    }

    void calcScores(){
        this.playersChoices.forEach((Player player, Map<Question,Choice> choices)->{
            int score = 0;
            for (Map.Entry<Question,Choice> entry : choices.entrySet()){
                if (entry.getValue().isCorrect()) score += 1;
            }
            this.playersScores.put(player, score);
        });;
    }

    public void regPlayerChoice(Player player, Question question, Choice choice){
        if (status.equals(statuses[1])){
            playersChoices.get(player).put(question, choice);
        }
    }
}