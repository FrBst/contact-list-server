package com.example.contactlistserver.model;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class UsersRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    public List<User> getAll() { return new ArrayList<>(users.values()); }

    public User get(int id) {
        return users.get(id);
    }

    public List<User> searchInName(String substring) {
        String subLower = substring.toLowerCase();
        return users.values().stream().filter(u -> u.getName().toLowerCase().contains(subLower))
                .collect(Collectors.toList());
    }

    public int add(String name) {
        int userId = counter.incrementAndGet();
        users.put(userId, new User(userId, name));
        return userId;
    }

    public boolean remove(int id) { return users.remove(id) != null; }
}
