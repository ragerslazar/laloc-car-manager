package com.ragerslazar.laloc.controller;

import com.ragerslazar.laloc.model.JDatabase;

public class User {
    private JDatabase db;

    public User() {
        this.db = new JDatabase();
    }

    public String loginController(String email, String password) {
        return this.db.loginDB(email, password);
    }
}
