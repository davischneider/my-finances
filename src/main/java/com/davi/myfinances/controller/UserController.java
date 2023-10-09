package com.davi.myfinances.controller;

import com.davi.myfinances.dto.UserDTO;
import com.davi.myfinances.exception.AuthException;
import com.davi.myfinances.exception.BusinessRuleException;
import com.davi.myfinances.model.entity.User;
import com.davi.myfinances.service.LaunchService;
import com.davi.myfinances.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LaunchService launchService;


    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody UserDTO dto) {
        try {
            User authUser = userService.auth(dto.getEmail(), dto.getPassword());
            return ResponseEntity.ok(authUser);
        } catch (AuthException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity save(@RequestBody UserDTO dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        try {
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    @GetMapping("{id}/balance")
    public ResponseEntity getBalance(@PathVariable("id") Long id) {
        Optional<User> user = userService.getById(id);

        if (user.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        BigDecimal balanceByUser = launchService.getBalanceByUser(id);
        return ResponseEntity.ok(balanceByUser);
    }
}
