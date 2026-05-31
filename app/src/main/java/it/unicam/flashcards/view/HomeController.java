package it.unicam.flashcards.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller di esempio per una vista con contatore.
 */
public class HomeController {

    /**
     * Label che mostra quante volte il bottone è stato premuto.
     */
    @FXML
    private Label counterLabel;

    /**
     * Numero di click effettuati sul bottone.
     */
    private int counter = 0;

    /**
     * Inizializza lo stato grafico della vista.
     */
    @FXML
    private void initialize() {
        updateCounterLabel();
    }

    /**
     * Gestisce il click sul bottone.
     */
    @FXML
    private void onButtonClicked() {
        counter++;
        updateCounterLabel();
    }

    /**
     * Aggiorna il testo della label in base al valore corrente del contatore.
     */
    private void updateCounterLabel() {
        counterLabel.setText("Premuto " + counter + " volte");
    }
}