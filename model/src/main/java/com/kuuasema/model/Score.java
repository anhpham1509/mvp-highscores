package com.kuuasema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    //@JoinColumn(name="player_id")
    //@JoinColumn(referencedColumnName = "id")
    private Player player;

    @Id
    @JsonIgnore
    private String id;

    @ManyToOne(optional=false)
    //@JoinColumn(referencedColumnName = "id")
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
