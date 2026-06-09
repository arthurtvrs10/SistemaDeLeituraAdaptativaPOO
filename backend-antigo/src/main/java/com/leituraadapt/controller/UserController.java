package com.leituraadapt.controller;

import com.leituraadapt.model.UserEntity;
import com.leituraadapt.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping
    public UserEntity save(@RequestBody UserEntity user) {
        return service.save(user);
    }

    @GetMapping
    public List<UserEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserEntity findById(@PathVariable String id) {
        return service.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }
}
