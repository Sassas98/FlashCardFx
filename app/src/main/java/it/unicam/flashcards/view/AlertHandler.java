package it.unicam.flashcards.view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Gestore centralizzato per la visualizzazione delle finestre di dialogo.
 *
 * <p>
 * La classe incapsula la creazione degli {@link Alert}, così da evitare
 * duplicazione di codice nei controller dell'interfaccia grafica.
 * </p>
 */
public class AlertHandler {

    /**
     * Mostra una finestra di errore.
     *
     * <p>
     * Il metodo delega la costruzione effettiva dell'alert a {@link #showAlert}.
     * Non esegue alcuna azione dopo la chiusura della finestra.
     * </p>
     *
     * @param data dati testuali da mostrare nella finestra
     */
    public void showError(String errorTitle, String errorText) {
        showAlert(new AlertData(errorTitle, null, errorText), Alert.AlertType.ERROR, () -> {});
    }

    /**
     * Mostra una finestra di conferma.
     *
     * <p>
     * Se l'utente conferma premendo OK, viene eseguita l'azione ricevuta
     * come parametro.
     * </p>
     *
     * @param data dati testuali da mostrare nella finestra
     * @param confirmAction azione da eseguire in caso di conferma
     */
    public void askConfirm(AlertData data, Runnable confirmAction) {
        showAlert(data, Alert.AlertType.CONFIRMATION, confirmAction);
    }

    /**
     * Crea e mostra una finestra di dialogo generica.
     *
     * <p>
     * Il metodo configura titolo, intestazione e messaggio usando i dati
     * ricevuti. Dopo la chiusura della finestra, esegue l'azione indicata
     * solo se l'utente ha premuto OK.
     * </p>
     *
     * @param data dati testuali da mostrare nell'alert
     * @param type tipo di alert da visualizzare
     * @param confirmAction azione da eseguire se l'utente conferma
     */
    private void showAlert(AlertData data, Alert.AlertType type, Runnable confirmAction) {
        Alert confirm = new Alert(type, data.message());
        confirm.setTitle(data.title());
        confirm.setHeaderText(data.header());

        // showAndWait blocca il flusso finché l'utente non chiude la finestra.
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            confirmAction.run();
        }
    }
}