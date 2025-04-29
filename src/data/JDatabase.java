package data;

import io.github.cdimascio.dotenv.Dotenv;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            if (cx != null) {
                System.out.println("Connexion réussie !");
            } else {
                System.out.println("Connexion Fail !");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver non trouvé");
        } catch (InstantiationException | SQLException e) {
            System.out.println("URL ou utilisateur/mot de passe incorrect");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String loginDB(String email, String password) {
        String authentication = "not_authenticated";

        String mdp = md5Hash(password);
        String req = "SELECT * FROM utilisateur WHERE email = ? AND password = ?";

        try {
            PreparedStatement pstmt = this.cx.prepareStatement(req);
            pstmt.setString(1, email);
            pstmt.setString(2, mdp);
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
            } else {
                System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Erreur de syntaxe SQL");
        } catch (SQLException e) {
            System.out.println("SQLException");
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            System.out.println("Serveur off");
        }

        return authentication;
    }


    public Object[][] queryVoitures() {
        ArrayList<Object[]> voituresList = new ArrayList<>();

        String req = "SELECT id_vehicule, marque, modele, immatriculation FROM vehicule WHERE dispo = ?";

        try {
            PreparedStatement pstmt = this.cx.prepareStatement(req);
            pstmt.setInt(1, 1); // dispo = 1
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_vehicule");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                String immatriculation = rs.getString("immatriculation");
                Object[] voiture = {id, marque, modele, immatriculation, "Action"};
                voituresList.add(voiture);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des véhicules: " + e.getMessage());
        }

        Object[][] data = new Object[voituresList.size()][5];
        for (int i = 0; i < voituresList.size(); i++) {
            data[i] = voituresList.get(i);
        }

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
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur de connexion ou d'exécution de la requête.");
        }

        return insertSuccess;
    }

    public ArrayList<Object[]> queryGarage() {
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
            e.printStackTrace();
            System.out.println("Erreur de connexion ou d'exécution de la requête.");
        }

        return result;
    }


    private String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

