package com.davi.myfinances.service;

import com.davi.myfinances.exception.BusinessRuleException;
import com.davi.myfinances.model.repository.UserRepository;
import com.davi.myfinances.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    UserService userService;
    UserRepository repository;

    @BeforeEach
    public void setUp () {
        repository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(repository);
    }

    @Test
    public void shouldValidateEmail () {
        Assertions.assertDoesNotThrow(() -> {
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
            userService.validateEmail("email@email.com");
        });
    }

    @Test
    public void shouldThrowExceptionWhenThereIsEmailAlready () {
        Assertions.assertThrows(BusinessRuleException.class, () -> {
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

            userService.validateEmail("user@email.com");
        });
    }
}
