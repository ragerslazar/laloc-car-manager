package com.ragerslazar.laloc.model;

import io.github.cdimascio.dotenv.Dotenv;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

public class JDatabase {
    private Connection cx;
    public JDatabase() {
        try {
            Dotenv dotenv = Dotenv.configure().load();
            String dbHost = dotenv.get("DB_HOST");
            String dbUsername = dotenv.get("DB_USERNAME");
            String dbPassword = dotenv.get("DB_PASSWORD");
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.cx = DriverManager.getConnection(dbHost, dbUsername, dbPassword);
            System.out.println("Connexion réussie !");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver non trouvé");
        } catch (InstantiationException | SQLException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection connection() {
        return this.cx;
    }

    public String loginDB(String email, String password) {
        String authentication = "not_authenticated";

        String hashedPwd = getHashedPwd(email);

        if (hashedPwd != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPwd);

            if (result.verified) {
                String req = "SELECT * FROM utilisateur WHERE email = ?";

                try {
                    PreparedStatement pstmt = this.cx.prepareStatement(req);
                    pstmt.setString(1, email);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String type = rs.getString("type");
                        if (type.equals("admin")) {
                            authentication = "approved";
                            System.out.println("Connexion réussie !");
                        } else {
                            authentication = "admin_perm_missing";
                            System.out.println("Compte admin nécessaire.");
                        }
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("Serveur off");
                }
            } else {
                System.out.println("Mot de passe incorrect.");
            }
        }
        return authentication;
    }


    public Object[][] queryVoitures() {
        ArrayList<Object[]> voituresList = new ArrayList<>();

        String req = "SELECT * FROM vehicule WHERE dispo = ?";

        try {
            PreparedStatement pstmt = this.cx.prepareStatement(req);
            pstmt.setInt(1, 1); // dispo = 1
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_vehicule");
                String marque = rs.getString("marque");
                String img = rs.getString("img");
                String modele = rs.getString("modele");
                String immatriculation = rs.getString("immatriculation");
                byte cf = rs.getByte("chevaux_fiscaux");
                int km = rs.getInt("km");
                byte dispo = rs.getByte("dispo");
                int prix = rs.getInt("prix");
                int id_garage = rs.getInt("id_garage");
                Object[] voiture = {id, marque, img, modele, immatriculation, cf, km, dispo, prix, id_garage, "❌", "✏️"};
                voituresList.add(voiture);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des véhicules: " + e.getMessage());
        }

        Object[][] data = new Object[voituresList.size()][11];
        voituresList.toArray(data);

        return data;
    }


    public boolean queryInsert(String marque, String img, String modele, String immatriculation, String chevaux, String km, String dispo, String prix, String idGarage) {
        boolean insertSuccess = false;
        String req = "INSERT INTO vehicule (id_vehicule, marque, img, modele, immatriculation, chevaux_fiscaux, km, dispo, prix, id_garage) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = this.cx.prepareStatement(req);

            stmt.setString(1, marque);
            stmt.setString(2, img);
            stmt.setString(3, modele);
            stmt.setString(4, immatriculation);
            stmt.setString(5, chevaux);
            stmt.setString(6, km);
            stmt.setString(7, dispo);
            stmt.setString(8, prix);
            stmt.setString(9, idGarage);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                insertSuccess = true;
                System.out.println("Véhicule ajouté avec succès.");
            } else {
                System.out.println("Échec de l'ajout du véhicule.");
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Erreur de syntaxe SQL : " + e.getMessage());
        } catch (SQLIntegrityConstraintViolationException | NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Erreur de connexion ou d'exécution de la requête.");
        }

        return insertSuccess;
    }

    public boolean queryDelete(int id) {
        boolean deleted = false;
        String req = "DELETE FROM `vehicule` WHERE id_vehicule = ?";
        try {
            PreparedStatement pstmt = this.cx.prepareStatement(req);
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                deleted = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deleted;
    }

    public boolean queryUpdate(String idVehicule, String marque, String img, String modele, String immatriculation, String chevaux, String km, String dispo, String prix, String idGarage) {
        boolean updateSuccess = false;
        String req = "UPDATE vehicule SET marque = ?, img = ?, modele = ?, immatriculation = ?, chevaux_fiscaux = ?, km = ?, dispo = ?, prix = ?, id_garage = ? WHERE id_vehicule = ?;";

        try {
            PreparedStatement stmt = this.cx.prepareStatement(req);

            stmt.setString(1, marque);
            stmt.setString(2, img);
            stmt.setString(3, modele);
            stmt.setString(4, immatriculation);
            stmt.setString(5, chevaux);
            stmt.setString(6, km);
            stmt.setString(7, dispo);
            stmt.setString(8, prix);
            stmt.setString(9, idGarage);
            stmt.setString(10, idVehicule);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                updateSuccess = true;
                System.out.println("Véhicule modifié avec succès.");
            } else {
                System.out.println("Échec de la modification.");
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Erreur de syntaxe SQL : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Erreur de connexion ou d'exécution de la requête.");
        }

        return updateSuccess;
    }

    public Object[][] queryGarage() {
        ArrayList<Object[]> result = new ArrayList<>();

        String req = "SELECT * FROM garage";
        try {
            Statement stmt = this.cx.createStatement();
            ResultSet rs = stmt.executeQuery(req);

            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("id_garage");
                row[1] = rs.getString("nom");
                row[2] = rs.getString("adresse");

                result.add(row);
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Erreur de syntaxe SQL : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Erreur de connexion ou d'exécution de la requête.");
        }
        Object[][] data = new Object[result.size()][3];
        result.toArray(data);

        return data;
    }

    private String getHashedPwd(String email) {
        String hashedPwd = null;

        String req = "SELECT password FROM utilisateur WHERE email = ?";
        try {
            PreparedStatement pstmt = this.cx.prepareStatement(req);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                hashedPwd = rs.getString("password");
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return hashedPwd;
    }

}

