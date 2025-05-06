package com.ragerslazar.laloc.data;

public class Voiture {
    private JDatabase db;

    public Voiture(JDatabase db) {
        this.db = db;
    }

    public Object[][] getVoitures() {
        return this.db.queryVoitures();
    }

    public boolean insertDB(String marque, String img, String modele, String immatriculation, String chevaux, String km, String dispo, String prix, String idGarage) {
        return this.db.queryInsert(marque, img, modele, immatriculation, chevaux, km, dispo, prix, idGarage);
    }

    public boolean deleteDB(int id) {
        return this.db.queryDelete(id);
    }

    public boolean updateDB(String idVehicule, String marque, String img, String modele, String immatriculation, String chevaux, String km, String dispo, String prix, String idGarage) {
        return this.db.queryUpdate(idVehicule, marque, img, modele, immatriculation, chevaux, km, dispo, prix, idGarage);
    }
}
