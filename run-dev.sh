#!/bin/bash

echo "--- Nettoyage et Construction du Projet ---"
# Construit le projet (télécharge les dépendances via HTTPS)
mvn clean install -DskipTests

echo "--- Lancement du Serveur GWT Dev Mode ---"
# La commande qui fonctionne sur la VM (bindAddress corrige le réseau)
mvn gwt:run -Dgwt.bindAddress=0.0.0.0
