package com.example.contactlistserver.wrapper;

import com.example.contactlistserver.model.Contact;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContactListWrapper {
    @JsonProperty("Contacts")
    List<Contact> contacts;

    public ContactListWrapper() { }

    public ContactListWrapper(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public int size() {
        return contacts.size();
    }
}
