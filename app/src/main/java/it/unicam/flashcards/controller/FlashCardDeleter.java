package it.unicam.flashcards.controller;

import it.unicam.flashcards.model.context.GenericRepository;
import it.unicam.flashcards.model.entity.Card;

public class FlashCardDeleter implements AbstractCardDeleter {

    private GenericRepository<Card> db;

    public FlashCardDeleter(GenericRepository<Card> db){
        this.db = db;
    }

    @Override
    public void deleteCard(Long id) {
        db.remove(id);
    }

    @Override
    public void deleteAll() {
        db.removeAll();
    }

}
