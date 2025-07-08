package com.filmapp.filmapp_backend.controller;

import com.filmapp.filmapp_backend.model.Review;
import com.filmapp.filmapp_backend.repository.ReviewRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/{filmId}")
    public List<Review> getReviews(@PathVariable String filmId) {
        return reviewRepository.findByFilmId(filmId);
    }

    @PostMapping
    public Review postReview(@RequestBody Review review) {
        return reviewRepository.save(review);
    }
}
