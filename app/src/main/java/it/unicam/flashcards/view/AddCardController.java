package it.unicam.flashcards.view;

import it.unicam.flashcards.controller.AbstractCardAdder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * Controller della finestra modale per l'aggiunta di una nuova flashcard.
 *
 * <p>
 * Gestisce la validazione dei campi inseriti dall'utente, il salvataggio
 * della flashcard e la chiusura della finestra.
 * </p>
 */
public class AddCardController {

    /**
     * Campo di input per l'argomento della flashcard.
     */
    @FXML
    private TextField argumentTextField;

    /**
     * Area di testo per i dettagli della flashcard.
     */
    @FXML
    private TextArea detailsTextArea;

    /**
     * Servizio responsabile dell'aggiunta della flashcard.
     */
    private final AbstractCardAdder cardAdder;

    /**
     * Gestore delle finestre di errore.
     */
    private final AlertHandler alertHandler;

    /**
     * Costruisce il controller della modale di aggiunta.
     *
     * @param cardAdder servizio usato per salvare una nuova flashcard
     */
    public AddCardController(AbstractCardAdder cardAdder) {
        this.cardAdder = cardAdder;
        this.alertHandler = new AlertHandler();
    }

    /**
     * Metodo chiamato automaticamente da JavaFX dopo il caricamento dell'FXML.
     *
     * <p>
     * Imposta il focus iniziale sul campo dell'argomento.
     * </p>
     */
    @FXML
    private void initialize() {
        argumentTextField.requestFocus();
    }

    /**
     * Gestisce il click sul pulsante di salvataggio.
     *
     * <p>
     * Il metodo valida i campi, aggiunge la flashcard tramite il servizio
     * applicativo e chiude la finestra in caso di successo.
     * </p>
     *
     * @param event evento generato dal click sul pulsante
     */
    @FXML
    private void onSaveClicked(ActionEvent event) {
        String argument = argumentTextField.getText();
        String details = detailsTextArea.getText();

        if (argument == null || argument.isBlank()) {
            alertHandler.showError("Campo mancante",
                     "Inserisci l'argomento della flashcard.");
            return;
        }

        if (details == null || details.isBlank()) {
            alertHandler.showError("Campo mancante",
                     "Inserisci i dettagli della flashcard.");
            return;
        }

        try {
            cardAdder.addCard(argument.trim(), details.trim());
            close(event);

        } catch (Exception e) {
            alertHandler.showError("Errore salvataggio",
                     e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante di annullamento.
     *
     * @param event evento generato dal click sul pulsante
     */
    @FXML
    private void onCancelClicked(ActionEvent event) {
        close(event);
    }

    /**
     * Chiude la finestra modale corrente.
     *
     * @param event evento da cui ricavare la finestra da chiudere
     */
    private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}