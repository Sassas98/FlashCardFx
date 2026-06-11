package it.unicam.flashcards.view;

import it.unicam.flashcards.controller.AbstractCardAdder;
import it.unicam.flashcards.controller.AbstractCardDeleter;
import it.unicam.flashcards.controller.AbstractFlippableCardCollectionGetter;
import it.unicam.flashcards.model.entity.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URL;
import java.util.List;

/**
 * Controller della vista principale dell'applicazione.
 *
 * <p>
 * Gestisce la visualizzazione delle flashcard, l'apertura della finestra
 * per aggiungere nuove card, l'eliminazione delle card e il refresh dei dati.
 * </p>
 */
public class HomeController {

    /**
     * Contenitore grafico in cui vengono inseriti dinamicamente i nodi delle card.
     */
    @FXML
    private FlowPane cardsPane;

    /**
     * Etichetta mostrata quando non sono presenti flashcard.
     */
    @FXML
    private Label emptyLabel;

    /**
     * Pulsante per aprire la finestra di aggiunta di una nuova flashcard.
     */
    @FXML
    private Button addButton;

    /**
     * Pulsante per eliminare tutte le flashcard.
     */
    @FXML
    private Button deleteAllButton;

    /**
     * Area scrollabile che contiene l'elenco delle flashcard.
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * Servizio per recuperare, aggiornare e gestire lo stato di flip delle card.
     */
    private final AbstractFlippableCardCollectionGetter cardGetter;

    /**
     * Servizio per aggiungere nuove flashcard.
     */
    private final AbstractCardAdder cardAdder;

    /**
     * Servizio per eliminare una o più flashcard.
     */
    private final AbstractCardDeleter cardDeleter;

    /**
     * Gestore centralizzato per la visualizzazione delle finestre di alert.
     */
    private final AlertHandler alertHandler;

    /**
     * Factory responsabile della creazione grafica dei nodi delle card.
     */
    private final CardNodeFactory cardFactory;

    /**
     * Colori di sfondo alternati per rendere le flashcard visivamente distinte.
     */
    private final String[] cardColors = {
            "#EEF2FF", "#ECFDF5", "#FFF7ED", "#FDF2F8",
            "#F0FDFA", "#FEFCE8", "#F5F3FF", "#EFF6FF",
            "#FEE2E2", "#E0F2FE", "#DCFCE7", "#FAE8FF"
    };

    /**
     * Colori di accento associati ai colori di sfondo delle flashcard.
     */
    private final String[] accentColors = {
            "#6366F1", "#10B981", "#F97316", "#EC4899",
            "#14B8A6", "#EAB308", "#8B5CF6", "#3B82F6",
            "#EF4444", "#0284C7", "#22C55E", "#D946EF"
    };

    /**
     * Costruisce il controller principale con i servizi necessari.
     *
     * @param cardGetter servizio per recuperare e gestire le flashcard visualizzate
     * @param cardAdder servizio per aggiungere nuove flashcard
     * @param cardDeleter servizio per eliminare flashcard
     */
    public HomeController(
            AbstractFlippableCardCollectionGetter cardGetter,
            AbstractCardAdder cardAdder,
            AbstractCardDeleter cardDeleter
    ) {
        this.cardGetter = cardGetter;
        this.cardAdder = cardAdder;
        this.cardDeleter = cardDeleter;
        this.alertHandler = new AlertHandler();

        /*
         * La factory riceve due azioni:
         * - eliminare una card;
         * - girare una card e ridisegnare la lista.
         */
        this.cardFactory = new CardNodeFactory(
            (c) -> deleteSingleCard(c),
            (c) -> {
                cardGetter.flip(c.getId());
                renderCards();
            });
    }

    /**
     * Metodo chiamato automaticamente da JavaFX dopo il caricamento dell'FXML.
     *
     * <p>
     * Aggiorna i dati dal database e renderizza la lista iniziale delle card.
     * </p>
     */
    @FXML
    private void initialize() {
        refreshAndRender();
    }

