package com.example.contactlistserver.controller;

import java.util.*;

import com.example.contactlistserver.model.User;
import com.example.contactlistserver.model.UsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    @Autowired
    private UsersRepository users;

    @Operation(summary = "Get a list of all users")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Returns list of all users (can be empty).",
                    content =
                            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
    ))
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users.getAll();
    }

    @Operation(summary = "Get a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of all users (can be empty).",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    })
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) {
        User user = users.get(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/users", params = "substring")
    public List<User> searchInName(@RequestParam("substring") String substring) {
        return users.searchInName(substring);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestParam("name") String name) {
        return ResponseEntity.ok(users.add(name));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> changeUser(@PathVariable("id") int id,
                                        @RequestParam("name") String name) {
        if (users.get(id) == null)
            return ResponseEntity.notFound().build();
        users.get(id).setName(name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> removeUser(@PathVariable("id") int id) {
        if (users.remove(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
