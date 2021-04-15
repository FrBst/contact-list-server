package com.example.contactlistserver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact {

    @JsonProperty("ContactId")
    private Long contactId;
    @JsonProperty("UserId")
    private Long userId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("PhoneNumber")
    private PhoneNumber phoneNumber;

    public Contact() { }

    public Contact(Long contactId, Long userId, String name, PhoneNumber phoneNumber) {
        this.contactId = contactId;
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) { this.contactId = contactId; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) { return true; }

        if (!other.getClass().equals(this.getClass())) { return false; }

        return this.contactId.equals(((Contact) other).contactId)
                && this.userId.equals(((Contact) other).userId)
                && this.name.equals(((Contact) other).name)
                && this.phoneNumber.equals(((Contact) other).phoneNumber);
    }
}
