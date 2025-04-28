package com.global.controllers;

import com.global.entities.User;
import com.global.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
//@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(userService.fetchAllUser());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> add(@RequestBody User user){
        return ResponseEntity.ok().body(userService.add(user));
    }
}
