import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageBook extends JFrame {
    private JComboBox<String> BookCombo;
    private JTextField FieldTitle;
    private JTextField FieldPrice;
    private JComboBox<String> AuthorCombo;
    private JTextField FieldDisponibility;
    private String SelectedBookID;

    public ManageBook() {

        // Titre de la fenêtre
        super("AP2 - Gérer les Livres");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);




        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sélectionner un Livre :"), gbc);



        // 
        gbc.gridx = 1;
        gbc.gridy = 0;
        BookCombo = new JComboBox<>();
        LoadBookCombo();
        BookCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DataSelectedBook();
            }
        });
        panel.add(BookCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Titre du Livre :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        FieldTitle = new JTextField(20);
        panel.add(FieldTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prix du Livre :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        FieldPrice = new JTextField(20);
        panel.add(FieldPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Auteur du Livre :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        AuthorCombo = new JComboBox<>();
        LoadAuthorCombo();
        panel.add(AuthorCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Disponibilité :"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        FieldDisponibility = new JTextField(20);
        panel.add(FieldDisponibility, gbc);



        // Ajouter Livre
        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton AddButton = new JButton("Ajouter Livre");
        AddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddBook();
            }
        });
        panel.add(AddButton, gbc);



        // Modifier Livre
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton ModifyButton = new JButton("Modifier Livre");
        ModifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ModifyBook();
            }
        });
        panel.add(ModifyButton, gbc);



        // Supprimer Livre
        gbc.gridx = 2;
        gbc.gridy = 5;
        JButton DeleteButton = new JButton("Supprimer Livre");
        DeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteBook();
            }
        });
        panel.add(DeleteButton, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1200, 800);
        setVisible(true);
    }



    // Permet de récuperer les Auteurs
    private void LoadAuthorCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root"); //Connexion a la bdd
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CONCAT(nom, ' ', prenom) AS auteur FROM auteur");

            while (rs.next()) {
                AuthorCombo.addItem(rs.getString("auteur"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    // Permet de récuperer le Titre des Livres
    private void LoadBookCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root"); //Connexion a la bdd
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT titre FROM livre");

            while (rs.next()) {
                BookCombo.addItem(rs.getString("titre"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    // Récuperer toutes les données sur un Livre choisit
    private void DataSelectedBook() {
        String selectedBook = (String) BookCombo.getSelectedItem();
        if (selectedBook != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");//Connexion a la bdd
                PreparedStatement stmt = con.prepareStatement("SELECT titre, prix, id_auteur, disponibilite, id_livre FROM livre WHERE titre = ?");
                stmt.setString(1, selectedBook);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    FieldTitle.setText(rs.getString("titre"));
                    FieldPrice.setText(rs.getString("prix"));
                    int id_auteur = rs.getInt("id_auteur");
                    AuthorCombo.setSelectedItem(getAuteurName(id_auteur));
                    FieldDisponibility.setText(String.valueOf(rs.getInt("disponibilite")));
                    SelectedBookID = rs.getString("id_livre"); // Charger l'id_livre du livre sélectionné
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Afficher les Auteurs 
    private String getAuteurName(int id_auteur) {
        String auteur = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root"); //Connexion a la bdd
            PreparedStatement stmt = con.prepareStatement("SELECT CONCAT(nom, ' ', prenom) AS auteur FROM auteur WHERE id_auteur = ?");
            stmt.setInt(1, id_auteur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                auteur = rs.getString("auteur");
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return auteur;
    }



    // Methode permettant d'ajouter un Livre
    private void AddBook() {
        String titre = FieldTitle.getText();
        String prix = FieldPrice.getText();
        String auteur = (String) AuthorCombo.getSelectedItem(); // Récupérer l'auteur sélectionné
        int disponibilite = Integer.parseInt(FieldDisponibility.getText());

        if (titre.isEmpty() || prix.isEmpty() || auteur.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (CheckDuplicateBook(titre)) {
            JOptionPane.showMessageDialog(this, "Ce livre existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root"); //Connexion a la bdd

            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO livre (titre, prix, id_auteur, disponibilite) VALUES (?, ?, (SELECT id_auteur FROM auteur WHERE CONCAT(nom, ' ', prenom) = ?), ?)");
            insertStmt.setString(1, titre);
            insertStmt.setString(2, prix);
            insertStmt.setString(3, auteur);
            insertStmt.setInt(4, disponibilite);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Livre ajouté avec succès.");
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Permttant de vérifier si le Livre a Ajouter est existant 
    private boolean CheckDuplicateBook(String titre) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

            PreparedStatement stmt = con.prepareStatement("SELECT titre FROM livre WHERE titre = ?");
            stmt.setString(1, titre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }



    // Permettant de Modfier un Livre
    private void ModifyBook() {
        String titre = FieldTitle.getText();
        String prix = FieldPrice.getText();
        String auteur = (String) AuthorCombo.getSelectedItem();

        int disponibilite = Integer.parseInt(FieldDisponibility.getText());

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

            PreparedStatement updateStmt = con.prepareStatement("UPDATE livre SET titre = ?, prix = ?, id_auteur = (SELECT id_auteur FROM auteur WHERE CONCAT(nom, ' ', prenom) = ?), disponibilite = ? WHERE id_livre = ?");
            updateStmt.setString(1, titre);
            updateStmt.setString(2, prix);
            updateStmt.setString(3, auteur);
            updateStmt.setInt(4, disponibilite);
            updateStmt.setString(5, SelectedBookID); // Utilisation de l'ID_Livre
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Livre mis à jour avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Aucun livre trouvé avec ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Permettant de Modfier un Livre (Si vous supprimez un Livre, -> Tous les emprunts du Livre seront automatiquement supprimer)
    private void DeleteBook() {
        String titre = FieldTitle.getText();

        if (titre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir le champ titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce livre ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

                PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM livre WHERE titre = ?");
                deleteStmt.setString(1, titre);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Livre supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun livre trouvé avec ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManageBook();
            }
        });
    }
}
