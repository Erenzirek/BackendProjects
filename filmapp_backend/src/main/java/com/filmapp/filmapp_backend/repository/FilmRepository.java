package com.filmapp.filmapp_backend.repository;

import com.filmapp.filmapp_backend.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends MongoRepository<Film, String> {
    Film findByImdbId(String imdbId); // Opsiyonel: tekil film sorgusu için
}
