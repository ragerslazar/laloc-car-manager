package com.ragerslazar.laloc.ui;

import com.ragerslazar.laloc.data.Garage;
import com.ragerslazar.laloc.data.JDatabase;
import com.ragerslazar.laloc.data.Voiture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
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
        newFrame.setSize(1100, 400);
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

        String[] columnNames = {"ID", "Marque", "Image", "Modèle", "Immatriculation", "Chevaux fiscaux", "Km", "Dispo", "Prix", "ID Garage", "Supprimer", "Modifier"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newFrame.dispose();
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                // Si clic sur la 5ème colonne (index 4)
                if (row >= 0 && col == 10) {
                    int confirm = JOptionPane.showConfirmDialog(scrollPane,
                            "Voulez-vous vraiment supprimer ce véhicule ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        int idVehicule = (int) table.getValueAt(row, 0); // Récupérer l'ID du véhicule
                        boolean deleteCar = voiture.deleteDB(idVehicule);
                        if (deleteCar) {
                            ((DefaultTableModel) table.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(table, "Véhicule supprimé avec succès !");
                        } else {
                            JOptionPane.showMessageDialog(table, "La voiture n'a pas pu être supprimée de la base.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    panel();
                } else if (row >= 0 && col == 11) {
                    updatePanel(table, row);
                }
            }
        });

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
                    frame.dispose();
                    panel();
                } else {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de l'insertion.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        String[] columnNames = {"ID", "Nom", "Adresse"};

        Object[][] data = this.garage.getGarage();

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

    private void updatePanel(JTable table, int row) {
        String idVehicule = table.getValueAt(row, 0).toString();

        JFrame modifFrame = new JFrame("Modifier véhicule");
        modifFrame.setSize(400, 600);
        modifFrame.setLocationRelativeTo(null);
        modifFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Utilise un layout en 18 lignes, 1 colonne : 9 labels + 9 champs
        modifFrame.setLayout(new GridLayout(20, 1, 5, 5));

        JLabel marqueLabel = new JLabel("Marque :");
        JLabel imageLabel = new JLabel("Image :");
        JLabel modeleLabel = new JLabel("Modèle :");
        JLabel immatLabel = new JLabel("Immatriculation :");
        JLabel chevauxLabel = new JLabel("Chevaux fiscaux :");
        JLabel kmLabel = new JLabel("Kilométrage :");
        JLabel dispoLabel = new JLabel("Disponible (true/false) :");
        JLabel prixLabel = new JLabel("Prix :");
        JLabel idGarageLabel = new JLabel("ID Garage :");

        JTextField marqueField = new JTextField(table.getValueAt(row, 1).toString());
        JTextField imageField = new JTextField(table.getValueAt(row, 2).toString());
        JTextField modeleField = new JTextField(table.getValueAt(row, 3).toString());
        JTextField immatField = new JTextField(table.getValueAt(row, 4).toString());
        JTextField chevauxField = new JTextField(table.getValueAt(row, 5).toString());
        JTextField kmField = new JTextField(table.getValueAt(row, 6).toString());
        JTextField dispoField = new JTextField(table.getValueAt(row, 7).toString());
        JTextField prixField = new JTextField(table.getValueAt(row, 8).toString());
        JTextField idGarageField = new JTextField(table.getValueAt(row, 9).toString());

        modifFrame.add(marqueLabel);
        modifFrame.add(marqueField);
        modifFrame.add(imageLabel);
        modifFrame.add(imageField);
        modifFrame.add(modeleLabel);
        modifFrame.add(modeleField);
        modifFrame.add(immatLabel);
        modifFrame.add(immatField);
        modifFrame.add(chevauxLabel);
        modifFrame.add(chevauxField);
        modifFrame.add(kmLabel);
        modifFrame.add(kmField);
        modifFrame.add(dispoLabel);
        modifFrame.add(dispoField);
        modifFrame.add(prixLabel);
        modifFrame.add(prixField);
        modifFrame.add(idGarageLabel);
        modifFrame.add(idGarageField);

        JButton saveButton = new JButton("Enregistrer");
        JButton closeButton = new JButton("Fermer");
        saveButton.addActionListener(_ -> {
            String marque = marqueField.getText();
            String image = imageField.getText();
            String modele = modeleField.getText();
            String immatriculation = immatField.getText();
            String chevaux = chevauxField.getText();
            String kilometrage = kmField.getText();
            String disponibilite = dispoField.getText();
            String prix = prixField.getText();
            String idGarage = idGarageField.getText();

            this.voiture.updateDB(idVehicule, marque, image, modele, immatriculation, chevaux, kilometrage, disponibilite, prix, idGarage);
            modifFrame.dispose();
            panel();
        });

        closeButton.addActionListener(_ -> {
            modifFrame.dispose();
            panel();
        });

        modifFrame.add(saveButton);
        modifFrame.add(closeButton);
        modifFrame.setVisible(true);
    }
}
