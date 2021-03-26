@echo off
chcp 65001
cls
if not exist webapp\target\webapp-1.2.7.war (
	echo **************************************************************************************
	echo * L'application WEB n'est pas installée !                                            *
	echo * Exécuter la commande 'install' en mode console ou double-cliquer sur 'install.bat' *
	echo * pour installer l'application WEB                                                   *
	echo **************************************************************************************
) else (
	java -jar webapp\target\webapp-1.2.7.war
)