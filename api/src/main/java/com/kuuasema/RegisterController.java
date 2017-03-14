package com.kuuasema;

import com.kuuasema.model.Player;
import com.kuuasema.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by DuyAnhPham on 14/03/2017.
 */
@RestController
@RequestMapping("/register")
class RegisterController {

    private final PlayerRepository playerRepository;

    @Autowired
    RegisterController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    static class RegisterParams {
        private String playerName;

        public String getPlayerName() {
            return this.playerName;
        }
    }

    static class RegisterRes {
        private String playerId;

        public RegisterRes(String playerId) {
            this.playerId = playerId;
        }

        public String getPlayerId() {
            return playerId;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> register(@RequestBody RegisterParams params) {
        Player player = this.playerRepository.findByName(params.getPlayerName());

        if (player != null) {
            return Application.PlayerNameConflic.build();
        }

        player = this.playerRepository.save(new Player(params.getPlayerName()));

        URI location = URI.create(Application.baseUrl + "/player/" + player.getId());

        return ResponseEntity.created(location).body(new RegisterRes(player.getId()));
    }
}
