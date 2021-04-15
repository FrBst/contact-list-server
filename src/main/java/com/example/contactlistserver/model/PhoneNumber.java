package com.example.contactlistserver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneNumber {

    @JsonProperty("Number")
    private String number;

    public PhoneNumber() { }

    public PhoneNumber(String phoneNumber) {
        setNumber(phoneNumber);
    }

    public String getNumber() { return number; }

    public void setNumber(String phoneNumber) {
        if (phoneNumber != null) {
            String onlyDigits = phoneNumber.replaceAll("[^1234567890]", "");
            if (onlyDigits.length() == 0) {
                throw new IllegalArgumentException();
            }
            this.number = onlyDigits;
        }
        else {
            this.number = null;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) { return true; }

        if (!other.getClass().equals(this.getClass())) { return false; }

        return this.number.equals(((PhoneNumber) other).number);
    }
}
