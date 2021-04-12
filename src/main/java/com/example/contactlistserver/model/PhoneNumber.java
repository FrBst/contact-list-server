package com.example.contactlistserver.model;

public final class PhoneNumber {
    private final String number;

    public PhoneNumber(String phoneNumber) {
        this.number = phoneNumber.replaceAll("[^1234567890]", "");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof PhoneNumber))
            return false;

        return this.number.equals(((PhoneNumber)other).number);
    }

    public String getNumber() {
        return number;
    }
}
