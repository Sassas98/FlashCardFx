package it.unicam.flashcards;

import it.unicam.flashcards.model.context.JpaUtil;
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
     * Avvia l'applicazione JavaFX.
     *
     * <p>
     * Il metodo carica la vista principale da FXML
     * e mostra la finestra principale.
     * </p>
     *
     * @param stage finestra principale dell'applicazione
     * @throws Exception se il caricamento della vista o l'avvio falliscono
     */
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = App.class.getResource("/fxml/home.fxml");

        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML non trovato: /fxml/home.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        Scene scene = new Scene(loader.load(), 1050, 700);

        stage.setTitle("FlashCards");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
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