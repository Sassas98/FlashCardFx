package it.unicam.flashcards.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String argument;

    @Column(nullable = false)
    private String details;

    protected Card() { }

    public Card(String argument, String details) {
        if (argument == null || argument.isBlank()
            || details == null || details.isBlank()) {
            throw new IllegalArgumentException("argomento e dettagli di una flash card non possono essere null.");
        }
        this.argument = argument.trim().toUpperCase();
        this.details = details.trim();
    }

    public Long getId() {
        return id;
    }

    public String getArgument() {
        return argument;
    }
    
    public String getDetails(){
        return details;
    }
}