package com.example.contactlistserver.repository;

import com.example.contactlistserver.exception.ContactNotFoundException;
import com.example.contactlistserver.exception.UserNotFoundException;
import com.example.contactlistserver.model.Contact;
import com.example.contactlistserver.model.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ContactsRepository {

    @Autowired
    UsersRepository users;

    private final Map<Long, Contact> contacts = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Contact> getAll() { return new ArrayList<>(contacts.values()); }

    public Contact get(long contactId) {
        if (contacts.get(contactId) == null) {
            throw new ContactNotFoundException();
        }
        return contacts.get(contactId);
    }

    public List<Contact> getAllUserContacts(long userId) {
        if (users.get(userId) == null) {
            throw new UserNotFoundException();
        }
        return contacts.values().stream().filter(c -> c.getUserId() == userId).collect(Collectors.toList());
    }

    public List<Contact> searchByPhoneNumber(long userId, String phoneNumber) {
        if (users.get(userId) == null) {
            throw new UserNotFoundException();
        }
        PhoneNumber p = new PhoneNumber(phoneNumber);
        return contacts.values().stream()
                .filter(c -> c.getUserId() == userId && c.getPhoneNumber().equals(p))
                .collect(Collectors.toList());
    }

    public long add(Contact contact) {
        if (users.get(contact.getUserId()) == null) {
            throw new UserNotFoundException();
        }
        long contactId = counter.incrementAndGet();
        contacts.put(contactId, new Contact(contactId, contact.getUserId(), contact.getName(),
                contact.getPhoneNumber()));
        return contactId;
    }

    public void updateContact(long id, Contact contact) {
        Contact c = contacts.get(id);
        if (c == null) {
            throw new ContactNotFoundException();
        }

        String name = contact.getName();
        PhoneNumber number = contact.getPhoneNumber();

        if (name != null) { c.setName(name); }
        if (number != null) { c.setPhoneNumber(number);}
    }

    public boolean remove(long id) { return contacts.remove(id) != null; }

    public void removeAllUserContacts(long id) {
        contacts.values().removeIf(v -> v.getUserId().equals(id));
    }
}
