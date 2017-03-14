package com.kuuasema.repository;

import com.kuuasema.model.Game;
import com.kuuasema.model.Player;
import com.kuuasema.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */
public interface ScoreRepository extends CrudRepository<Score, String> {
    Collection<Score> findByGameOrderByScoreDesc(Game game);
    Collection<Score> findByPlayer(Player player);
    Score findByPlayerAndGame(Player player, Game game);
}
