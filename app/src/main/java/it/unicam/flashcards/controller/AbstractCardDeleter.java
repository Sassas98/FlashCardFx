package it.unicam.flashcards.controller;

public interface AbstractCardDeleter {

    /**
     * Delete the card with the id, if it exists
     * @param id id of the card to delete
     */
    public void deleteCard(Long id);

    /**
     * Delete all the cards
     */
    public void deleteAll();
}
