package com.ragerslazar.laloc.controller;

import com.ragerslazar.laloc.model.JDatabase;

public class Garage {
    private JDatabase db;

    public Garage() {
        this.db = new JDatabase();
    }

    public Object[][] getGarage() {
        return this.db.queryGarage();
    }
}
