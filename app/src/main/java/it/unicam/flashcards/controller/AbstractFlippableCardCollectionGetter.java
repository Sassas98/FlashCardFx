package it.unicam.flashcards.controller;

import java.util.List;

public interface AbstractFlippableCardCollectionGetter extends AbstractCardCollectionGetter {

    /**
     * Flip the card with the id, if it exists
     * @param id the id of the card to flip
     */
    public void flip(Long id);

    /**
     * Getter to the all the cards actually flipped
     * @return all the ids of the flipped cards
     */
    public List<Long> getFlippedCards();
}
