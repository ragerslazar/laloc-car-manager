package com.ragerslazar.laloc.view;

import com.ragerslazar.laloc.controller.Garage;
import com.ragerslazar.laloc.model.JDatabase;
import com.ragerslazar.laloc.controller.Voiture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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

        String[] columnNames = {"ID", "Marque", "Image", "Modèle", "Immatriculation", "Chevaux fiscaux", "Km", "Dispo", "Prix", "ID Garage", "Debut Location", "Fin Location", "Supprimer", "Modifier"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row >= 0 && col == 12) {
                    newFrame.dispose();
                    int confirm = JOptionPane.showConfirmDialog(scrollPane,
                            "Voulez-vous vraiment supprimer ce véhicule ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        int idVehicule = (int) table.getValueAt(row, 0); // Récupérer l'ID du véhicule
                        boolean deleteCar = voiture.deleteDB(idVehicule);
                        if (deleteCar) {
                            ((DefaultTableModel) table.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(table,"Véhicule supprimé avec succès !", "Info", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(table, "La voiture n'a pas pu être supprimée de la base.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    panel();
                } else if (row >= 0 && col == 13) {
                    newFrame.dispose();
                    updatePanel(table, row);
                }
            }
        });

        newFrame.add(scrollPane, BorderLayout.CENTER);

        newFrame.setVisible(true);
    }

    private void createPanel() {
        JFrame frame = new JFrame("Ajouter un véhicule");
        frame.setSize(400, 650); // Augmenter la hauteur pour les nouveaux champs
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

        // Ajout des nouveaux champs pour début et fin de location
        JLabel labelDebutLocation = new JLabel("Début de location:");
        JTextField fieldDebutLocation = new JTextField();

        JLabel labelFinLocation = new JLabel("Fin de location:");
        JTextField fieldFinLocation = new JTextField();

        JButton submitButton = new JButton("Ajouter");
        JButton closeButton = new JButton("Fermer");

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
            String debutLocation = fieldDebutLocation.getText();
            String finLocation = fieldFinLocation.getText();

            if (marque.isEmpty() || img.isEmpty() || modele.isEmpty() || immatriculation.isEmpty() || chevaux.isEmpty() || km.isEmpty() || dispo.isEmpty() || prix.isEmpty() || idGarage.isEmpty() || debutLocation.isEmpty() || finLocation.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Des données sont manquantes", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean filer = this.voiture.checkFilter(marque);
                if (!filer) {
                    this.voiture.addFilter(marque);
                }
                boolean insertDB = this.voiture.insertDB(marque, img, modele, immatriculation, chevaux, km, dispo, prix, idGarage, debutLocation, finLocation);
                if (insertDB) {
                    frame.dispose();
                    panel();
                } else {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de l'insertion.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        closeButton.addActionListener(_ -> {
            frame.dispose();
            panel();
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

        // Ajout des champs de date
        frame.add(labelDebutLocation);
        frame.add(fieldDebutLocation);

        frame.add(labelFinLocation);
        frame.add(fieldFinLocation);

        frame.add(submitButton);
        frame.add(closeButton);
        frame.add(grg);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private void updatePanel(JTable table, int row) {
        String idVehicule = table.getValueAt(row, 0).toString();

        JFrame modifFrame = new JFrame("Modifier véhicule");
        modifFrame.setSize(400, 650); // Augmenter la hauteur pour les nouveaux champs
        modifFrame.setLocationRelativeTo(null);
        modifFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modifFrame.setLayout(new GridLayout(22, 1, 5, 5));

        JLabel marqueLabel = new JLabel("Marque :");
        JLabel imageLabel = new JLabel("Image :");
        JLabel modeleLabel = new JLabel("Modèle :");
        JLabel immatLabel = new JLabel("Immatriculation :");
        JLabel chevauxLabel = new JLabel("Chevaux fiscaux :");
        JLabel kmLabel = new JLabel("Kilométrage :");
        JLabel dispoLabel = new JLabel("Disponible (true/false) :");
        JLabel prixLabel = new JLabel("Prix :");
        JLabel idGarageLabel = new JLabel("ID Garage :");
        JLabel debutLocationLabel = new JLabel("Début de location :");
        JLabel finLocationLabel = new JLabel("Fin de location :");

        JTextField marqueField = new JTextField(table.getValueAt(row, 1).toString());
        JTextField imageField = new JTextField(table.getValueAt(row, 2).toString());
        JTextField modeleField = new JTextField(table.getValueAt(row, 3).toString());
        JTextField immatField = new JTextField(table.getValueAt(row, 4).toString());
        JTextField chevauxField = new JTextField(table.getValueAt(row, 5).toString());
        JTextField kmField = new JTextField(table.getValueAt(row, 6).toString());
        JTextField dispoField = new JTextField(table.getValueAt(row, 7).toString());
        JTextField prixField = new JTextField(table.getValueAt(row, 8).toString());
        JTextField idGarageField = new JTextField(table.getValueAt(row, 9).toString());


        JTextField debutLocationField = new JTextField(table.getValueAt(row, 10).toString());
        JTextField finLocationField = new JTextField(table.getValueAt(row, 11).toString());

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

        // Ajout des champs de date
        modifFrame.add(debutLocationLabel);
        modifFrame.add(debutLocationField);
        modifFrame.add(finLocationLabel);
        modifFrame.add(finLocationField);

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
            String debutLocation = debutLocationField.getText();
            String finLocation = finLocationField.getText();

            boolean update = this.voiture.updateDB(idVehicule, marque, image, modele, immatriculation, chevaux, kilometrage, disponibilite, prix, idGarage, debutLocation, finLocation);
            if (update) {
                JOptionPane.showMessageDialog(modifFrame, "Véhicule modifié avec succès !", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(modifFrame, "Erreur lors de la modification.", "Info", JOptionPane.ERROR_MESSAGE);
            }
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
