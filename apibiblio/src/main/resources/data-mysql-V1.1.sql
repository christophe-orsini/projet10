USE `biblio`;

DELETE FROM `pret`;
DELETE FROM `utilisateur`;
DELETE FROM `ouvrage`;
DELETE FROM `theme`;

INSERT INTO theme (id, nom) VALUES
(1, "Inconnu"),
(2, "Aventure pour tous"),
(3, "Aventure"),
(4, "Roman fantaisie"),
(5, "Enfants"),
(6, "Roman");

INSERT INTO ouvrage (id, auteur, nbre_exemplaire, nbre_exemplaire_total, resume, titre, theme_id, annee_edition) VALUES
(1, "Antoine de Saint-Exupéry", 1, 2, "À la suite d’une panne de moteur, un aviateur se retrouve dans le désert du Sahara. Il rencontre le petit prince qui lui demande de lui dessiner un mouton.", "Le Petit Prince", 2, 1943),
(2, "Herman Melville", 4, 5, "Moby Dick est ce chef-d'oeuvre total que tout le monde peut lire comme le plus formidable des romans d'aventures.", "Moby Dick", 3, 1851),
(3, "J.K. Rowling", 0, 1, "Après la mort de ses parents, Harry Potter est recueilli par sa tante maternelle Pétunia et son oncle Vernon à l'âge d'un an.", "Harry Potter à l'école des sorciers", 4, 1997),
(4, "Charles Perrault", 0, 1, "Un bûcheron et sa femme n'ont plus de quoi nourrir leurs sept garçons.", "Le Petit Poucet", 5, 1697),
(5, "Stendhal", 0, 1, "Fils de charpentier, Julien Sorel est trop sensible et trop ambitieux pour suivre la carrière familiale dans la scierie d’une petite ville de province. ", "Le Rouge et le Noir", 6, 1830);

INSERT INTO utilisateur (id, email, nom, password, prenom, role) VALUES
(1, "admin@biblio.fr", "Administrateur", "$2a$10$eZPZMT/NtMtoJANyhmIIUuRXakpVesT0wNMC4NHLd2r9UF/sVNFxu", NULL, "ROLE_ADMINISTRATEUR"),
(2, "abonne@biblio.fr", "Abonné", "$2a$10$nxSnMr9s6rYmqCefnlWnse3R6FSMT8aYPVIeada9IAam2fNbfoW1a", NULL, "ROLE_ABONNE"),
(3, "batch@biblio.fr", "Traitement Batch", "$2a$10$n4CJ9BhIyr90Qte2uOSJFeBaV/cXbDjrcRv7ryrCM27t462oGrpcm", NULL, "ROLE_BATCH"),
(4, "abonne2@biblio.fr", "Abonné 2", "$2a$10$HiQbf4cxgm48/wry4X7AOeKqxNtJ3CCs5H6ycoubQAnIGhjEKeSkO", NULL, "ROLE_ABONNE");

INSERT INTO pret (id, date_heure_reservation, date_debut, date_fin_prevu, date_retour, statut, abonne_id, ouvrage_id, periodes, prolongations_possible) VALUES
(1, NULL, "2021-02-27", "2021-03-27", NULL, "EN_COURS", 1, 1, 1, 1),
(2, NULL, "2021-01-20", "2021-03-17", NULL, "PROLONGE", 2, 2, 2, 0),
(3, NULL, "2021-02-01", "2021-03-29", NULL, "PROLONGE", 2, 3, 2, 0),
(4, NULL, "2021-02-19", "2021-03-19", NULL, "EN_COURS", 2, 4, 1, 1),
(5, NULL, "2021-02-18", "2021-03-18", NULL, "EN_COURS", 2, 5, 1, 1),
(6, NULL, "2021-02-22", "2021-03-22", NULL, "EN_COURS", 1, 2, 1, 1);

INSERT INTO pret (id, date_heure_reservation, date_heure_expiration, statut, email_envoye,  abonne_id, ouvrage_id, periodes, prolongations_possible) VALUES
(7, "2021-03-09 09:53:00", NULL, "RESERVE", 0, 3, 3, 0, 0),
(8, "2021-03-16 10:21:00", NULL, "RESERVE", 0, 1, 3, 0, 0),
(9, "2021-03-13 14:17:00", "2021-03-18 08:30:00", "DISPONIBLE", 1, 1, 5, 0, 0);


