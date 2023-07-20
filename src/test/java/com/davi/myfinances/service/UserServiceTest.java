package com.davi.myfinances.service;

import com.davi.myfinances.exception.BusinessRuleException;
import com.davi.myfinances.model.entity.User;
import com.davi.myfinances.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository repository;

    @Test
    public void shouldValidateEmail () {
        Assertions.assertDoesNotThrow(() -> {
            repository.deleteAll();
            userService.validateEmail("email@email.com");
        });
    }

    @Test
    public void shouldThrowExceptionWhenThereIsEmailAlready () {
        Assertions.assertThrows(BusinessRuleException.class, () -> {
            User user = User.builder().name("user").email("user@email.com").build();
            repository.save(user);

            userService.validateEmail("user@email.com");
        });
    }
}
