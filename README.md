# TP 2 — Gestion des entités Salle et Utilisateur avec JPA et Hibernate

Projet Maven réalisé selon le TP2 : persistance JPA avec Hibernate 5.6, H2 en mémoire, DAO générique et spécifiques, service métier et tests JUnit 4. La gestion des transactions est manuelle avec `EntityTransaction`, sans EJB.

## Prérequis

- JDK 11 ou supérieur
- Maven

## Exécution

```bash
mvn clean compile exec:java -Dexec.mainClass=com.example.jpa.App
```

Le programme crée une salle et deux utilisateurs, puis affiche les listes et les résultats des recherches JPQL.

## Tests

```bash
mvn test
```

Les tests utilisent une unité de persistance H2 isolée avec `create-drop`. La configuration principale `gestionPU` garde `update` comme demandé dans le TP.
