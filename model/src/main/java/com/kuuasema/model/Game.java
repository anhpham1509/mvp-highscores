package com.kuuasema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */

@Entity(name = "game")
public class Game {

    @Id
    @JsonIgnore
    private String id;

    @Column(unique = true)
    private String title;

    @JsonIgnore
    @OneToMany(mappedBy = "game")
    private Set<Score> scores = new HashSet<Score>();

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Game(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
    }

    Game(){}
}

