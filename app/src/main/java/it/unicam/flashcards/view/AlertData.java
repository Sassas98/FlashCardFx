package it.unicam.flashcards.view;

/**
 * Contiene i dati testuali necessari per costruire una finestra di dialogo.
 *
 * @param title titolo della finestra
 * @param header intestazione principale del messaggio
 * @param message contenuto descrittivo del messaggio
 */
public record AlertData(String title, String header, String message) { }