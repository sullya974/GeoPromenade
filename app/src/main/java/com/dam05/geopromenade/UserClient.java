package com.dam05.geopromenade;

import android.app.Application;

import com.dam05.geopromenade.Model.User;

public class UserClient extends Application {
    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
