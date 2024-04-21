import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame {

    public Home() {

        // Titre de la fenêtre
        super("AP2 - Page d'accueil");


        
        // Message afficher dans le terminal
        System.out.print("[Terminal] Ouverture de la fenêtre : Page d'accueil...");



        // Création du panneau principal avec BorderLayout
        JPanel MainPanel = new JPanel(new BorderLayout());
        MainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));



        // Ajout du texte au-dessus des boutons
        JLabel MessageLabel = new JLabel("Bienvenue dans la gestion de la bibliothèque !");
        MessageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        MessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        MainPanel.add(MessageLabel, BorderLayout.NORTH);



        // Création du panneau pour les boutons avec GridLayout
        JPanel ButtonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        ButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));



        // Bouton (Emprunter un Livre)
        JButton ButtonBorrowBook = new JButton("Emprunter un Livre");
        ButtonBorrowBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenWindowBorrowBook();
            }
        });
        ButtonPanel.add(ButtonBorrowBook);



        // Bouton (Gestion des Adhérents)
        JButton ButtonManageMember = new JButton("Gestion des Adhérents");
        ButtonManageMember.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenWindowManageMember();
            }
        });
        ButtonPanel.add(ButtonManageMember);



        // Bouton (Gestion des Auteurs)
        JButton ButtonManageAuthor = new JButton("Gestion des Auteurs");
        ButtonManageAuthor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenWindowManageAuthor();
            }
        });
        ButtonPanel.add(ButtonManageAuthor);



        // Bouton (Gestion des Livres)
        JButton ButtonManageBook = new JButton("Gestion des Livres");
        ButtonManageBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenWindowManageBook();
            }
        });
        ButtonPanel.add(ButtonManageBook);



        // Ajout du panneau des boutons au panneau principal
        MainPanel.add(ButtonPanel, BorderLayout.CENTER);



        // Ajout d'un texte en dessous des boutons
        JLabel footerLabel = new JLabel("© 2024 Bibliothèque - Tous droits réservés | par Rayanne MTIMET");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        MainPanel.add(footerLabel, BorderLayout.SOUTH);



        // Configuration de la fenêtre
        add(MainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1200, 800);
        setVisible(true);
    }



    // Fonction permettant l'ouverture de la fenêtre (Emprunter un Livre)
    private void OpenWindowBorrowBook() {
        new BorrowBook();
    }

    // Fonction permettant l'ouverture de la fenêtre (Gestion des Adhérents)
    private void OpenWindowManageMember() {
        new ManageMember();
    }

    // Fonction permettant l'ouverture de la fenêtre (Gestion des Auteurs)
    private void OpenWindowManageAuthor() {
        new ManageAuthor();
    }

    // Fonction permettant l'ouverture de la fenêtre (Gestion des Livres)
    private void OpenWindowManageBook() {
        new ManageBook();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home());
    }
}
