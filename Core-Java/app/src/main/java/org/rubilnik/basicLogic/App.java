package org.rubilnik.basicLogic;

import org.rubilnik.basicLogic.users.User;

// main class for testing basic logic functions
public class App {
    public static void main(String[] args) {

        var user1 = new User("Joe","joe@mail", "123");
        var user2 = new User("May","may@mail", "123");
        var user3 = new User("Jas","jas@mail", "123");
        var user4 = new User("Suzu","suzu@mail", "123");
        var quiz = user1.createQuiz("quiz1");
        quiz.addQuestion("question 1?").addChoice("wrong choice", false).addChoice("correct choice", true);
        quiz.addQuestion("question 2?").addChoice("only choice", true);
        var host = user1.createRoom(quiz);
        var room = host.getRoom();
        var player1 = user2.joinRoom(host.getRoom());
        var player2 = user3.joinRoom(host.getRoom());
        var player3 = user4.joinRoom(host.getRoom());
        
        var question = host.startRoom();

        player1.choose(question, question.getChoices().get(1)); // index out of bounds
        player2.choose(question, question.getChoices().get(0)); // index out of bounds

        question = host.nextQuestion();

        player1.choose(question, question.getChoices().get(0));
        player3.choose(question, question.getChoices().get(0));


        host.endRoom();
        System.out.println("Choices: "+"\n"+room.printChoices()+"\n");
        System.out.println("Scores: "+"\n"+room.listScores()+"\n");
    }
}
