package com.davi.myfinances.controller;

import com.davi.myfinances.dto.LaunchDTO;
import com.davi.myfinances.dto.UpdateStatusDTO;
import com.davi.myfinances.exception.BusinessRuleException;
import com.davi.myfinances.model.entity.Launch;
import com.davi.myfinances.model.entity.User;
import com.davi.myfinances.model.enums.LaunchStatus;
import com.davi.myfinances.model.enums.LaunchType;
import com.davi.myfinances.service.LaunchService;
import com.davi.myfinances.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/launch")
@RequiredArgsConstructor
public class LaunchController {

    private final LaunchService launchService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity save(@RequestBody LaunchDTO dto){
        try {
            Launch entity = convert(dto);
            entity = launchService.save(entity);
            return new ResponseEntity(entity, HttpStatus.CREATED);
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody LaunchDTO dto){
        return launchService.getById(id).map(entity -> {
            try {
                Launch launch = convert(dto);
                launch.setId(entity.getId());
                launchService.update(launch);
                return ResponseEntity.ok(launch);
            } catch (BusinessRuleException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{id}/updateStatus")
    public ResponseEntity updateStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO dto) {
        return launchService.getById(id).map(entity -> {
            LaunchStatus selectedStatus = LaunchStatus.valueOf(dto.getStatus());
            if (selectedStatus == null){
                return ResponseEntity.badRequest().body("Envie um status válido!");
            }
            try {
                entity.setStatus(selectedStatus);
                launchService.update(entity);
                return ResponseEntity.ok(entity);
            } catch (BusinessRuleException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return launchService.getById(id).map(entity -> {
            launchService.delete(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity get(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam("user") Long userId
    ){
        Launch launchFilter = new Launch();
        launchFilter.setDescription(description);
        launchFilter.setMonth(month);
        launchFilter.setYear(year);

        Optional<User> user = userService.getById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado com este Id.");
        } else {
            launchFilter.setUser(user.get());
        }

        List<Launch> launches = launchService.search(launchFilter);
        return ResponseEntity.ok(launches);
    }

    private Launch convert(LaunchDTO dto) {
        Launch launch = new Launch();
        launch.setId(dto.getId());
        launch.setDescription(dto.getDescription());
        launch.setYear(dto.getYear());
        launch.setMonth(dto.getMonth());
        launch.setValue(dto.getValue());

        User user = userService.getById(dto.getUser())
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado co Id informado!"));
        launch.setUser(user);

        if (dto.getStatus() != null) {
            launch.setStatus(LaunchStatus.valueOf(dto.getStatus()));
        }
        if (dto.getType() != null) {
            launch.setType(LaunchType.valueOf(dto.getType()));
        }

        return launch;
    }
}
