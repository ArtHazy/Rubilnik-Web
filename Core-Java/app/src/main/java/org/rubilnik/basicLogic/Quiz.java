package org.rubilnik.basicLogic;
import java.util.List;

import org.rubilnik.basicLogic.users.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.LinkedList;

@Entity
public class Quiz {
    @Id
    @GeneratedValue
    private long id;
    @JoinColumn
    @ManyToOne
    private User author;
    @Column
    private String title;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private List<Question> questions = new LinkedList<>();
    @Column
    private Date dateCreated;

    public List<Question> getQuestions() {
        return questions;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    // def for JPA
    protected Quiz(){}
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
    
    @Entity
    // static for hibernate
    public static class Question {
        @Id 
        @GeneratedValue
        private long id;
        @Column
        private String title;
        @JoinColumn
        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
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
        // def for JPA
        public Question(){}
        Question(String title, List<Choice> list){
            this.title = title;
            this.choices = list;
        }
    
        public Question addChoice(String title, boolean isCorrect){
            var c = new Choice(title, isCorrect);
            choices.add(c);
            return this;
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
    
        @Entity
        public static class Choice {
            @Id
            @GeneratedValue
            private long id;
            @Column
            private String title;
            @Column
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
    
            protected Choice(){}
            Choice(String title, boolean isCorrect){
                this.title = title;
                this.isCorrect = isCorrect;
            }
    
            boolean isCorrect(){return isCorrect;}
        }
    }

}