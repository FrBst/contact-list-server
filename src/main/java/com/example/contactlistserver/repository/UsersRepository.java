package com.example.contactlistserver.repository;

import com.example.contactlistserver.exception.UserNotFoundException;
import com.example.contactlistserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class UsersRepository {

    @Autowired
    ContactsRepository contacts;

    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public List<User> getAll() { return new ArrayList<>(users.values()); }

    public User get(long id) {
        if (users.get(id) == null) {
            throw new UserNotFoundException();
        }
        return users.get(id);
    }

    public List<User> searchInName(String substring) {
        String subLower = substring.toLowerCase();
        return users.values().stream().filter(u -> u.getName().toLowerCase().contains(subLower))
                .collect(Collectors.toList());
    }

    public long add(User user) {
        long userId = counter.incrementAndGet();
        users.put(userId, new User(userId, user.getName()));
        return userId;
    }

    public void update(long id, User user) {
        if (users.get(id) == null) {
            throw new UserNotFoundException();
        }
        if (user.getName() != null) {
            users.get(id).setName(user.getName());
        }
    }

    public boolean remove(long id) {
        if (users.get(id) == null) {
            throw new UserNotFoundException();
        }
        if (users.remove(id) != null) {
            contacts.removeAllUserContacts(id);
            return true;
        }
        return false;
    }
}
