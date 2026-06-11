package it.unicam.flashcards.view;

import java.util.function.Consumer;

import it.unicam.flashcards.model.entity.Card;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Factory responsabile della costruzione grafica delle flashcard.
 *
 * <p>
 * La classe centralizza la creazione dei nodi JavaFX usati per rappresentare
 * una singola {@link Card}, mantenendo il controller principale più pulito.
 * </p>
 */
public class CardNodeFactory {

    /**
     * Azione da eseguire quando l'utente richiede l'eliminazione di una card.
     */
    private Consumer<Card> deleteAction;

    /**
     * Azione da eseguire quando l'utente clicca una card per girarla.
     */
    private Consumer<Card> flipAction;

    /**
     * Costruisce una factory per i nodi grafici delle card.
     *
     * @param deleteAction azione da eseguire alla pressione del pulsante di eliminazione
     * @param flipAction azione da eseguire al click sulla card
     */
    public CardNodeFactory(Consumer<Card> deleteAction, Consumer<Card> flipAction) {
        this.deleteAction = deleteAction;
        this.flipAction = flipAction;
    }

    /**
     * Crea l'etichetta che indica il tipo di contenuto mostrato dalla card.
     *
     * @param flipped indica se la card sta mostrando i dettagli
     * @param accent colore di accento usato per l'etichetta
     * @return label configurata per indicare "ARGOMENTO" o "DETTAGLI"
     */
    private Label getTypeLable(boolean flipped, String accent) {
        Label typeLabel = new Label(flipped ? "DETTAGLI" : "ARGOMENTO");
        typeLabel.setStyle("""
                -fx-font-size: 11px;
                -fx-font-weight: 800;
                -fx-text-fill: %s;
                -fx-letter-spacing: 1px;
                """.formatted(accent));
        return typeLabel;
    }

    /**
     * Crea l'etichetta principale della card.
     *
     * <p>
     * Se la card è girata mostra i dettagli, altrimenti mostra l'argomento.
     * </p>
     *
     * @param flipped indica se la card sta mostrando i dettagli
     * @param card card da rappresentare
     * @return label contenente il testo principale della card
     */
    private Label getTitleLabel(boolean flipped, Card card) {
        Label titleLabel = new Label(flipped ? card.getDetails() : card.getArgument());
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(230);
        titleLabel.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: 800;
                -fx-text-fill: #111827;
                """);
        return titleLabel;
    }

    /**
     * Crea la barra inferiore della card con il pulsante di eliminazione.
     *
     * @param card card associata al pulsante di eliminazione
     * @return barra inferiore della card
     */
    private HBox getBottomBar(Card card) {
        Button deleteButton = new Button("Elimina");
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setStyle("""
                -fx-background-color: rgba(255,255,255,0.72);
                -fx-text-fill: #991B1B;
                -fx-font-weight: 700;
                -fx-background-radius: 999;
                -fx-padding: 6 12 6 12;
                """);

        deleteButton.setOnAction(event -> {
            // Evita che il click sul pulsante venga interpretato anche come click sulla card.
            event.consume();
            deleteAction.accept(card);
        });

        HBox bottomBar = new HBox(deleteButton);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        return bottomBar;
    }

    /**
     * Costruisce il nodo JavaFX completo che rappresenta una flashcard.
     *
     * @param card card da rappresentare
     * @param bg colore di sfondo della card
     * @param accent colore di accento della card
     * @param flipped indica se la card deve mostrare i dettagli
     * @return nodo grafico della flashcard
     */
    public VBox buildCardNode(Card card, String bg, String accent, boolean flipped) {
        VBox node = new VBox(12,
            getTypeLable(flipped, accent),
            getTitleLabel(flipped, card),
            getBottomBar(card)
        );

        node.setPrefWidth(270);
        node.setMinHeight(190);
        node.setPadding(new Insets(20));
        node.setCursor(Cursor.HAND);
        node.setAlignment(Pos.TOP_LEFT);

        node.setStyle("""
                -fx-background-color: %s;
                -fx-background-radius: 24;
                -fx-border-color: %s;
                -fx-border-width: 0 0 0 6;
                -fx-border-radius: 24;
                -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.13), 18, 0.22, 0, 8);
                """.formatted(bg, accent));

        node.setOnMouseClicked(event -> {
            flipAction.accept(card);
        });

        return node;
    }
}