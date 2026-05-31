package it.unicam.flashcards.model.context;

import java.util.function.Function;

import it.unicam.flashcards.model.entity.IdentifiableEntity;
import jakarta.persistence.EntityManager;

public abstract class JpaRepository<T extends IdentifiableEntity> implements GenericRepository<T> {
    protected <R> R runIntoTransaction(Function<EntityManager, R> function){
        EntityManager em = JpaUtil.createEntityManager();

        try {
            em.getTransaction().begin();
            R result = function.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}
