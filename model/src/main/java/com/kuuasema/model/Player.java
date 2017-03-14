package com.kuuasema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */
@Entity(name = "player")
public class Player {
    @JsonIgnore
    @OneToMany(mappedBy = "player")
    private Set<Score> scores = new HashSet<Score>();

    @Id
    @JsonIgnore
    private String id;

    @Column(unique = true)
    private String name;

    public Set<Score> getScores() {
        return scores;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Player(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + " --- Name: " + getName() + "\n";
    }

    Player() {
    }
}
