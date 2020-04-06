package com.gotcha.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="players")
public class Player extends User {

    @NotBlank
    @Getter
    @Setter
    private String alias;
    @Getter
    @Setter
    private String psychFaceURL;
    @Getter
    @Setter
    private String picURL;

    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Stat stats = new Stat();

    @Getter
    @Setter
    @ManyToMany
    private Set<Game> games = new HashSet<>();

    public Player() {
    }

    private Player(Builder builder) {
        setAlias(builder.alias);
        setSaltedHashPassword(builder.saltedHashedPassword);
        setEmail(builder.email);
        setPsychFaceURL(builder.psychFaceURL);
        setPicURL(builder.picURL);
    }

    public static Builder newPlayer() {
        return new Builder();
    }


    public static final class Builder {
        private @Email @NotBlank String email;
        private @NotBlank String saltedHashedPassword;
        private @NotBlank String alias;
        private String psychFaceURL;
        private String picURL;

        public Builder() {
        }

        public Player build() {
            return new Player(this);
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder saltedHashedPassword(String saltedHashedPassword) {
            this.saltedHashedPassword = saltedHashedPassword;
            return this;
        }

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder psychFaceURL(String psychFaceURL) {
            this.psychFaceURL = psychFaceURL;
            return this;
        }

        public Builder picURL(String picURL) {
            this.picURL = picURL;
            return this;
        }
    }
}
