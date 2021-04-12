package com.example.contactlistserver.controller;

import com.example.contactlistserver.model.Contact;
import com.example.contactlistserver.model.ContactsRepository;
import com.example.contactlistserver.model.User;
import com.example.contactlistserver.model.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactsController {

    @Autowired
    private UsersRepository users;
    @Autowired
    private ContactsRepository contacts;

    @GetMapping("/contacts")
    public List<Contact> getAllContacts() {
        return contacts.getAll();
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable("id") int id) {
        Contact contact = contacts.get(id);
        if (contact == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/users/{userId}/contacts")
    public List<Contact> getAllUserContacts(@PathVariable("userId") int userId) {
        return contacts.getAllUserContacts(userId);
    }

    @GetMapping(value = "/users/{userId}/contacts", params = "phoneNumber")
    public List<Contact> searchByPhoneNumber(@PathVariable("userId") int userId,
                                             @RequestParam("phoneNumber") String phoneNumber) {
        return contacts.searchByPhoneNumber(userId, phoneNumber);
    }

    @PostMapping("/users/{userId}/contacts")
    public ResponseEntity<?> addUserContact(@PathVariable("userId") int userId,
                                            @RequestParam("name") String name,
                                            @RequestParam("phoneNumber") String phoneNumber) {
        if (users.get(userId) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(contacts.add(userId, name, phoneNumber));
    }

    @PatchMapping("/contacts/{id}")
    public ResponseEntity<?> changeContact(@PathVariable("id") int id,
                                           @RequestParam(name = "name", required = false) String name,
                                           @RequestParam(name = "phoneNumber", required = false) String phoneNumber) {
        if (users.get(id) == null)
            return ResponseEntity.notFound().build();
        if (name == null && phoneNumber == null) {
            return ResponseEntity.badRequest().build();
        }

        Contact c = contacts.get(id);
        if (name != null) { c.setName(name); }
        if (phoneNumber != null) { c.setPhoneNumber(phoneNumber); }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<?> removeContact(@PathVariable("id") int id) {
        if (contacts.remove(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
