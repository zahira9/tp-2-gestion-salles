package com.example.jpa.service;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.dao.UtilisateurDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Objects;

public class ServiceGestion implements AutoCloseable {

    private final EntityManagerFactory emf;

    public ServiceGestion() {
        this(Persistence.createEntityManagerFactory("gestionPU"));
    }

    public ServiceGestion(EntityManagerFactory emf) {
        this.emf = Objects.requireNonNull(emf, "emf");
    }

    public void ajouterSalle(Salle salle) {
        withEntityManager(em -> new SalleDao(em).create(salle));
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        withEntityManager(em -> new UtilisateurDao(em).create(utilisateur));
    }

    public List<Salle> listerSalles() {
        return withEntityManagerResult(em -> new SalleDao(em).findAll());
    }

    public List<Utilisateur> listerUtilisateurs() {
        return withEntityManagerResult(em -> new UtilisateurDao(em).findAll());
    }

    public List<Salle> sallesAvecCapaciteMinimale(int capaciteMin) {
        return withEntityManagerResult(
                em -> new SalleDao(em).findByCapaciteMin(capaciteMin)
        );
    }

    public List<Utilisateur> utilisateursParSalle(String nomSalle) {
        return withEntityManagerResult(
                em -> new UtilisateurDao(em).findBySalleNom(nomSalle)
        );
    }

    public void modifierCapaciteSalle(Long id, int capacite) {
        if (capacite < 0) {
            throw new IllegalArgumentException("La capacité ne peut pas être négative.");
        }
        withEntityManager(em -> {
            SalleDao dao = new SalleDao(em);
            Salle salle = dao.find(id);
            if (salle == null) {
                throw new IllegalArgumentException("Salle introuvable : " + id);
            }
            salle.setCapacite(capacite);
            dao.update(salle);
        });
    }

    private void withEntityManager(EntityManagerWork work) {
        withEntityManagerResult(em -> {
            work.accept(em);
            return null;
        });
    }

    private <T> T withEntityManagerResult(EntityManagerFunction<T> work) {
        EntityManager em = emf.createEntityManager();
        try {
            return work.apply(em);
        } finally {
            em.close();
        }
    }

    @Override
    public void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    @FunctionalInterface
    private interface EntityManagerWork {
        void accept(EntityManager em);
    }

    @FunctionalInterface
    private interface EntityManagerFunction<T> {
        T apply(EntityManager em);
    }
}
