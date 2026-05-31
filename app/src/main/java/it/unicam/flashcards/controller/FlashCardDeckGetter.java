package it.unicam.flashcards.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unicam.flashcards.model.context.GenericRepository;
import it.unicam.flashcards.model.entity.Card;

public class FlashCardDeckGetter implements AbstractFlippableCardCollectionGetter{

    private GenericRepository<Card> db;
    private Long flipId = 0L;
    private List<Card> cards;

    public FlashCardDeckGetter(GenericRepository<Card> db){
        this.db = db;
        refresh();
    }

    @Override
    public List<Card> getAll() {
        return new ArrayList<>(cards);
    }

    @Override
    public void refresh() {
        cards = db.getAll();
    }

    @Override
    public void flip(Long id) {
        if(cards.stream().anyMatch(x -> x.getId().equals(id)))
            flipId = flipId.equals(id) ? 0L : id;
    }

    @Override
    public List<Long> getFlippedCards() {
        return flipId.equals(0L) ? new ArrayList<>() : Arrays.asList(new Long[]{flipId});
    }

}
