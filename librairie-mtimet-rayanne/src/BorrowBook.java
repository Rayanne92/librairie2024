import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowBook extends JFrame {
    private JComboBox<String> MemberCombo;
    private JComboBox<String> BooksCombo;
    private JLabel BookDataLabel;
    private JButton BorrowButton;
    private Home home;
    
    public BorrowBook(Home home) {

        // Titre de la fenêtre
        super("AP2 - Emprunter un Livre");

        this.home = home;

        JPanel panel = new JPanel();
        JButton backButton = new JButton("Retour à la Bibliothèque");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBackToHome();
            }
        });
        panel.add(backButton);
        add(panel);
    
        setLocationRelativeTo(home);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    private void goBackToHome() {
        home.setVisible(true);
        dispose();
    }
    


    public BorrowBook() {


        super("AP2 - Emprunter un Livre");

        System.out.print("[Terminal] Ouverture de la fenêtre : Emprunter un Livre...");



        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Sélectionner un adhérent :"));
        MemberCombo = new JComboBox<>();
        LoadMemberCombo();
        panel.add(MemberCombo);



        panel.add(new JLabel("Sélectionner un Livre :"));
        BooksCombo = new JComboBox<>();
        LoadBookCombo();
        BooksCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UpdateBookData();
            }
        });
        panel.add(BooksCombo);



        panel.add(new JLabel("Infomrations du Livre :"));
        BookDataLabel = new JLabel();
        panel.add(BookDataLabel);



        // Boutton Emprunter
        BorrowButton = new JButton("Emprunter le Livre");
        BorrowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VoidBorrowBook();
            }
        });
        panel.add(BorrowButton);


        // Configuration de la fenêtre
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1200, 800);

        setVisible(true);
    }



    //
    private void LoadMemberCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CONCAT(nom, ' ', prenom) AS nom_complet FROM adherent");

            while (rs.next()) {
                MemberCombo.addItem(rs.getString("nom_complet"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    //
    private void LoadBookCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT titre FROM livre WHERE disponibilite > 0");
    
            while (rs.next()) {
                BooksCombo.addItem(rs.getString("titre"));
            }
    
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    


    //
    private void UpdateBookData() {
        String selectedLivre = (String) BooksCombo.getSelectedItem();
        if (selectedLivre != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
                PreparedStatement stmt = con.prepareStatement("SELECT titre, prix, id_auteur FROM livre WHERE titre = ?");
                stmt.setString(1, selectedLivre);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String prix = rs.getString("prix");
                    int id_auteur = rs.getInt("id_auteur");

                    String auteur = GetAuthorName(id_auteur);

                    BookDataLabel.setText(" Prix: " + prix + ", Auteur: " + auteur);
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    

    //
    private String GetAuthorName(int id_auteur) {
        String auteur = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT nom, prenom FROM auteur WHERE id_auteur = ?");
            stmt.setInt(1, id_auteur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                auteur = nom + " " + prenom;
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return auteur;
    }



    //
    private void VoidBorrowBook() {
        String selectedAdherent = (String) MemberCombo.getSelectedItem();
        String selectedLivre = (String) BooksCombo.getSelectedItem();

        if (selectedAdherent == null || selectedLivre == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un adhérent et un livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }


        
        //
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");

            // Vérification de la disponibilité du livre
            PreparedStatement checkStmt = con.prepareStatement("SELECT disponibilite FROM livre WHERE titre = ?");
            checkStmt.setString(1, selectedLivre);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int disponibilite = rs.getInt("disponibilite");
                if (disponibilite > 0) {
                    // Mise à jour de la disponibilité
                    PreparedStatement updateStmt = con.prepareStatement("UPDATE livre SET disponibilite = ? WHERE titre = ?");
                    updateStmt.setInt(1, disponibilite - 1);
                    updateStmt.setString(2, selectedLivre);
                    updateStmt.executeUpdate();

                    // Calcul de la date de retour (4 semaines plus tard)
                    LocalDate dateEmprunt = LocalDate.now();
                    LocalDate dateRetour = dateEmprunt.plusWeeks(4);

                    // Insertion de l'emprunt dans la base de données
                    PreparedStatement insertStmt = con.prepareStatement("INSERT INTO emprunts (id_adherent, id_livre, date_emprunt, date_retour) VALUES (?, (SELECT id_livre FROM livre WHERE titre = ?), ?, ?)");
                    insertStmt.setInt(1, GetAuthorID(selectedAdherent));
                    insertStmt.setString(2, selectedLivre);
                    insertStmt.setDate(3, Date.valueOf(dateEmprunt));
                    insertStmt.setDate(4, Date.valueOf(dateRetour));
                    insertStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Livre emprunté avec succès. Date de retour : " + dateRetour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    JOptionPane.showMessageDialog(this, "Le livre sélectionné n'est pas disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'emprunt du livre : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    //
    private int GetAuthorID(String nomComplet) {
        int MemberID = -1;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque-mtimet-rayanne", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT adhnum FROM adherent WHERE CONCAT(nom, ' ', prenom) = ?");
            stmt.setString(1, nomComplet);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MemberID = rs.getInt("adhnum");
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MemberID;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BorrowBook();
            }
        });
    }
}