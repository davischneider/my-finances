package com.davi.myfinances.service;

import com.davi.myfinances.model.entity.User;

public interface UserService {
    User auth(String email, String password);

    User saveUser(User usuario);

    void validateEmail(String email);
}
