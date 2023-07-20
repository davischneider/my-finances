package com.davi.myfinances.model.repository;

import com.davi.myfinances.model.entity.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    public void shouldVerifyIfEmailExists() {
        User user = User.builder().name("user").email("user@email.com").build();
        repository.save(user);

        boolean result = repository.existsByEmail("user@email.com");

        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenThereIsNoEmailAlready() {
        repository.deleteAll();

        boolean result = repository.existsByEmail("user@email.com");

        Assertions.assertFalse(result);
    }
}
