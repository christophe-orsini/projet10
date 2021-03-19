/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

USE `biblio`;

ALTER TABLE `pret`
	ADD COLUMN `date_heure_reservation` datetime DEFAULT NULL
	AFTER `id`;
ALTER TABLE `pret`
	ADD COLUMN `date_heure_expiration` datetime DEFAULT NULL
	AFTER `date_heure_reservation`;
ALTER TABLE `pret`
	ADD COLUMN `email_envoye` tinyint(1) DEFAULT 0
	AFTER `date_heure_expiration`;	
ALTER TABLE `pret`
	MODIFY `date_debut` date DEFAULT NULL;
ALTER TABLE `pret`
	MODIFY `date_fin_prevu` date DEFAULT NULL;

ALTER TABLE `ouvrage`
	ADD COLUMN `nbre_exemplaire_total` int(11) DEFAULT 0
	AFTER `nbre_exemplaire`;	

UPDATE `ouvrage` AS o
SET `nbre_exemplaire_total` = `nbre_exemplaire` +
	(SELECT COUNT(*) AS nb FROM `pret`
     WHERE o.id = pret.ouvrage_id AND (pret.statut = 'EN_COURS' OR pret.statut = 'PROLONGE' OR pret.statut = 'RETARD'));

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;