package com.example.jpa;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;

public class App {

    public static void main(String[] args) {
        try (ServiceGestion service = new ServiceGestion()) {
            Salle salle = new Salle("Salle Atlas", 24);
            service.ajouterSalle(salle);

            service.ajouterUtilisateur(
                    new Utilisateur("Alice", "alice@example.com", salle)
            );
            service.ajouterUtilisateur(
                    new Utilisateur("Karim", "karim@example.com", salle)
            );

            System.out.println("Salles : " + service.listerSalles());
            System.out.println("Utilisateurs : " + service.listerUtilisateurs());
            System.out.println(
                    "Utilisateurs de Salle Atlas : "
                            + service.utilisateursParSalle("Salle Atlas")
            );
            System.out.println(
                    "Salles de capacité >= 20 : "
                            + service.sallesAvecCapaciteMinimale(20)
            );
        }
    }
}
