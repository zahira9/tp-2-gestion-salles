package com.example.jpa.test;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ServiceGestionTest {

    private EntityManagerFactory emf;
    private ServiceGestion service;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("gestionTestPU");
        service = new ServiceGestion(emf);
    }

    @After
    public void tearDown() {
        service.close();
    }

    @Test
    public void testCreationEtRechercheSalle() {
        Salle salle = new Salle("Salle Test", 20);
        service.ajouterSalle(salle);

        List<Salle> salles = service.listerSalles();

        assertFalse(salles.isEmpty());
        assertEquals("Salle Test", salles.get(0).getNom());
        assertNotNull(salles.get(0).getId());
    }

    @Test
    public void testCreationUtilisateurAvecSalle() {
        Salle salle = new Salle("Salle B", 15);
        service.ajouterSalle(salle);
        service.ajouterUtilisateur(
                new Utilisateur("Alice", "alice@example.com", salle)
        );

        List<Utilisateur> utilisateurs = service.listerUtilisateurs();

        assertEquals(1, utilisateurs.size());
        assertEquals("Alice", utilisateurs.get(0).getNom());
        assertEquals("Salle B", utilisateurs.get(0).getSalle().getNom());
    }

    @Test
    public void testRechercheUtilisateursParSalle() {
        Salle salle = new Salle("Salle C", 30);
        service.ajouterSalle(salle);
        service.ajouterUtilisateur(
                new Utilisateur("Bob", "bob@example.com", salle)
        );
        service.ajouterUtilisateur(
                new Utilisateur("Carol", "carol@example.com", salle)
        );

        List<Utilisateur> utilisateurs =
                service.utilisateursParSalle("Salle C");

        assertEquals(2, utilisateurs.size());
    }

    @Test
    public void testRechercheSalleParCapacite() {
        service.ajouterSalle(new Salle("Petite", 8));
        service.ajouterSalle(new Salle("Grande", 40));

        List<Salle> salles = service.sallesAvecCapaciteMinimale(20);

        assertEquals(1, salles.size());
        assertEquals("Grande", salles.get(0).getNom());
    }

    @Test
    public void testSalleSupprimeeParDaoGenerique() {
        Salle salle = new Salle("A supprimer", 10);
        service.ajouterSalle(salle);

        javax.persistence.EntityManager em = emf.createEntityManager();
        try {
            com.example.jpa.dao.SalleDao dao =
                    new com.example.jpa.dao.SalleDao(em);
            assertNotNull(dao.find(salle.getId()));
            assertEquals(true, dao.deleteById(salle.getId()));
            assertNull(dao.find(salle.getId()));
        } finally {
            em.close();
        }
    }
}
