package com.gotcha.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gotcha.game.Utils;
import com.gotcha.game.exceptions.InvalidGameActionException;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import netscape.javascript.JSObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name= "games")
public class Game extends Auditable{
    @ManyToMany
    @Getter
    @Setter
    @JsonIdentityReference
    private Set<Player> players = new HashSet<>();

    @ManyToOne
    @JsonIdentityReference
    @Getter
    @Setter
    @NotNull
    private GameMode gameMode;

    @OneToMany(mappedBy = "game" , cascade = CascadeType.ALL)
    @OrderBy(value = "round_number asc")
    @Getter
    @Setter
    @JsonManagedReference
    private List<Round> rounds = new ArrayList<>();

    @Getter
    @Setter
    private Boolean hasEllen = false;

    @NotNull
    @Getter @Setter
    @ManyToOne
    @JsonIdentityReference
    private Player leader;

    @ManyToMany(cascade = CascadeType.ALL)
    @Getter @Setter
    @JsonManagedReference
    private Map<Player, Stat> playerStats = new HashMap<>();

    @Getter @Setter
    private int numRounds = 10;

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private GameStatus gameStatus = GameStatus.PLAYERS_JOINING;

    @ManyToMany
    @Getter @Setter
    @JsonIdentityReference
    private Set<Player> readyPlayers = new HashSet<>();

    public Game(@NotNull GameMode gameMode, int numRounds, Boolean hasEllen, @NotNull Player leader) {
        this.gameMode = gameMode;
        this.numRounds = numRounds;
        this.hasEllen = hasEllen;
        this.leader = leader;
        try {
            addPlayer(leader);
        } catch (InvalidGameActionException ignored) {
        }
    }

    public void addPlayer(Player player) throws InvalidGameActionException {
        System.out.println(gameStatus);
        if(!gameStatus.equals(GameStatus.PLAYERS_JOINING))
            throw new InvalidGameActionException("Cant join after game has starter");
        players.add(player);
        player.setCurrentGame(this);
    }

    public void removePlayer(Player player) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game!");
        players.remove(player);
        if(player.getCurrentGame().equals(this))
            player.setCurrentGame(null);
        if((players.size() == 1 && !gameStatus.equals(GameStatus.PLAYERS_JOINING)) || players.size()==0)
            endGame();
    }

    public void startGame(Player player) throws InvalidGameActionException {
        if(!player.equals(leader))
            throw new InvalidGameActionException("only leader can start the player!!");
        startNewRound();
    }

    private void startNewRound() {
        gameStatus = GameStatus.SUBMITTING_ANSWERS;
        Question question =  Utils.getRandomQuestion(gameMode);
        Round round = new Round(this, question, rounds.size() + 1);
        if(hasEllen)
            round.setEllenAnswer(Utils.getRandomEllenAnswer(question));
        rounds.add(round);
    }

    public void submitAnswer(Player player, String answer) throws InvalidGameActionException {
        if(answer.length() == 0)
            throw new InvalidGameActionException("Answer is empty!!");
        if(!players.contains(player))
            throw new InvalidGameActionException("player is not available!!");
        if(!gameStatus.equals(GameStatus.SUBMITTING_ANSWERS))
            throw new InvalidGameActionException("game is not accepting the answers");
        Round currentRound =  getCurrentRound();
       currentRound.submitAnswer(player, answer);
        if(currentRound.allAnswersSubmitted(players.size()))
            gameStatus = GameStatus.SELECTING_ANSWERS;
    }

    public void selectAnswer(Player player, PlayerAnswer answer) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("player is not available!!");
        if(!gameStatus.equals(GameStatus.SELECTING_ANSWERS))
            throw new InvalidGameActionException("game is not SELECTING the answers");
        Round currentRound =  getCurrentRound();
        currentRound.selectAnswer(player, answer);
        if(currentRound.allAnswersSelected(players.size())) {
            if(rounds.size() < numRounds)
                gameStatus = GameStatus.WAITING_FOR_READY;
            else
                endGame();
        }

    }

    public void playerIsReady(Player player) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("no such player was in the game!");
        if(!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("game is not waiting for players to be ready!");
        readyPlayers.add(player);
        if(readyPlayers.size() == players.size())
            startNewRound();
    }

    public void playerIsNotReady(Player player) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("no such player was in the game!");
        if(!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("game is not waiting for players to be ready!");
        readyPlayers.remove(player);
    }

    private Round getCurrentRound() throws InvalidGameActionException {
        if(rounds.size() == 0)
            throw new InvalidGameActionException("game has not started!!");
        return rounds.get(rounds.size() -1 );
    }

    private void endGame() {
        gameStatus = GameStatus.ENDED;
        for (Player player : players) {
            if(player.getCurrentGame().equals(this))
                player.setCurrentGame(null);
        }
    }

    public void endGame(Player player) throws InvalidGameActionException {
        if (gameStatus.equals(GameStatus.ENDED))
            throw new InvalidGameActionException("The game has already ended");
        if (!player.equals(leader))
            throw new InvalidGameActionException("Only the leader can end the game");
        endGame();
    }

    public JSONObject getRoundData() throws InvalidGameActionException {
        return getCurrentRound().getRoundData();
    }

    public String getSecretCode() {
        return Utils.getSecretCodeFromGameId(getId());
    }

    public Game() {
    }
}
