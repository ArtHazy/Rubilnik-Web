package org.rubilnik.basicLogic;

import org.rubilnik.basicLogic.Quiz.Question;
import org.rubilnik.basicLogic.users.*;

public class Main {
    public static void main(String[] args) {
        User user = new User("Joe");
        user.createQuiz("quiz1");
        Host host = new Host(user);
        user = null;

        User user2 = new User("Mark");
        User user3 = new User("James");
        Player player = new Player(user2);
        Player player2 = new Player(user3);
        user2 = null;
        user3 = null;

        Room room = new Room(host, host.getQuiz(0));
        var quiz = host.getQuiz(0);
        var q = quiz.addQuestion("question 1");
        q.addChoice("choice 1", true);
        q.addChoice("choice 2", false);
        q.addChoice("choice 3", false);
        q.addChoice("choice 4", false);

        player.joinRoom(room);
        player2.joinRoom(room);
        host.startRoom();
        // host.nextQuestion();
        Question question = room.getCurrentQuestion();
        player.choose(question, question.getChoices().get(0));
        player2.choose(question, question.getChoices().get(1));
        host.endRoom();
        QuizReport playerReport = new QuizReport(player, room);
        QuizReport playerReport2 = new QuizReport(player2, room);
        System.out.println(playerReport.toString());
        System.out.println(playerReport2.toString());
    }
}
