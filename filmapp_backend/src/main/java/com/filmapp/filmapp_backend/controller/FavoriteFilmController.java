package com.filmapp.filmapp_backend.controller;

import com.filmapp.filmapp_backend.model.FavoriteFilm;
import com.filmapp.filmapp_backend.repository.FavoriteFilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteFilmController {

    @Autowired
    private FavoriteFilmRepository favoriteFilmRepository;

    // Favori film ekle
    @PostMapping
    public ResponseEntity<String> addFavorite(@RequestBody FavoriteFilm favoriteFilm) {
        if (favoriteFilmRepository.existsByUserIdAndFilmImdbId(favoriteFilm.getUserId(), favoriteFilm.getFilmImdbId())) {
            return ResponseEntity.badRequest().body("Film zaten favorilerde");
        }
        favoriteFilmRepository.save(favoriteFilm);
        return ResponseEntity.ok("Favori film eklendi");
    }

    // Kullanıcının favori filmlerini listele
    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteFilm>> getFavorites(@PathVariable String userId) {
        List<FavoriteFilm> favorites = favoriteFilmRepository.findByUserId(userId);
        return ResponseEntity.ok(favorites);
    }

    // Favori film sil
    @DeleteMapping
    public ResponseEntity<String> deleteFavorite(@RequestParam String userId, @RequestParam String filmImdbId) {
        favoriteFilmRepository.deleteByUserIdAndFilmImdbId(userId, filmImdbId);
        return ResponseEntity.ok("Favori film silindi");
    }
}
