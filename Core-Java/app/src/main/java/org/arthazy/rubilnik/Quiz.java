package org.arthazy.rubilnik;
import java.util.List;

import org.arthazy.rubilnik.users.User;

import java.util.Date;
import java.util.LinkedList;

public class Quiz {
    private User author;
    private String title;
    private List<Question> questions = new LinkedList<>();
    private Date dateCreated;

    public List<Question> getQuestions() {
        return questions;
    }
    public Date getDateCreated() {
        return dateCreated;
    }

    public Quiz(User author, String title){
        this.title = title;
        this.author = author;
        this.dateCreated = new Date();
    }

    public String getTitle() {
        return title;
    }
    public User getAuthor() {
        return author;
    }

    public Question addQuestion(String title){
        var q = new Question(title, new LinkedList<>());
        this.questions.add(q);
        return q;
    }
    Question addQuestion(int index, String title){
        var q = new Question(title, new LinkedList<>());
        this.questions.add(index,q);
        return q;
    }

    void removeQuestion(int index){
        try {
            this.questions.remove(index);
        } catch (IndexOutOfBoundsException e) {
            // TODO: handle exception
        }
    }

    public class Question {
        private String title;
        private List<Choice> choices;

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }
        public List<Choice> getChoices() {
            return choices;
        }

        Question(String title, List<Choice> list){
            this.title = title;
            this.choices = list;
        }

        public Choice addChoice(String title, boolean isCorrect){
            var c = new Choice(title, isCorrect);
            choices.add(c);
            return c;
        }
        Choice addChoice(int index, String title, boolean isCorrect){
            var c = new Choice(title, isCorrect);
            choices.add(index,c);
            return c;
        }
        
        void removeChoice(int index){
            try {
                choices.remove(index);
            } catch (IndexOutOfBoundsException e) {}
        }
        void removeChoice(Choice choice){
            choices.remove(choice);
        }

        public class Choice {
            private String title;
            private boolean isCorrect;

            public String getTitle() {
                return title;
            }
            public void setTitle(String title) {
                this.title = title;
            }
            public void setCorrect(boolean isCorrect) {
                this.isCorrect = isCorrect;
            }

            Choice(String title, boolean isCorrect){
                this.title = title;
                this.isCorrect = isCorrect;
            }

            boolean isCorrect(){return isCorrect;}
        }
    }
}