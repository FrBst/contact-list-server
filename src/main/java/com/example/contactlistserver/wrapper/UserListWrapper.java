package com.example.contactlistserver.wrapper;

import com.example.contactlistserver.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserListWrapper {
    @JsonProperty("Users")
    List<User> users;

    public UserListWrapper() { }

    public UserListWrapper(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int size() {
        return users.size();
    }
}
