package com.ragerslazar.laloc.ui;

import com.ragerslazar.laloc.data.Garage;
import com.ragerslazar.laloc.data.JDatabase;
import com.ragerslazar.laloc.data.Voiture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class IHM {

    private JDatabase db;
    private Voiture voiture;
    private Garage garage;

    public IHM() {
        this.db = new JDatabase();
        this.voiture = new Voiture(db);
        this.garage = new Garage(db);
    }
    public void login() {
        JFrame frame = new JFrame("Connexion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JLabel userLabel = new JLabel("Email:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Mot de passe:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Se connecter");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel()); // Pour pousser le bouton a droite
        panel.add(loginButton);

        frame.add(panel);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String login_req = db.loginDB(username, password);

            if (login_req.equals("approved")) {
                JOptionPane.showMessageDialog(frame, "Connexion réussie !");
                panel();
                frame.dispose();
            } else if (login_req.equals("not_authenticated")) {
                JOptionPane.showMessageDialog(frame, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else if (login_req.equals("admin_perm_missing")) {
                JOptionPane.showMessageDialog(frame, "Compte administrateur nécessaire !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void panel() {
        Object[][] data = this.voiture.getVoitures();

        JFrame newFrame = new JFrame("Interface Utilisateur");
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(600, 400);
        newFrame.setLocationRelativeTo(null);
        newFrame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton addButton = new JButton("Ajouter un véhicule");

        addButton.addActionListener(e -> {
            createPanel();
            newFrame.dispose();
        });

        topPanel.add(addButton);

        newFrame.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Marque", "Modèle", "Immatriculation", "Action"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        newFrame.add(scrollPane, BorderLayout.CENTER);

        newFrame.setVisible(true);
    }

    private void createPanel() {

        JFrame frame = new JFrame("Ajouter un véhicule");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JLabel grg = new JLabel("Garages:");
        JLabel labelMarque = new JLabel("Marque:");
        JTextField fieldMarque = new JTextField();

        JLabel labelImg = new JLabel("Image:");
        JTextField fieldImg = new JTextField();

        JLabel labelModele = new JLabel("Modèle:");
        JTextField fieldModele = new JTextField();

        JLabel labelImmatriculation = new JLabel("Immatriculation:");
        JTextField fieldImmatriculation = new JTextField();

        JLabel labelChevaux = new JLabel("Chevaux Fiscaux:");
        JTextField fieldChevaux = new JTextField();

        JLabel labelKm = new JLabel("Kilométrage:");
        JTextField fieldKm = new JTextField();

        JLabel labelDispo = new JLabel("Disponible (0 ou 1):");
        JTextField fieldDispo = new JTextField();

        JLabel labelPrix = new JLabel("Prix:");
        JTextField fieldPrix = new JTextField();

        JLabel labelGarage = new JLabel("ID Garage:");
        JTextField fieldGarage = new JTextField();

        JButton submitButton = new JButton("Ajouter");
        submitButton.addActionListener(_ -> {
            String marque = fieldMarque.getText();
            String img = fieldImg.getText();
            String modele = fieldModele.getText();
            String immatriculation = fieldImmatriculation.getText();
            String chevaux = fieldChevaux.getText();
            String km = fieldKm.getText();
            String dispo = fieldDispo.getText();
            String prix = fieldPrix.getText();
            String idGarage = fieldGarage.getText();

            if (marque.isEmpty() || img.isEmpty() || modele.isEmpty() || immatriculation.isEmpty() || chevaux.isEmpty() || km.isEmpty() || dispo.isEmpty() || prix.isEmpty() || idGarage.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Des données sont manquantes", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean insertDB = this.voiture.insertDB(marque, img, modele, immatriculation, chevaux, km, dispo, prix, idGarage);
                if (insertDB) {
                    frame.dispose(); //Ferme la frame
                    panel();
                } else {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de l'insertion.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        List<Object[]> mGarage = this.garage.getGarage();

        String[] columnNames = {"ID", "Nom", "Adresse"};

        Object[][] data = new Object[mGarage.size()][3];
        mGarage.toArray(data);

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(labelMarque);
        frame.add(fieldMarque);

        frame.add(labelImg);
        frame.add(fieldImg);

        frame.add(labelModele);
        frame.add(fieldModele);

        frame.add(labelImmatriculation);
        frame.add(fieldImmatriculation);

        frame.add(labelChevaux);
        frame.add(fieldChevaux);

        frame.add(labelKm);
        frame.add(fieldKm);

        frame.add(labelDispo);
        frame.add(fieldDispo);

        frame.add(labelPrix);
        frame.add(fieldPrix);

        frame.add(labelGarage);
        frame.add(fieldGarage);

        frame.add(submitButton);
        frame.add(grg);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