    /**
     * Apre la finestra modale per l'aggiunta di una nuova flashcard.
     *
     * <p>
     * Dopo la chiusura della modale, la vista principale viene aggiornata
     * per mostrare eventuali nuove card inserite.
     * </p>
     */
    @FXML
    private void onOpenAddModalClicked() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/add-card.fxml");

            if (fxmlUrl == null) {
                throw new IllegalStateException("FXML non trovato: /fxml/add-card.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);

            /*
             * Factory usata per iniettare il servizio di aggiunta nel controller
             * della modale, che non viene quindi creato con un costruttore vuoto.
             */
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == AddCardController.class) {
                    return new AddCardController(cardAdder);
                }

                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Impossibile creare il controller: " + controllerClass, e);
                }
            });

            Parent root = loader.load();

            Stage modal = new Stage();
            modal.setTitle("Nuova flashcard");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(addButton.getScene().getWindow());
            modal.setResizable(false);
            modal.setScene(new Scene(root));

            modal.showAndWait();

            // Al ritorno dalla modale, ricarica i dati dal database.
            refreshAndRender();

        } catch (Exception e) {
            this.alertHandler.showError("Errore apertura modale", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'eliminazione di tutte le flashcard.
     *
     * <p>
     * Prima di procedere mostra una finestra di conferma all'utente.
     * </p>
     */
    @FXML
    private void onDeleteAllClicked() {
        try {
            this.alertHandler.askConfirm(
                new AlertData("Conferma eliminazione", "Eliminare tutte le flashcard?",
                    "Questa operazione rimuove tutte le card dal database."),
                () -> {
                    cardDeleter.deleteAll();
                    refreshAndRender();
                }
            );

        } catch (Exception e) {
            this.alertHandler.showError("Errore eliminazione", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aggiorna i dati dal database e ridisegna la lista delle card.
     */
    private void refreshAndRender() {
        try {
            cardGetter.refresh();
            renderCards();

        } catch (Exception e) {
            this.alertHandler.showError("Errore caricamento card", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Renderizza graficamente tutte le flashcard correnti.
     *
     * <p>
     * Il metodo aggiorna anche il contatore, lo stato della label vuota
     * e l'abilitazione del pulsante di eliminazione totale.
     * </p>
     */
    private void renderCards() {
        cardsPane.getChildren().clear();

        List<Card> cards = cardGetter.getAll();
        List<Long> flippedIds = cardGetter.getFlippedCards();

        emptyLabel.setVisible(cards.isEmpty());
        emptyLabel.setManaged(cards.isEmpty());

        deleteAllButton.setDisable(cards.isEmpty());

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            boolean flipped = flippedIds.contains(card.getId());

            VBox cardNode = createCardNode(card, i, flipped);
            cardsPane.getChildren().add(cardNode);
        }
    }

    /**
     * Crea il nodo grafico associato a una singola flashcard.
     *
     * @param card card da rappresentare graficamente
     * @param index indice della card nella lista, usato per scegliere i colori
     * @param flipped indica se la card deve mostrare i dettagli invece dell'argomento
     * @return nodo JavaFX che rappresenta la card
     */
    private VBox createCardNode(Card card, int index, boolean flipped) {
        String bg = cardColors[index % cardColors.length];
        String accent = accentColors[index % accentColors.length];

        return this.cardFactory.buildCardNode(card, bg, accent, flipped);
    }

    /**
     * Elimina una singola flashcard dopo conferma dell'utente.
     *
     * @param card card da eliminare
     */
    private void deleteSingleCard(Card card) {
        try {
            this.alertHandler.askConfirm(
                new AlertData("Conferma eliminazione",
                    "Eliminare questa flashcard?", card.getArgument()
                ),
                () -> {
                    cardDeleter.deleteCard(card.getId());
                    refreshAndRender();
                });

        } catch (Exception e) {
            this.alertHandler.showError("Errore eliminazione card", e.getMessage());
            e.printStackTrace();
        }
    }
}