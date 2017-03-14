package com.kuuasema;

import com.kuuasema.model.Game;
import com.kuuasema.model.Player;
import com.kuuasema.model.Score;
import com.kuuasema.repository.GameRepository;
import com.kuuasema.repository.PlayerRepository;
import com.kuuasema.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */

@RestController
@RequestMapping("/score")
class ScoreController {
    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    @Autowired
    ScoreController(ScoreRepository scoreRepository, PlayerRepository playerRepository, GameRepository gameRepository) {
        this.scoreRepository = scoreRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    static class PostScoreParams {
        private String playerId;
        private String gameTitle;
        private Double score;

        public String getPlayerId() {
            return playerId;
        }

        public String getGameTitle() {
            return gameTitle;
        }

        public Double getScore() {
            return score;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> postScore(@RequestBody PostScoreParams params) {
        Player player = this.playerRepository.findById(params.getPlayerId());
        if (player != null) {

            URI location = URI.create(Application.baseUrl + "/score/game/" + params.getGameTitle());

            Game game = this.gameRepository.findByTitle(params.getGameTitle());
            if (game != null) {

                Score score = this.scoreRepository.findByPlayerAndGame(player, game);
                if (score != null) {
                    if (params.getScore() > score.getScore()) {
                        score.setScore(params.getScore());
                        score = this.scoreRepository.save(score);
                    }
                    return ResponseEntity.ok(score);
                }

                score = new Score(player, game, params.getScore());
                score = this.scoreRepository.save(score);

                return ResponseEntity.created(location).body(score);
            }

            // create a game with the given title
            game = new Game(params.getGameTitle());
            game = this.gameRepository.save(game);

            Score score = new Score(player, game, params.getScore());
            score = this.scoreRepository.save(score);

            return ResponseEntity.created(location).body(score);
        }

        return Application.PlayerNotFound.build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/game/{gameTitle}")
    ResponseEntity<?> getHighScore(@PathVariable String gameTitle) {
        Game game = this.gameRepository.findByTitle(gameTitle);

        if (game != null) {
            Set<Score> scores = this.scoreRepository.findByGameOrderByScoreDesc(game);
            return ResponseEntity.ok(scores);
        }

        return Application.GameNotFound.build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/player/{playerId}")
    ResponseEntity<?> getPlayer(@PathVariable String playerId) {
        Player player = this.playerRepository.findById(playerId);
        if (player != null) {
            Set<Score> scores = player.getScores();
            return ResponseEntity.ok(scores);
        }

        return Application.PlayerNotFound.build();
    }

    static class DeleteScoreParams {
        private String playerId;
        private String gameTitle;

        public String getPlayerId() {
            return playerId;
        }

        public String getGameTitle() {
            return gameTitle;
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    ResponseEntity<?> deletePlayerScore(@RequestBody DeleteScoreParams params) {
        Player player = this.playerRepository.findById(params.getPlayerId());
        if (player != null) {

            Game game = this.gameRepository.findByTitle(params.getGameTitle());
            if (game != null) {
                this.scoreRepository.deleteByPlayerAndGame(player, game);

                return ResponseEntity.ok(null);
            }

            return Application.GameNotFound.build();
        }

        return Application.PlayerNotFound.build();
    }
}
