package com.gotcha.game.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gotcha.game.exceptions.InvalidGameActionException;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "rounds")
public class Round extends Auditable{
    @ManyToOne
    @Getter
    @Setter
    @NotNull
    @JsonBackReference
    private Game game;

    @ManyToOne
    @NotNull
    @Getter @Setter
    @JsonIdentityReference
    private Question question;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player, PlayerAnswer> submittedAnswers = new HashMap<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player, PlayerAnswer> selectedAnswers = new HashMap<>();

    @NotNull
    @Getter @Setter
    private int roundNumber;

    @ManyToOne
    @JsonIdentityReference
    @Getter @Setter
    private EllenAnswer ellenAnswer;

    public Round(@NotNull Game game, @NotNull Question question, @NotNull int roundNumber) {
        this.game = game;
        this.question = question;
        this.roundNumber = roundNumber;
    }



    public void submitAnswer(Player player, String answer) throws InvalidGameActionException {
            if(submittedAnswers.containsKey(player))
                throw new InvalidGameActionException("player already submitted for this round!");
            for (PlayerAnswer existingAnswer: submittedAnswers.values())
                if(answer.equals(existingAnswer.getAnswer()))
                    throw new InvalidGameActionException("Duplicate Answer!!");
            submittedAnswers.put(player, new PlayerAnswer(this,player,answer));



    }

    public void selectAnswer(Player player,  PlayerAnswer answer) throws InvalidGameActionException {
        if(selectedAnswers.containsKey(player))
            throw new InvalidGameActionException("player already selected for this round!");
        if(answer.getPlayer().equals(player))
            throw new InvalidGameActionException("Cant select your own answer!!");

        selectedAnswers.put(player, answer);

    }

    public boolean allAnswersSubmitted(int size) {
        return submittedAnswers.size() == size;
    }

    public boolean allAnswersSelected(int size) {
        return selectedAnswers.size() == size;
    }

    public JSONObject getRoundData() {
        return null;
    }
}
