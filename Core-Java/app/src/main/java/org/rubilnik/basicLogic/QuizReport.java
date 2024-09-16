package org.rubilnik.basicLogic;
import java.util.Map;

import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.Quiz.Question.Choice;
import org.rubilnik.basicLogic.users.Player;
import org.rubilnik.basicLogic.users.User;

import java.util.Date;


public class QuizReport {
    User user;
    Quiz quiz;
    Map<Question, Choice> choices;
    int score;

    public QuizReport(Player player, Room room) {
        this.user = player;
        this.quiz = room.getQuiz();
        this.choices = room.getPlayersChoices(player);
        this.score = room.getPlayersScore(player);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(quiz.getTitle()+"\n"+"made by "+quiz.getAuthor().getName()+"\n"+quiz.getDateCreated()+
        "\n"+"\n"+"passed by "+user.getName()+"\n"+new Date().toString()+
        "\n"+"\n");

        choices.forEach((Question question, Choice choice)->{
            sb.append(question.getTitle());
            sb.append(" : ");
            sb.append(choice.getTitle());
            if (choice.isCorrect()) {
                sb.append(" : ");
                sb.append("OK");
            } else {
                sb.append(" : ");
                sb.append("F");
            }
            sb.append("\n");
        });
        sb.append("\n");
        sb.append("score: ").append(score).append("\n");

        return sb.toString();
    }
}
