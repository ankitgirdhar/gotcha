package com.gotcha.game;

import com.gotcha.game.model.GameMode;
import com.gotcha.game.model.Player;
import com.gotcha.game.model.Question;
import com.gotcha.game.repositories.PlayerRepository;
import com.gotcha.game.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dev-test")
public class HelloWorld {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuestionRepository questionRepository;


    @GetMapping("/")
    public String hello() {

        return "Hello, World!";
    }

    @GetMapping("/populate")
    public String populateDB() {
        Player ankit = new Player.Builder().alias("anky").saltedHashedPassword("admin").email("ankybro@gmail.com").build();
        playerRepository.save(ankit);
        Player punisher = new Player.Builder().alias("punisher69").saltedHashedPassword("punishyou").email("punishyoufucker@gmail.com").build();
        playerRepository.save(punisher);

        questionRepository.save(new Question("tere kitne baap hai?", "4" , GameMode.IS_THIS_A_FACT));
        return "populated";
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable(name = "id") Long id) {
        return questionRepository.findById(id).orElseThrow();
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/player/{id}")
    public Player getPlayerById(@PathVariable(name = "id") Long id) {
        return playerRepository.findById(id).orElseThrow();
    }




    //games
    //admins
    //questions
    //rounds
    //contentwriters
}
