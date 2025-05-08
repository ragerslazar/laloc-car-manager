package com.ragerslazar.laloc.controller;

import com.ragerslazar.laloc.model.JDatabase;

public class Garage {
    private JDatabase db;

    public Garage(JDatabase db) {
        this.db = db;
    }

    public Object[][] getGarage() {
        return this.db.queryGarage();
    }
}
