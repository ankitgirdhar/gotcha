package com.gotcha.game.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "playeranswers")
public class PlayerAnswer extends Auditable{
    @NotNull
    @ManyToOne
    @Getter @Setter
    @JsonBackReference
    private Round round;

    @NotNull
    @ManyToOne
    @Getter @Setter
    @JsonIdentityReference
    private Player player;

    @NotBlank
    @Getter @Setter
    private String answer;

    public PlayerAnswer(@NotNull Round round, @NotNull Player player, @NotBlank String answer) {
        this.round = round;
        this.player = player;
        this.answer = answer;
    }
}
