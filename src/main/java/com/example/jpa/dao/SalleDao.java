package com.example.jpa.dao;

import com.example.jpa.entities.Salle;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

public class SalleDao extends GenericDao<Salle> {

    public SalleDao(EntityManager em) {
        super(Salle.class, em);
    }

    public List<Salle> findByCapaciteMin(int capaciteMin) {
        if (capaciteMin < 0) {
            throw new IllegalArgumentException("La capacité minimale ne peut pas être négative.");
        }
        return em.createQuery(
                        "SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin",
                        Salle.class
                )
                .setParameter("capaciteMin", capaciteMin)
                .getResultList();
    }

    public Salle findByNom(String nom) {
        Objects.requireNonNull(nom, "nom");
        List<Salle> result = em.createQuery(
                        "SELECT s FROM Salle s WHERE s.nom = :nom",
                        Salle.class
                )
                .setParameter("nom", nom)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
