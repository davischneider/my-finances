package com.davi.myfinances.service;

import com.davi.myfinances.exception.AuthException;
import com.davi.myfinances.exception.BusinessRuleException;
import com.davi.myfinances.model.entity.User;
import com.davi.myfinances.model.repository.UserRepository;
import com.davi.myfinances.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @SpyBean
    UserServiceImpl userService;

    @MockBean
    UserRepository repository;

    @Test
    public void shouldAuthenticateAnUserWithSuccess() {
        String email = "email@email.com";
        String password = "password";

        User user = User.builder().email(email).password(password).id(1L).build();
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.auth(email,password);

        assertNotNull(result);
    }

    @Test
    public void shouldThrowAuthExceptionWhenThereIsNoUserWithEmail() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        AuthException authException = assertThrows(AuthException.class, () -> userService.auth("email@email.com", "password"));
        assertEquals("Usuário não encontrado para o email informado!",authException.getMessage());
    }

    @Test
    public void shouldThrowAuthExceptionWhenPasswordDoesNotMatch() {
        String email = "email@email.com";
        String password = "password";

        User user = User.builder().email(email).password(password).id(1L).build();
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        AuthException authException = assertThrows(AuthException.class, () -> userService.auth(email, "123"));
        assertEquals("Senha inválida!",authException.getMessage());
    }

    @Test
    public void shouldSaveUser() {
        doNothing().when(userService).validateEmail(anyString());
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("email@.com.br")
                .password("password")
                .build();

        when(repository.save(any(User.class))).thenReturn(user);

        User userSaved = userService.saveUser(user);

        assertNotNull(userSaved);
        assertEquals(user.getId(), userSaved.getId());
        assertEquals(user.getName(), userSaved.getName());
        assertEquals(user.getEmail(), userSaved.getEmail());
        assertEquals(user.getPassword(), userSaved.getPassword());
    }

    @Test
    public void shouldNotSaveUserWhenAlreadyThereIsUserWithEmail() {
        String email = "email@email.com";
        User user = User.builder().email(email).build();
        doThrow(BusinessRuleException.class).when(userService).validateEmail(email);

        assertThrows(BusinessRuleException.class, () -> userService.saveUser(user));
        verify(repository, never()).save(user);
    }

    @Test
    public void shouldValidateEmail () {
        assertDoesNotThrow(() -> {
            when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
            userService.validateEmail("email@email.com");
        });
    }

    @Test
    public void shouldThrowExceptionWhenThereIsEmailAlready () {
        assertThrows(BusinessRuleException.class, () -> {
            when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

            userService.validateEmail("user@email.com");
        });
    }
}
