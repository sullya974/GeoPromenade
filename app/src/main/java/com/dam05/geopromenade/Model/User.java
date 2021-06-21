package com.dam05.geopromenade.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class User {
    private String email;
    @Exclude
    private String password;
    private String id;
    private String firstname;
    private String lastname;

    @Exclude
    private boolean created;
    private String bio;

    public User(String name, String email, String password) {
        this.firstname = name;
        this.email = email;
        this.password = password;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String avatar;

    public User() {
    }

    public User(String id, String firstname, String lastname, String bio, String avatar) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
        this.avatar = avatar;
    }

    @Exclude
    public boolean isCreated() {
        return created;
    }
    @Exclude
    public void setCreated(boolean created) {
        this.created = created;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBio() {
        return this.bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }
    @Exclude
    public void setPassword(String password) {
        this.password = password;
    }
}
