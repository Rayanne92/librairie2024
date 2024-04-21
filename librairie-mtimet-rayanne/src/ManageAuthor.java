import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageAuthor extends JFrame {
    private JComboBox<String> AuthorCombo;
    private JTextField FieldName;
    private JTextField FieldFirstName;
    private JTextField FieldDateOfBirth;
    private JTextField FieldDescription;

    // Stockage de l'ID de l'auteur choisit
    private int SelectedAuthorID;

    public ManageAuthor() {
        super("AP2 - Gérer les auteurs");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sélectionner un auteur :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        AuthorCombo = new JComboBox<>();
        LoadAuthorCombo();
        AuthorCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DataSelectedAuthor();
            }
        });
        panel.add(AuthorCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nom de famille :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        FieldName = new JTextField(20);
        panel.add(FieldName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prénom :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        FieldFirstName = new JTextField(20);
        panel.add(FieldFirstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date de naissance :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        FieldDateOfBirth = new JTextField(20);
        panel.add(FieldDateOfBirth, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Description :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        FieldDescription = new JTextField(20);
        panel.add(FieldDescription, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;


        
        // Boutton Ajouter
        JButton AddButton = new JButton("Ajouter Auteur");
        AddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddAuthor();
            }
        });
        panel.add(AddButton, gbc);


        
        // Boutton Modifier
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton ModifyButton = new JButton("Modifier Auteur");
        ModifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ModifyAuthor();
            }
        });
        panel.add(ModifyButton, gbc);



        // Boutton Supprimer
        gbc.gridx = 2;
        gbc.gridy = 5;
        JButton DeleteButton = new JButton("Supprimer Auteur");
        DeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteAuthor();
            }
        });
        panel.add(DeleteButton, gbc);

        // Configuration de la fenêtre
        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1200, 800);
        setVisible(true);
    }


    
    // Chargement Auteur
    private void LoadAuthorCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CONCAT(nom, ' ', prenom) AS auteur, id_auteur FROM auteur");

            while (rs.next()) {
                AuthorCombo.addItem(rs.getString("auteur"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    
    // Remplir les champs avec les données de l'auteur
    private void DataSelectedAuthor() {
        String selectedAuthor = (String) AuthorCombo.getSelectedItem();
        if (selectedAuthor != null) {
            String[] nomPrenom = selectedAuthor.split(" ");
            String nom = nomPrenom[0];
            String prenom = nomPrenom[1];

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
                PreparedStatement stmt = con.prepareStatement("SELECT id_auteur, date_naissance, description FROM auteur WHERE nom = ? AND prenom = ?");
                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    SelectedAuthorID = rs.getInt("id_auteur"); // Stockage de l'ID de l'auteur sélectionné
                    FieldName.setText(nom);
                    FieldFirstName.setText(prenom);
                    FieldDateOfBirth.setText(rs.getString("date_naissance"));
                    FieldDescription.setText(rs.getString("description"));
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    
    // Ajouter Auteur
    private void AddAuthor() {
        String nom = FieldName.getText();
        String prenom = FieldFirstName.getText();
        String dateNaissance = FieldDateOfBirth.getText();
        String description = FieldDescription.getText();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

            // Vérifier si l'auteur existe déjà
            if (ExistsAuthor(nom, prenom)) {
                JOptionPane.showMessageDialog(this, "Cet auteur existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                con.close();
                return;
            }

            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO auteur (nom, prenom, date_naissance, description) VALUES (?, ?, ?, ?)");
            insertStmt.setString(1, nom);
            insertStmt.setString(2, prenom);
            insertStmt.setString(3, dateNaissance);
            insertStmt.setString(4, description);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Auteur ajouté avec succès.");
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


        
    // Modifier Auteur
    private void ModifyAuthor() {
        String nom = FieldName.getText();
        String prenom = FieldFirstName.getText();
        String dateNaissance = FieldDateOfBirth.getText();
        String description = FieldDescription.getText();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir modifier cet auteur ?", "Modifier Auteur", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

                PreparedStatement updateStmt = con.prepareStatement("UPDATE auteur SET nom = ?, prenom = ?, date_naissance = ?, description = ? WHERE id_auteur = ?");
                updateStmt.setString(1, nom);
                updateStmt.setString(2, prenom);
                updateStmt.setString(3, dateNaissance);
                updateStmt.setString(4, description);
                updateStmt.setInt(5, SelectedAuthorID); // Utilisation de l'ID de l'auteur sélectionné
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Auteur mis à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun auteur trouvé avec ces informations.", "Une erreur est survenue.", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    // Supprimer Auteur
    private void DeleteAuthor() {
        String nom = FieldName.getText();
        String prenom = FieldFirstName.getText();

        if (nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet auteur ?", "Supprimer Auteur", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

                PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM auteur WHERE nom = ? AND prenom = ?");
                deleteStmt.setString(1, nom);
                deleteStmt.setString(2, prenom);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Auteur supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun auteur trouvé avec ces informations.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    
    // Vérification de l'existance de l'auteur
    private boolean ExistsAuthor(String nom, String prenom) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS count FROM auteur WHERE nom = ? AND prenom = ?");
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManageAuthor();
            }
        });
    }
}
