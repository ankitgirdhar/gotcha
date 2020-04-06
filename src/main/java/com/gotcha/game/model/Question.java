package com.gotcha.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question extends Auditable{
    @NotNull
    @Getter @Setter
    private String question;
    @NotNull
    @Getter @Setter
    private String correctAnswer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @Getter @Setter
    private Set<EllenAnswer> ellenAnswers;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @NotNull
    private GameMode gameMode;


    public Question() {
    }

    public Question(@NotNull String question, @NotNull String correctAnswer, @NotNull GameMode gameMode) {
        this.question = question;
        this.gameMode = gameMode;
        this.correctAnswer = correctAnswer;
    }
}
