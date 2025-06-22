// com.filmapp.filmapp_backend.repository
package com.filmapp.filmapp_backend.repository;

import com.filmapp.filmapp_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
