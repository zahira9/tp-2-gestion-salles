package com.example.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Objects;

public class GenericDao<T> {

    private final Class<T> entityClass;
    protected final EntityManager em;

    public GenericDao(Class<T> entityClass, EntityManager em) {
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass");
        this.em = Objects.requireNonNull(em, "em");
    }

    public void create(T entity) {
        inTransaction(() -> em.persist(Objects.requireNonNull(entity, "entity")));
    }

    public T find(Object id) {
        return em.find(entityClass, id);
    }

    public T update(T entity) {
        final Object[] result = new Object[1];
        inTransaction(() -> result[0] = em.merge(Objects.requireNonNull(entity, "entity")));
        return entityClass.cast(result[0]);
    }

    public void delete(T entity) {
        inTransaction(() -> {
            T managed = em.contains(entity) ? entity : em.merge(entity);
            em.remove(managed);
        });
    }

    public boolean deleteById(Object id) {
        T entity = find(id);
        if (entity == null) {
            return false;
        }
        delete(entity);
        return true;
    }

    public List<T> findAll() {
        return em.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e",
                entityClass
        ).getResultList();
    }

    private void inTransaction(Runnable operation) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            operation.run();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }
}
