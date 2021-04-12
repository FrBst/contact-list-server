package com.example.contactlistserver.model;

public class Contact {
    private final int contactId;
    private final int userId;
    private String name;
    private PhoneNumber phoneNumber;

    public Contact(int contactId, int userId, String name, String phoneNumber) {
        this.contactId = contactId;
        this.userId = userId;
        this.name = name;
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public int getContactId() {
        return contactId;
    }

    public int getUserId() { return userId; }

    public String getName() {
        return name;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) { this.name = name; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = new PhoneNumber(phoneNumber); }
}
