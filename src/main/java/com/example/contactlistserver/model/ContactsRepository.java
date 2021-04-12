package com.example.contactlistserver.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ContactsRepository {
    private final Map<Integer, Contact> contacts = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    public List<Contact> getAll() { return new ArrayList<>(contacts.values()); }

    public Contact get(int contactId) { return contacts.get(contactId); }

    public List<Contact> getAllUserContacts(int userId) {
        return contacts.values().stream().filter(c -> c.getUserId() == userId).collect(Collectors.toList());
    }

    public List<Contact> searchByPhoneNumber(int owner, String phoneNumber) {
        PhoneNumber p = new PhoneNumber(phoneNumber);
        return contacts.values().stream()
                .filter(c -> c.getUserId() == owner && c.getPhoneNumber().equals(p))
                .collect(Collectors.toList());
    }

    public int add(int userId, String name, String phoneNumber) {
        int contactId = counter.incrementAndGet();
        contacts.put(contactId, new Contact(contactId, userId, name, phoneNumber));
        return contactId;
    }

    public boolean remove(int id) { return contacts.remove(id) != null; }
}
