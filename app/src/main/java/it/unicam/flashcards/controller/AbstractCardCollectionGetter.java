package it.unicam.flashcards.controller;

import java.util.List;

import it.unicam.flashcards.model.entity.Card;

public interface AbstractCardCollectionGetter {

    /**
     * Getter of all the cards into the in-memory deck
     * @return the cards into the deck
     */
    public List<Card> getAll();

    /**
     * Refresh the current in-memory deck to load db changes
     */
    public void refresh();


}
