// com.filmapp.filmapp_backend.repository
package com.filmapp.filmapp_backend.repository;

import com.filmapp.filmapp_backend.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FilmRepository extends MongoRepository<Film, String> {}
