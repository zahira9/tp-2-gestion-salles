package com.example.jpa.dao;

import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

public class UtilisateurDao extends GenericDao<Utilisateur> {

    public UtilisateurDao(EntityManager em) {
        super(Utilisateur.class, em);
    }

    public List<Utilisateur> findBySalleNom(String nomSalle) {
        Objects.requireNonNull(nomSalle, "nomSalle");
        return em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.salle.nom = :nomSalle",
                        Utilisateur.class
                )
                .setParameter("nomSalle", nomSalle)
                .getResultList();
    }

    public Utilisateur findByEmail(String email) {
        Objects.requireNonNull(email, "email");
        List<Utilisateur> result = em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.email = :email",
                        Utilisateur.class
                )
                .setParameter("email", email)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
