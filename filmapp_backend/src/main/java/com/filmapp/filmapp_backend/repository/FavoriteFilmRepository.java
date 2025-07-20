package com.filmapp.filmapp_backend.repository;

import com.filmapp.filmapp_backend.model.FavoriteFilm;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FavoriteFilmRepository extends MongoRepository<FavoriteFilm, String> {

    List<FavoriteFilm> findByUserId(String userId);

    boolean existsByUserIdAndFilmImdbId(String userId, String filmImdbId);

    void deleteByUserIdAndFilmImdbId(String userId, String filmImdbId);
}
