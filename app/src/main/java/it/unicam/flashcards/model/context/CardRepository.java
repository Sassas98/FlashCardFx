package it.unicam.flashcards.model.context;

import java.util.List;
import java.util.function.Consumer;

import it.unicam.flashcards.model.entity.Card;

public class CardRepository extends JpaRepository<Card> {

    @Override
    public void add(Card e) {
        runIntoTransaction((em) ->{
            em.persist(e);
            return true;
        });
    }

    @Override
    public Card get(Long id) {
        return runIntoTransaction((em) -> em.find(Card.class, id));
    }

    @Override
    public List<Card> getAll() {
        return runIntoTransaction((em) -> 
            em.createQuery("SELECT u FROM Card u", Card.class)
                .getResultList());
    }

    @Override
    public void remove(Long id) {
        runIntoTransaction((em) -> {
            Card card = em.find(Card.class, id);
            if (card == null) {
                throw new RuntimeException("Card con id " + id + " non trovata.");
            }
            em.remove(card);
            return true;
        });
    }

    @Override
    public void removeAll() {
        runIntoTransaction((em) -> 
            em.createQuery("DELETE FROM Card")
                .executeUpdate());
    }

    @Override
    public void update(Long id, Consumer<Card> changes) {
        runIntoTransaction((em) -> {
            Card card = em.find(Card.class, id);
            changes.accept(card);
            return true;
        });
    }

}
