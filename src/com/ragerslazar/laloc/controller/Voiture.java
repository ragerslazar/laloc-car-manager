package com.ragerslazar.laloc.controller;

import com.ragerslazar.laloc.model.JDatabase;

public class Voiture {
    private JDatabase db;

    public Voiture() {
        this.db = new JDatabase();
    }

    public Object[][] getVoitures() {
        return this.db.queryVoitures();
    }

    public boolean insertDB(String marque, String img, String modele, String immatriculation, String chevaux, String km, String dispo, String prix, String idGarage, String debut_loc, String fin_loc) {
        return this.db.queryInsert(marque, img, modele, immatriculation, chevaux, km, dispo, prix, idGarage, debut_loc, fin_loc);
    }

    public boolean deleteDB(int id) {
        return this.db.queryDelete(id);
    }

    public boolean updateDB(String idVehicule, String marque, String img, String modele, String immatriculation, String chevaux, String km, String dispo, String prix, String idGarage, String debut_loc, String fin_loc) {
        return this.db.queryUpdate(idVehicule, marque, img, modele, immatriculation, chevaux, km, dispo, prix, idGarage, debut_loc, fin_loc);
    }

    public boolean checkFilter(String filter) {
        return this.db.checkFiler(filter);
    }

    public boolean addFilter(String filter) {
        return this.db.addFilter(filter);
    }
}
