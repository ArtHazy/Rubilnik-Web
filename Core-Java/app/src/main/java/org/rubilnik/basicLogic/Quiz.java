package org.rubilnik.basicLogic;
import java.util.List;
import java.util.stream.Collectors;

import org.rubilnik.basicLogic.interfaces.UniqueObject;
import org.rubilnik.basicLogic.users.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


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
public class Quiz implements UniqueObject  {
    @Id
    @GeneratedValue
    private long id;
    @JsonBackReference
    @JoinColumn @ManyToOne
    private User author;
    @Column
    private String title;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private List<Question> questions = new LinkedList<>();
    @Column
    private Date dateCreated;
    @Column
    private Date dateSaved;

    public void setDateSaved(Date dateSaved) {
        this.dateSaved = dateSaved;
    }
    public Date getDateSaved() {
        return dateSaved;
    }

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
        this.dateSaved = dateCreated;
    }

    public Long getId() {
        return id;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    // @Override
    // public String getId() {
    //     return String.valueOf(id);
    // }
    public String getTitle() {
        return title;
    }
    public User getAuthor() {
        return author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Question addQuestion(String title){
        var q = new Question(title, new LinkedList<>());
        q.quiz = this;
        this.questions.add(q);
        return q;
    }
    Question addQuestion(int index, String title){
        var q = new Question(title, new LinkedList<>());
        q.quiz = this;
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
    public static class Question implements UniqueObject {
        @Id 
        @GeneratedValue
        private long id;
        @Column
        private String title;
        @JsonBackReference
        @ManyToOne @JoinColumn
        private Quiz quiz;
        @JsonManagedReference
        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "question")
        private List<Choice> choices;
    
        public void setTitle(String title) {
            this.title = title;
        }
        @Override
        public Long getId() {
            return id;
        }
        public String getTitle() {
            return title;
        }
        public List<Choice> getChoices() {
            return choices;
        }
        public List<Choice> getCorrectChoices(){
            return choices.stream().filter( c->c.isCorrect() ).collect(Collectors.toList());
        }

        // def for JPA
        public Question(){}
        Question(String title, List<Choice> list){
            this.title = title;
            this.choices = list;
        }
    
        public Question addChoice(String title, boolean isCorrect){
            var c = new Choice(title, isCorrect);
            c.question = this;
            choices.add(c);
            return this;
        }
        Choice addChoice(int index, String title, boolean isCorrect){
            var c = new Choice(title, isCorrect);
            c.question = this;
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
        public static class Choice implements UniqueObject {
            @Id
            @GeneratedValue
            private long id;
            @Column
            private String title;
            @Column
            private boolean isCorrect;
            @JsonBackReference
            @ManyToOne @JoinColumn
            private Question question;

            public Long getId() {
                return id;
            }
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
            public boolean isCorrect() {
                return isCorrect;
            }
        }
    }

}