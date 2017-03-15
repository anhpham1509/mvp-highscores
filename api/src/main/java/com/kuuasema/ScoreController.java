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

import java.net.URI;
import java.util.*;

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

    static class PostScoreRes implements Comparable{
        private String playerName;
        private String gameTitle;
        private Double score;

        public PostScoreRes(String playerName, String gameTitle, Double score) {
            this.playerName = playerName;
            this.gameTitle = gameTitle;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getGameTitle() {
            return gameTitle;
        }

        public Double getScore() {
            return score;
        }

        @Override
        public int compareTo(Object o) {
            GameScore another = (GameScore) o;
            if (this.score > another.getScore()) {
                return -1;
            }
            return 1;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> postScore(@RequestBody PostScoreParams params) {
        if (!(validatePlayerId(params.getPlayerId()) && validateGameTitle(params.getGameTitle()) && validateScore(params.getScore()))) {
            return Application.InvalidRequest.build();
        }

        Player player = this.playerRepository.findById(params.getPlayerId());
        if (player != null) {

            URI location = URI.create(Application.baseUrl + "/score/game/" + params.getGameTitle());

            PostScoreRes gameScore = new PostScoreRes(player.getName(), params.getGameTitle(), params.getScore());

            Game game = this.gameRepository.findByTitle(params.getGameTitle());
            if (game != null) {

                Score score = this.scoreRepository.findByPlayerAndGame(player, game);
                if (score != null) {
                    if (params.getScore() > score.getScore()) {
                        score.setScore(params.getScore());
                        this.scoreRepository.save(score);
                    }
                    return ResponseEntity.ok(gameScore);
                }

                score = new Score(player, game, params.getScore());
                this.scoreRepository.save(score);

                return ResponseEntity.created(location).body(gameScore);
            }

            // create a game with the given title
            game = new Game(params.getGameTitle());
            game = this.gameRepository.save(game);

            Score score = new Score(player, game, params.getScore());
            this.scoreRepository.save(score);

            return ResponseEntity.created(location).body(gameScore);
        }

        return Application.PlayerNotFound.build();
    }

    static class GameScore implements Comparable{
        private String playerName;
        private Double score;

        public GameScore(String playerName, Double score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public Double getScore() {
            return score;
        }

        @Override
        public int compareTo(Object o) {
            GameScore another = (GameScore) o;
            if (this.score > another.getScore()) {
                return -1;
            }
            return 1;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/game/{gameTitle}")
    ResponseEntity<?> getHighScore(@PathVariable String gameTitle) {
        if (!validateGameTitle(gameTitle)) {
            return Application.InvalidRequest.build();
        }

        Game game = this.gameRepository.findByTitle(gameTitle);
        if (game != null) {
            Set<Score> scoreSet = this.scoreRepository.findByGame(game);
            List<GameScore> gameScores = new ArrayList<>();
            scoreSet.forEach(score -> {
                gameScores.add(new GameScore(score.getPlayer().getName(), score.getScore()));
            });
            Collections.sort(gameScores);
            return ResponseEntity.ok(gameScores);
        }

        return Application.GameNotFound.build();
    }

    static class PlayerScore{
        private String gameTitle;
        private Double score;

        public PlayerScore(String gameTitle, Double score) {
            this.gameTitle = gameTitle;
            this.score = score;
        }

        public String getGameTitle() {
            return gameTitle;
        }

        public Double getScore() {
            return score;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/player/{playerId}")
    ResponseEntity<?> getPlayer(@PathVariable String playerId) {
        if (!validatePlayerId(playerId)) {
            return Application.InvalidRequest.build();
        }

        Player player = this.playerRepository.findById(playerId);
        if (player != null) {
            Set<Score> scoreSet = player.getScores();
            List<PlayerScore> playerScores = new ArrayList<>();
            scoreSet.forEach(score -> {
                playerScores.add(new PlayerScore(score.getGame().getTitle(), score.getScore()));
            });
            return ResponseEntity.ok(playerScores);
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
        if (!(validatePlayerId(params.getPlayerId()) && validateGameTitle(params.getGameTitle()))) {
            return Application.InvalidRequest.build();
        }

        Player player = this.playerRepository.findById(params.getPlayerId());
        if (player != null) {

            Game game = this.gameRepository.findByTitle(params.getGameTitle());
            if (game != null) {
                Score score = this.scoreRepository.findByPlayerAndGame(player, game);
                if (score != null) {
                    this.scoreRepository.delete(score);
                    return ResponseEntity.ok(null);
                }
                return Application.ScoreNotFound.build();
            }

            return Application.GameNotFound.build();
        }

        return Application.PlayerNotFound.build();
    }

    String uuidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

    boolean validatePlayerId(String playerId) {
        return playerId.matches(uuidRegex);
    }

    boolean validateGameTitle(String gameTitle) {
        return !gameTitle.equals("");
    }

    boolean validateScore(Double score) {
        return score > 0.0;
    }
}
