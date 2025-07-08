package com.filmapp.filmapp_backend.repository;

import com.filmapp.filmapp_backend.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByFilmId(String filmId);
}

