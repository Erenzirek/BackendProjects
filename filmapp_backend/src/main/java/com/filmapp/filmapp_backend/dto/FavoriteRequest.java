package com.filmapp.filmapp_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteRequest {
    private String userId;
    private String filmImdbId;
}
