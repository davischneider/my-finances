package com.davi.myfinances.service.impl;

import com.davi.myfinances.model.entity.User;
import com.davi.myfinances.model.repository.UserRepository;
import com.davi.myfinances.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User auth(String email, String password) {
        return null;
    }

    @Override
    public User saveUser(User usuario) {
        return null;
    }

    @Override
    public void validateEmail(String email) {

    }
}
