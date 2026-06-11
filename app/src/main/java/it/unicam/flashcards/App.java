package it.unicam.flashcards;

import it.unicam.flashcards.controller.AbstractCardAdder;
import it.unicam.flashcards.controller.AbstractCardDeleter;
import it.unicam.flashcards.controller.AbstractFlippableCardCollectionGetter;
import it.unicam.flashcards.controller.FlashCardAdder;
import it.unicam.flashcards.controller.FlashCardDeckGetter;
import it.unicam.flashcards.controller.FlashCardDeleter;
import it.unicam.flashcards.model.context.CardRepository;
import it.unicam.flashcards.model.context.JpaUtil;
import it.unicam.flashcards.view.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Classe principale dell'applicazione JavaFX.
 *
 * <p>
 * Si occupa di inizializzare il livello di persistenza, i servizi applicativi,
 * la creazione dei controller FXML e la finestra principale.
 * </p>
 */
public class App extends Application {

    /**
     * Repository usato per accedere ai dati delle flashcard tramite JPA.
     */
    private CardRepository cardRepository;

    /**
     * Servizio responsabile dell'aggiunta di nuove flashcard.
     */
    private AbstractCardAdder cardAdder;

    /**
     * Servizio responsabile del recupero e della gestione del mazzo di flashcard.
     */
    private AbstractFlippableCardCollectionGetter cardGetter;

    /**
     * Servizio responsabile dell'eliminazione delle flashcard.
     */
    private AbstractCardDeleter cardDeleter;

    /**
     * Avvia l'applicazione JavaFX.
     *
     * <p>
     * Il metodo inizializza le dipendenze, carica la vista principale da FXML,
     * configura la factory dei controller e mostra la finestra principale.
     * </p>
     *
     * @param stage finestra principale dell'applicazione
     * @throws Exception se il caricamento della vista o l'avvio falliscono
     */
    @Override
    public void start(Stage stage) throws Exception {
        initializeDependencies();

        URL fxmlUrl = App.class.getResource("/fxml/home.fxml");

        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML non trovato: /fxml/home.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        /*
         * Factory personalizzata per creare i controller FXML.
         * Serve a iniettare manualmente le dipendenze nei controller
         * che non hanno un costruttore vuoto.
         */
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == HomeController.class) {
                return new HomeController(cardGetter, cardAdder, cardDeleter);
            }

            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Impossibile creare il controller: " + controllerClass, e);
            }
        });

        Scene scene = new Scene(loader.load(), 1050, 700);

        stage.setTitle("FlashCards");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Inizializza le dipendenze principali dell'applicazione.
     *
     * <p>
     * Il repository viene creato per primo e poi condiviso tra i servizi
     * che gestiscono le operazioni sulle flashcard.
     * </p>
     */
    private void initializeDependencies() {
        this.cardRepository = new CardRepository();

        this.cardAdder = new FlashCardAdder(cardRepository);
        this.cardGetter = new FlashCardDeckGetter(cardRepository);
        this.cardDeleter = new FlashCardDeleter(cardRepository);
    }

    /**
     * Arresta l'applicazione e rilascia le risorse di persistenza.
     */
    @Override
    public void stop() {
        JpaUtil.close();
    }

    /**
     * Punto di ingresso dell'applicazione.
     *
     * <p>
     * Il livello JPA viene inizializzato prima dell'avvio di JavaFX, così
     * eventuali errori di configurazione della persistenza emergono subito.
     * </p>
     *
     * @param args argomenti da riga di comando
     */
    public static void main(String[] args) {
        // Forza l'inizializzazione di JPA all'avvio.
        JpaUtil.createEntityManager().close();
        launch(args);
    }
}