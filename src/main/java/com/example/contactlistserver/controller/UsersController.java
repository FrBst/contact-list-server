package com.example.contactlistserver.controller;

import com.example.contactlistserver.wrapper.UserListWrapper;
import com.example.contactlistserver.exception.IllegalRequestException;
import com.example.contactlistserver.exception.UserNotFoundException;
import com.example.contactlistserver.model.User;
import com.example.contactlistserver.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    @Autowired
    private UsersRepository users;

    @GetMapping("/users")
    public ResponseEntity<UserListWrapper> getAllUsers() {
        return ResponseEntity.ok(new UserListWrapper(users.getAll()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok(users.get(id));
    }

    @GetMapping(value = "/users", params = "substring")
    public ResponseEntity<UserListWrapper> searchInName(@RequestParam("substring") String substring) {
        return ResponseEntity.ok(new UserListWrapper(users.searchInName(substring)));
    }

    @PostMapping("/users")
    public ResponseEntity<Long> addUser(@RequestBody User user) {
        String name = user.getName();
        if (user.getId() != null || name == null || name.isEmpty()) {
            throw new IllegalRequestException();
        }
        return ResponseEntity.ok(users.add(user));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> changeUser(@PathVariable("id") long id,
                                        @RequestBody User user) {
        String name = user.getName();
        if (user.getId() != null || name == null || name.isEmpty()) {
            throw new IllegalRequestException();
        }
        users.update(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> removeUser(@PathVariable("id") long id) {
        if (!users.remove(id)) {
            throw new UserNotFoundException();
        }
        return ResponseEntity.ok().build();
    }
}
