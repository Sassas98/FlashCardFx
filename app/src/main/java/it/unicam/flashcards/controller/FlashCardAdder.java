package it.unicam.flashcards.controller;

import it.unicam.flashcards.model.context.GenericRepository;
import it.unicam.flashcards.model.entity.Card;

public class FlashCardAdder implements AbstractCardAdder {

    private GenericRepository<Card> db;

    public FlashCardAdder(GenericRepository<Card> db){
        this.db = db;
    }

    @Override
    public void addCard(String argument, String details) {
        Card c = new Card(argument, details);
        db.add(c);
    }

}
