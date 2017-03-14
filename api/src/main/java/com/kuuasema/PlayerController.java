package com.kuuasema;

import com.kuuasema.model.Player;
import com.kuuasema.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */

@RestController
@RequestMapping("/player")
class PlayerController {
    private final PlayerRepository playerRepository;

    @Autowired
    PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<?> searchPlayerName(@RequestParam(value = "search") String[] pattern) {
        if (pattern.length != 1) {
            return Application.InvalidRequest.build();
        }

        Set<Player> gameSet = this.playerRepository.findByNameIgnoreCaseLike("%" + pattern[0] + "%");
        Set<String> playerNames = new HashSet<>();
        gameSet.forEach(player -> {
            playerNames.add(player.getName());
        });

        return ResponseEntity.ok(playerNames);
    }
}
