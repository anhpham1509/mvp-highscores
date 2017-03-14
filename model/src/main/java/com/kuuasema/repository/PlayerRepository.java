package com.kuuasema.repository;

import com.kuuasema.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */
public interface PlayerRepository extends CrudRepository<Player, String> {
    Player findByName(String name);
    Player findById(String id);
}
