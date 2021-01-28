/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

CREATE DATABASE IF NOT EXISTS `biblio` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `biblio`;

CREATE TABLE IF NOT EXISTS `theme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ouvrage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `annee_edition` int(11) NOT NULL,
  `auteur` varchar(255) NOT NULL,
  `nbre_exemplaire` int(11) DEFAULT NULL,
  `resume` varchar(255) DEFAULT NULL,
  `titre` varchar(255) NOT NULL,
  `theme_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp6qbwb99kgijwvep76svustxe` (`theme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `pret` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_heure_reservation` datetime DEFAULT NULL,
  `date_heure_expiration` datetime DEFAULT NULL,
  `email_envoye` tinyint(1) DEFAULT 0,
  `date_debut` date DEFAULT NULL,
  `date_fin_prevu` date DEFAULT NULL,
  `date_retour` date DEFAULT NULL,
  `periodes` int(11) DEFAULT NULL,
  `prolongations_possible` int(11) DEFAULT NULL,
  `statut` varchar(255) NOT NULL,
  `abonne_id` bigint(20) DEFAULT NULL,
  `ouvrage_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd1kavqk92nftmj6ki820fkmsg` (`abonne_id`),
  KEY `FKryd1x5fme3awtoykchid0ri0y` (`ouvrage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `ouvrage`
  ADD CONSTRAINT `FKp6qbwb99kgijwvep76svustxe` FOREIGN KEY (`theme_id`) REFERENCES `theme` (`id`);

ALTER TABLE `pret`
  ADD CONSTRAINT `FKd1kavqk92nftmj6ki820fkmsg` FOREIGN KEY (`abonne_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `FKryd1x5fme3awtoykchid0ri0y` FOREIGN KEY (`ouvrage_id`) REFERENCES `ouvrage` (`id`);
  
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;