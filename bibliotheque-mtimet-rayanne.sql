-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 21, 2024 at 12:45 PM
-- Server version: 5.7.24
-- PHP Version: 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bibliotheque-mtimet-rayanne`
--

-- --------------------------------------------------------

--
-- Table structure for table `adherent`
--

CREATE TABLE `adherent` (
  `id_adherent` int(4) NOT NULL,
  `nom` varchar(42) DEFAULT NULL,
  `prenom` varchar(42) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `adherent`
--

INSERT INTO `adherent` (`id_adherent`, `nom`, `prenom`, `email`) VALUES
(1, 'MTIMET', 'Rayanne', 'rayannemtimet@ent.fr'),
(2, 'GHARBI', 'Yassine', 'ygharbi@ent.fr'),
(3, 'NEDEL', 'Monsieur', 'nmonsieur@ent.fr'),
(4, 'CALVIAC', 'Julie', 'cjulie@ent.fr'),
(5, 'WEIGER', 'Mathieu', 'wmathieu@ent.fr');

-- --------------------------------------------------------

--
-- Table structure for table `auteur`
--

CREATE TABLE `auteur` (
  `id_auteur` int(4) NOT NULL,
  `nom` varchar(42) DEFAULT NULL,
  `prenom` varchar(42) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `description` varchar(400) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `auteur`
--

INSERT INTO `auteur` (`id_auteur`, `nom`, `prenom`, `date_naissance`, `description`) VALUES
(1, 'Hajime', 'Isayama', '2000-10-10', 'Hajime Isayama, né le 29 août 1986 à Ōyama au Japon, est un mangaka japonais.'),
(2, 'Kōhei', 'Horikoshi', '1990-05-09', 'A écrit My Hero Academia en 2022'),
(3, 'MTIMET', 'Rayanne', '2003-09-05', 'Écrivain et Étudiant en BTS à La Tournelle, bogoss'),
(4, 'FOFANA', 'Abdoulaye', '1999-12-12', 'Écrivain à la ramasse'),
(5, 'ATABONG', 'Maverick', '2004-11-28', 'Bonne écrivain'),
(6, 'Orwell', 'George', '1903-06-25', 'English novelist, essayist, journalist and critic.'),
(7, 'Tolkien', 'J.R.R.', '1892-01-03', 'English writer, poet, philologist, and university professor.'),
(8, 'Murakami', 'Haruki', '1949-01-12', 'Japanese writer.'),
(9, 'Rowling', 'J.K.', '1965-07-31', 'British author, philanthropist, film producer, television producer, and screenwriter.'),
(10, 'Saint-Exupéry', 'Antoine de', '1900-06-29', 'French writer, poet, aristocrat, journalist, and pioneering aviator.');

-- --------------------------------------------------------

--
-- Table structure for table `emprunts`
--

CREATE TABLE `emprunts` (
  `id_emprunt` int(11) NOT NULL,
  `id_adherent` int(11) DEFAULT NULL,
  `id_livre` int(11) DEFAULT NULL,
  `date_emprunt` date DEFAULT NULL,
  `date_retour` date DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `emprunts`
--

INSERT INTO `emprunts` (`id_emprunt`, `id_adherent`, `id_livre`, `date_emprunt`, `date_retour`) VALUES
(25, -1, 9, '2024-04-21', '2024-05-19'),
(26, -1, 2, '2024-04-21', '2024-05-19'),
(27, -1, 2, '2024-04-21', '2024-05-19'),
(28, -1, 9, '2024-04-21', '2024-05-19'),
(29, -1, 7, '2024-04-21', '2024-05-19');

-- --------------------------------------------------------

--
-- Table structure for table `livre`
--

CREATE TABLE `livre` (
  `id_livre` int(4) NOT NULL,
  `titre` varchar(255) DEFAULT NULL,
  `prix` int(4) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `id_auteur` int(4) NOT NULL,
  `disponibilite` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `livre`
--

INSERT INTO `livre` (`id_livre`, `titre`, `prix`, `genre`, `id_auteur`, `disponibilite`) VALUES
(1, 'L\'Attaques des Titans', 15, 'Manga', 1, NULL),
(2, 'Hunter X Hunter', 12, 'Manga', 2, 41),
(3, 'One Piece', 10, 'Anime', 3, 10),
(4, 'Divergente', 18, 'Fiction', 4, 10),
(5, 'Harry Potter', 20, 'Fiction', 4, NULL),
(6, 'Borderland', 14, 'Fiction', 5, NULL),
(7, 'Fortnite', 16, 'Jeux Vidéo', 6, 9),
(8, 'The Hobbit', 18, 'Fiction', 7, 10),
(9, 'Hajime No Ippo', 5, 'Manga', 1, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `adherent`
--
ALTER TABLE `adherent`
  ADD PRIMARY KEY (`id_adherent`);

--
-- Indexes for table `auteur`
--
ALTER TABLE `auteur`
  ADD PRIMARY KEY (`id_auteur`);

--
-- Indexes for table `emprunts`
--
ALTER TABLE `emprunts`
  ADD PRIMARY KEY (`id_emprunt`),
  ADD KEY `id_adherent` (`id_adherent`),
  ADD KEY `fk_id_livre` (`id_livre`);

--
-- Indexes for table `livre`
--
ALTER TABLE `livre`
  ADD PRIMARY KEY (`id_livre`),
  ADD KEY `id_auteur` (`id_auteur`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `adherent`
--
ALTER TABLE `adherent`
  MODIFY `id_adherent` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `auteur`
--
ALTER TABLE `auteur`
  MODIFY `id_auteur` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `emprunts`
--
ALTER TABLE `emprunts`
  MODIFY `id_emprunt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `livre`
--
ALTER TABLE `livre`
  MODIFY `id_livre` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
