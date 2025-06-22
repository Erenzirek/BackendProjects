// com.filmapp.filmapp_backend.model
package com.filmapp.filmapp_backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "films")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @Id
    private String id;
    private String title;
    private String director;
    private int year;
    private String trailerLink;
    private String poster;
}
