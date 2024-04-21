import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageMember extends JFrame {
    private JComboBox<String> MemberCombo;
    private JTextField FieldName;
    private JTextField FieldFirstName;
    private JTextField FieldMail;

    public ManageMember() {

        super("AP2 - Gérer les Adhérents");

        JPanel Panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);



        // Label et ComboBox pour Sélectionner un Adhérent
        gbc.gridx = 0;
        gbc.gridy = 0;
        Panel.add(new JLabel("Sélectionner un adhérent :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        MemberCombo = new JComboBox<>();
        LoadMemberCombo();
        MemberCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DataSelectedMember();
            }
        });
        Panel.add(MemberCombo, gbc);


        
        gbc.gridx = 0;
        gbc.gridy = 1;
        Panel.add(new JLabel("Nom de famille :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        FieldName = new JTextField(50);
        Panel.add(FieldName, gbc);


        
        gbc.gridx = 0;
        gbc.gridy = 2;
        Panel.add(new JLabel("Prénom :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        FieldFirstName = new JTextField(50);
        Panel.add(FieldFirstName, gbc);


        
        gbc.gridx = 0;
        gbc.gridy = 3;
        Panel.add(new JLabel("Adresse e-mail :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        FieldMail = new JTextField(50);
        Panel.add(FieldMail, gbc);


        
        // Boutton Ajouter
        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton AddButton = new JButton("Ajouter adhérent");
        AddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddMember();
            }
        });
        Panel.add(AddButton, gbc);


        
        // Boutton Modifier
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton ModifyButton = new JButton("Modifier adhérent");
        ModifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ModifyMember();
            }
        });
        Panel.add(ModifyButton, gbc);



        // Boutton Supprimer
        gbc.gridx = 2;
        gbc.gridy = 5;
        JButton DeleteButton = new JButton("Supprimer adhérent");
        DeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteMember();
            }
        });
        Panel.add(DeleteButton, gbc);

        // Configuration de la fenêtre
        add(Panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1200, 800);
        setVisible(true);
    }



    // Charger la liste des Adhérents dans le ComboBox
    // ComboBox : est un composant d'interface utilisateur qui combine une liste déroulante avec un champ de texte
    private void LoadMemberCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nom, prenom FROM adherent");

            while (rs.next()) {
                MemberCombo.addItem(rs.getString("nom") + " " + rs.getString("prenom"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    // Remplir les champs avec les données de l'adhérent
    private void DataSelectedMember() {
        String SelectedMember = (String) MemberCombo.getSelectedItem();
        if (SelectedMember != null) {
            String[] nomPrenom = SelectedMember.split(" ");
            String nom = nomPrenom[0];
            String prenom = nomPrenom[1];

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
                PreparedStatement stmt = con.prepareStatement("SELECT email FROM adherent WHERE nom = ? AND prenom = ?");
                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    FieldName.setText(nom);
                    FieldFirstName.setText(prenom);
                    FieldMail.setText(rs.getString("email"));
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    // Vérification de l'existance de l'adhérent
    private boolean MemberAvailabe(String nom, String prenom) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS count FROM adherent WHERE nom = ? AND prenom = ?");
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                return count == 0; // count == 0, alors l'adhérent n'est pas existant
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }



    // Ajouter un Adhérent
    private void AddMember() {
        String nom = FieldName.getText();
        String prenom = FieldFirstName.getText();
        String email = FieldMail.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!MemberAvailabe(nom, prenom)) {
            JOptionPane.showMessageDialog(this, "Cet adhérent existe déjà.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO adherent (nom, prenom, email) VALUES (?, ?, ?)");
            insertStmt.setString(1, nom);
            insertStmt.setString(2, prenom);
            insertStmt.setString(3, email);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Adhérent ajouté avec succès.");
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + ex.getMessage(), "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Modifier un Adhérent
    private void ModifyMember() {
        String nom = FieldName.getText();
        String prenom = FieldFirstName.getText();
        String email = FieldMail.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // if (!MemberAvailabe(nom, prenom)) {
        //     JOptionPane.showMessageDialog(this, "Cet adhérent n'existe pas.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
        //     return;
        // }

        // int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir modifier les données de cet adhérent ?", "Confirmation de modification", JOptionPane.YES_NO_OPTION);
        // if (option == JOptionPane.YES_OPTION) {
        //     try {
        //         Class.forName("com.mysql.jdbc.Driver");
        //         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

        //         PreparedStatement updateStmt = con.prepareStatement("UPDATE adherent SET email = ? WHERE nom = ? AND prenom = ?");
        //         updateStmt.setString(1, email);
        //         updateStmt.setString(2, nom);
        //         updateStmt.setString(3, prenom);
        //         int rowsAffected = updateStmt.executeUpdate();

        //         if (rowsAffected > 0) {
        //             JOptionPane.showMessageDialog(this, "Adhérent modifié avec succès.");
        //         } else {
        //             JOptionPane.showMessageDialog(this, "Aucun adhérent trouvé avec ces informations.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
        //         }

        //         con.close();
        //     } catch (Exception ex) {
        //         JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + ex.getMessage(), "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
        //     }


        
        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir modifier cet adhérent ?", "Confirmation de modification", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

                PreparedStatement updateStmt = con.prepareStatement("UPDATE adherent SET nom = ?, prenom = ?, email = ? WHERE nom = ? AND prenom = ?");
                updateStmt.setString(1, nom);
                updateStmt.setString(2, prenom);
                updateStmt.setString(3, email);
                updateStmt.setString(4, MemberCombo.getSelectedItem().toString().split(" ")[0]);
                updateStmt.setString(5, MemberCombo.getSelectedItem().toString().split(" ")[1]);
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Adhérent mis à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun adhérent trouvé avec ces informations.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        }
    }



    // Supprimer un Adhérent
    private void DeleteMember() {
        String nom = FieldName.getText();
        String prenom = FieldFirstName.getText();

        if (nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet adhérent ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

                PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM adherent WHERE nom = ? AND prenom = ?");
                deleteStmt.setString(1, nom);
                deleteStmt.setString(2, prenom);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Adhérent supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun adhérent trouvé avec ces informations.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + ex.getMessage(), "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManageMember();
            }
        });
    }


}
