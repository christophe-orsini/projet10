# BIBLIOTHEQUES DE BOUQUINVILLE

## Project NÂ°7 Gestion du SI des bibliothÃ¨ques de Bouquinville

* DÃ©veloppeur : Christophe ORSINI
* Version 1.0.7

---
### PrÃ©requis
- **Java** version **1.8.0_222**
- **Maven** version **3.6.2**
- **MySQL** version **5.7.21**
 
Le serveur MySQL doit Ãªtre en fonction et les ports 8080 et 8084 doivent Ãªtre libres

### Chargement
Clonez le dÃ©pÃ´t Ã  cette adresse [https://github.com/christophe-orsini/Projet7.git](https://github.com/christophe-orsini/Projet7.git)

### Deploiement, Installation et ExÃ©cution
1. **Mettre le serveur MySQL en fonction**  
L'application est configurÃ©e pour accÃ©der au serveur MySQL avec le login `root` et sans password  
Vous pouvez changer ces infos dans le fichier `<dossier de clonage>/apibiblio/src/main/ressources/application-prod.properties`  
rubriques `spring.datasource.username` et `spring.datasource.password` 
2. **Installer l'application**  
    - Placez vous dans le dossier oÃ¹ vous avez clonÃ© le dÃ©pÃ´t  
    - Tapez la commande `install` si vous Ãªtes en mode console ou cliquez sur `install.bat`
    **N'utilisez pas encore l'application**
3. Importer dans votre serveur MySQL le script `create-database.sql` pour crÃ©er la base de donnÃ©es et les tables
4. Importer dans votre serveur MySQL le script `data-mysql.sql` pour charger les donnÃ©es de dÃ©monstration dans la base
5. DÃ©marrez l'application pour exÃ©cuter les diffÃ©rents modules 
    - Tapez la commande `run` si vous Ãªtes en mode console ou cliquez sur `run.bat` pour dÃ©marrer le serveur  
6. Entrer l'adresse `http://<ip_hote>:8084` (ip_hote = adresse IP ou nom de la machine dans laquelle l'application est installÃ©e) dans votre navigateur WEB prÃ©fÃ©rÃ© pour vous rendre sur le site WEB  

> Vous trouverez les fichiers de script *.sql dans les livrables ou dans le dossier biblio/apibiblio/src/main/ressources

L'application est prÃ¨te Ã  fonctionner avec l'utilisateur :
- `abonne@biblio.fr` mot de passe `abonne` pour le rÃ´le d'utilisateur connectÃ©

### Mise Ã  jour pour la version 1.0.7
Pour mettre Ã  jour l'application, exÃ©cuter les Ã©tapes 2, 5 et 6 ci-dessus  
> L'application doit Ãªtre arrÃªtÃ©e avant la mise Ã  jour

### Nettoyage
Si necessaire, vous pouvez supprimer les donnÃ©es de dÃ©monstration en exÃ©cutant le script `clean-datas.sql` et la base de donnÃ©es sera entiÃ¨rement vidÃ©es.  
Recommencez ensuite Ã  l'Ã©tape 4 ci-dessus.