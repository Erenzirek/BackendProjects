// com.filmapp.filmapp_backend.controller
package com.filmapp.filmapp_backend.controller;

import com.filmapp.filmapp_backend.model.Film;
import com.filmapp.filmapp_backend.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@CrossOrigin(origins = "*")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @GetMapping
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmRepository.save(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable String id) {
        filmRepository.deleteById(id);
    }
}
