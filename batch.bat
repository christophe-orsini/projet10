@echo off
chcp 65001
cls
if not exist batch\target\batch-1.0.6.jar (
	echo **************************************************************************************
	echo * L'application batch n'est pas installée !                                          *
	echo * Exécuter la commande 'install' en mode console ou double-cliquer sur 'install.bat' *
	echo * pour installer l'application                                                       *
	echo **************************************************************************************
) else (
	java -jar batch\target\batch-1.0.6.jar
)