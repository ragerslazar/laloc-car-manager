package com.ragerslazar.laloc.data;

public class Garage {
    private JDatabase db;

    public Garage(JDatabase db) {
        this.db = db;
    }

    public Object[][] getGarage() {
        return this.db.queryGarage();
    }
}
