# MedilaboSolutions

Medilabo est une application médicale. Elle permet aux utilisateurs (médecins) 
d’ajouter des patients et de mettre à jour leurs informations personnelles. 
En tant que médecin, il est également possible d’ajouter des notes médicales pour chaque 
patient et de les consulter. En fonction du sexe du patient, de son âge et du nombre de termes 
déclencheurs présents dans les notes médicales, une estimation du risque de développer 
le diabète est calculée et affichée sur la page d’informations du patient.

## Table des matières

- [Technologies](#technologies)

- [Architecture de l'application](#architecture-de-lapplication)

- [Le Green Code](#le-green-code)

- [Configuration](#configuration)

- [Installation et exécution](#installation-et-exécution)

## Technologies

* Java 21
* Spring Boot
* Spring Security
* Thymeleaf
* Bootstrap
* Mysql 8.0.42
* MongoDb 
* Docker

## Architecture de l'application

![img.png](img.png)

## Le Green Code

1. Optimiser la gestion des notes et des patients

* Stocker les résultats de calcul plutôt que de recalculer à chaque requête.

* Supprimer ou mettre à jour uniquement les informations nécessaires.

* Réécrire les algorithmes en versions plus légères et performantes.

* Réduire la duplication et simplifier les fonctions complexes.

2. Optimiser le calcul des risques

* Stocker le niveau de risque d’un patient au lieu de recalculer systématiquement toutes les données à chaque consultation.

* Recalculer uniquement lorsque de nouvelles informations ou notes sont ajoutées.

3. Limiter les données affichées

* Ne pas afficher toutes les informations d’un patient sur une seule page, afin de réduire le transfert et le rendu de données inutiles.

4. Optimisation générale

* Mettre en place un microservice de registre et de découverte (Eureka) pour optimiser la communication entre les services.

## Configuration
Avant de lancer l'application, il est nécessaire de configurer la connexion à la base de données de microsevice patient
dans le fichier application.yml, situé à l'emplacement suivant :

patient-service/src/main/resources/application.yml

1. Modifier les informations de connexion
Remplace les lignes suivantes selon vos informations personnelles de connexion à MySQL :

spring
    datasource
        username:
        password:
2. Lance la commande suivante dans MySQL pour créer la base de données patient_P9_db :

    CREATE DATABASE patient_P9_db;

3. Modifier le mode de gestion du schéma de base de données pour vous pouvez créer les données
Dans un premier temps, remplacez la ligne suivante :

    jpa:
        hibernate:
            ddl-auto: update
            init:
                mode: never

    par :
    jpa:
        hibernate:
            ddl-auto: create
            init:
                mode: always

    Cela permettra à Hibernate de créer automatiquement toutes les tables à partir des entités Java.

🔁 Ensuite, une fois les tables créées et l'application correctement démarrée, remplacez de nouveau la ligne par :

    jpa:
        hibernate:
            ddl-auto: update
            init:
                mode: never


Ce mode permet de mettre à jour le schéma sans effacer les données déjà présentes.

## Installation et exécution

1. Cloner le projet
   git clone <https://github.com/hadhoudda/MediLaboSolutions>
   cd MediLaboSolutions

2. Compiler le projet
   mvn clean install
3. Lance les tests
   mvn clean test
4. Lance l'application avec docker :
   docker-compose up --build -d
5. Pour accéder à l’application après l’avoir lancée, utilisez l’utilisateur de test suivant:
    username: user
    mot de passe:1234 