package com.example.contactlistserver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonProperty("Id")
    private Long id;
    @JsonProperty("Name")
    private String name;

    public User() { }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object other) {
        if (other == this) { return true; }

        if (!other.getClass().equals(this.getClass())) { return false; }

        return this.id.equals(((User) other).id)
                && this.name.equals(((User) other).name);
    }
}