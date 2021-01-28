@echo off
chcp 65001
cls
if not exist apibiblio\target\apibiblio-2.0.0.jar (
	echo **************************************************************************************
	echo * L'API n'est pas installée !                                                        *
	echo * Exécuter la commande 'install' en mode console ou double-cliquer sur 'install.bat' *
	echo * pour installer l'API                                                               *
	echo **************************************************************************************
) else (
	java -jar apibiblio\target\apibiblio-2.0.0.jar
)