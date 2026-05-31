package it.unicam.flashcards.controller;

public interface AbstractCardAdder {

    /**
     * Add a new card to the current deck of flash-cards.
     * @param argument argument of the card
     * @param details details of the card
     */
    public void addCard(String argument, String details);

}
