package com.kuuasema.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */

@Entity(name = "score")
public class Score {

    @ManyToOne(optional=false)
    private Player player;

    @Id
    private String id;

    @ManyToOne(optional=false)
    private Game game;

    private Double score;

    public Player getPlayer() {
        return player;
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Score(Player player, Game game, Double score) {
        this.id = UUID.randomUUID().toString();
        this.player = player;
        this.score = score;
        this.game = game;
    }

    Score() {}
}
