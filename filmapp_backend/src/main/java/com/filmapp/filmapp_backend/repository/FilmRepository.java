package com.filmapp.filmapp_backend.repository;

import com.filmapp.filmapp_backend.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;

@Repository
public interface FilmRepository extends MongoRepository<Film, ObjectId> {
    Film findByImdbId(String imdbId);
}
