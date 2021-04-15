package com.example.contactlistserver.controller;

import com.example.contactlistserver.wrapper.ContactListWrapper;
import com.example.contactlistserver.exception.ContactNotFoundException;
import com.example.contactlistserver.exception.IllegalRequestException;
import com.example.contactlistserver.model.Contact;
import com.example.contactlistserver.repository.ContactsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactsController {

    @Autowired
    private ContactsRepository contacts;

    @GetMapping("/contacts")
    public ResponseEntity<ContactListWrapper> getAllContacts() {
        return ResponseEntity.ok(new ContactListWrapper(contacts.getAll()));
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable("id") long id) {
        return ResponseEntity.ok(contacts.get(id));
    }

    @GetMapping("/users/{userId}/contacts")
    public ResponseEntity<ContactListWrapper> getAllUserContacts(@PathVariable("userId") long userId) {
        return ResponseEntity.ok(new ContactListWrapper(contacts.getAllUserContacts(userId)));
    }

    @GetMapping(value = "/users/{userId}/contacts", params = "phoneNumber")
    public ResponseEntity<ContactListWrapper> searchByPhoneNumber(@PathVariable("userId") long userId,
                                                                  @RequestParam("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(new ContactListWrapper(contacts.searchByPhoneNumber(userId, phoneNumber)));
    }

    @PostMapping("/contacts")
    public ResponseEntity<Long> addContact(@RequestBody Contact contact) {
        if (contact.getContactId() != null
                || contact.getUserId() == null
                || contact.getName() == null || contact.getName().isEmpty()
                || contact.getPhoneNumber() == null) {
            throw new IllegalRequestException();
        }
        return ResponseEntity.ok(contacts.add(contact));
    }

    @PatchMapping("/contacts/{id}")
    public ResponseEntity<?> changeContact(@PathVariable("id") long id,
                                           @RequestBody Contact contact) {
        if (contact.getContactId() != null
                || contact.getUserId() != null
                || (contact.getName() == null || contact.getName() == "") && contact.getPhoneNumber() == null) {
            throw new IllegalRequestException();
        }
        contacts.updateContact(id, contact);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<?> removeContact(@PathVariable("id") long id) {
        if (!contacts.remove(id)) {
            throw new ContactNotFoundException();
        }
        return ResponseEntity.ok().build();
    }
}
