package com.ragerslazar.laloc.data;

import java.util.ArrayList;

public class Garage {
    private JDatabase db;

    public Garage(JDatabase db) {
        this.db = db;
    }

    public Object[][] getGarage() {
        return this.db.queryGarage();
    }
}
