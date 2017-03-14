package com.kuuasema.repository;

import com.kuuasema.model.Game;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by DuyAnhPham on 12/03/2017.
 */
public interface GameRepository extends CrudRepository<Game, String> {
    Game findByTitle(String title);
    Set<Game> findByTitleIgnoreCaseLike(String pattern);
}
