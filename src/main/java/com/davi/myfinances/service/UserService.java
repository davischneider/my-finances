package com.davi.myfinances.service;

import com.davi.myfinances.model.entity.User;

import java.util.Optional;

public interface UserService {
    User auth(String email, String password);

    User saveUser(User usuario);

    void validateEmail(String email);

    Optional<User> getById(Long id);
}
