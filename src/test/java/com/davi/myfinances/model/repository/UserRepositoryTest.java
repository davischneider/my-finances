package com.davi.myfinances.model.repository;

import com.davi.myfinances.model.entity.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void shouldVerifyIfEmailExists() {
        User user = createUser();
        entityManager.persist(user);

        boolean result = repository.existsByEmail("user@email.com");

        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenThereIsNoEmailAlready() {
        boolean result = repository.existsByEmail("user@email.com");

        Assertions.assertFalse(result);
    }

    @Test
    public void shouldPersistOneUserInDatabase() {
        User user = createUser();
        User userSave = repository.save(user);

        Assertions.assertNotNull(userSave.getId());
    }

    @Test
    public void shouldGetUserByEmail() {
        User user = createUser();
        entityManager.persist(user);

        Optional<User> result = repository.findByEmail("user@email.com");

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void  shouldNotReturnUserByEmail() {
        Optional<User> result = repository.findByEmail("user@email.com");

        Assertions.assertFalse(result.isPresent());
    }

    private static User createUser() {
        User user = User
                .builder()
                .name("user")
                .email("user@email.com")
                .password("password")
                .build();
        return user;
    }

}
