package com.filmapp.filmapp_backend.controller;

import com.filmapp.filmapp_backend.model.Film;
import com.filmapp.filmapp_backend.repository.FilmRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@CrossOrigin(origins = "http://localhost:5173")
public class FilmController {

    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @GetMapping("/{imdbId}")
    public Film getFilmByImdbId(@PathVariable String imdbId) {
        return filmRepository.findByImdbId(imdbId);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmRepository.save(film);
    }
}
