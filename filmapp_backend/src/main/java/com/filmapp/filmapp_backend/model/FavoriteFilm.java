package com.filmapp.filmapp_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "favorite_films")
public class FavoriteFilm {

    @Id
    private String id;           // MongoDB ObjectId string hali

    private String userId;       // Favoriyi ekleyen kullanıcı ID'si

    private String filmImdbId;   // Favori film IMDb ID'si
}
