package com.kuuasema;

import com.kuuasema.model.Game;
import com.kuuasema.model.Player;
import com.kuuasema.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DuyAnhPham on 13/03/2017.
 */
@RestController
@RequestMapping("/game")
class GameController {

    private GameRepository gameRepository;

    @Autowired
    GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<?> searchGameTitle(@RequestParam(value = "search") String[] pattern) {
        if (pattern.length != 1) {
            return Application.InvalidRequest.build();
        }

        Set<Game> gameSet = this.gameRepository.findByTitleIgnoreCaseLike("%" + pattern[0] + "%");
        Set<String> gameTitles = new HashSet<>();
        gameSet.forEach(game -> {
            gameTitles.add(game.getTitle());
        });

        return ResponseEntity.ok(gameTitles);
    }
}
