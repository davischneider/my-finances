package com.davi.myfinances.service.impl;

import com.davi.myfinances.exception.AuthException;
import com.davi.myfinances.exception.BusinessRuleException;
import com.davi.myfinances.model.entity.User;
import com.davi.myfinances.model.repository.UserRepository;
import com.davi.myfinances.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User auth(String email, String password) {
        Optional<User> user = repository.findByEmail(email);

        if (!user.isPresent()) {
            throw new AuthException("Usuário não encontrado para o email informado!");
        }

        if (!user.get().getPassword().equals(password)) {
            throw new AuthException("Senha inválida!");
        }
        return user.get();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        validateEmail(user.getEmail());
        return repository.save(user);
    }

    @Override
    public void validateEmail(String email) {
        boolean exists = repository.existsByEmail(email);
        if (exists) {
            throw new BusinessRuleException("Já existe usuário cadastrado com o email informado!");
        }
    }

    @Override
    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }
}
