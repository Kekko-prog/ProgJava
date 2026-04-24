package it.unina.prog.dao;

import it.unina.prog.exception.DatabaseException;
import it.unina.prog.model.Veicolo;

import java.util.*;

/**
 * DAO Interface per la gestione dei Veicoli.
 * Responsabile di tutte le operazioni di inventario sui veicoli.
 * L'implementazione concreta è in VeicoloDAOImpl.
 * 
 * Responsabilità:
 * - CRUD completo sui veicoli
 * - Queries per filtrare veicoli disponibili vs. venduti
 * - Gestione dello stato dei veicoli
 */
public interface VeicoloDAO {
    /**
     * Inserisce un nuovo veicolo nel catalogo.
     * @param targa targa unica del veicolo
     * @param marca marca/costruttore
     * @param modello modello specifico
     * @param tipo categoria (es. "Auto", "Moto")
     * @param prezzo prezzo di catalogo
     * @throws DatabaseException se l'inserimento fallisce
     */
    void inserisciVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws DatabaseException;

    /**
     * Recupera lo stato attuale di un veicolo (disponibile, venduto, manutenzione, etc.).
     * @param targa targa del veicolo
     * @return stato del veicolo oppure null se non trovato
     * @throws DatabaseException se la query fallisce
     */
    String getStatoVeicolo(String targa) throws DatabaseException;

    /**
     * Recupera solo i veicoli ancora disponibili per la vendita.
     * @return lista di Veicoli con stato "Disponibile"
     * @throws DatabaseException se la query fallisce
     */
    List<Veicolo> getVeicoliDisponibili() throws DatabaseException;

    /**
     * Aggiorna i dettagli di un veicolo esistente.
     * @param targa targa del veicolo da aggiornare (PK)
     * @param marca nuova marca
     * @param modello nuovo modello
     * @param tipo nuovo tipo
     * @param prezzo nuovo prezzo
     * @throws DatabaseException se l'aggiornamento fallisce
     */
    void aggiornaVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws DatabaseException;

    /**
     * Elimina un veicolo dal catalogo.
     * Solitamente operazione rara, usata solo se il veicolo non è mai stato venduto.
     * @param targa targa del veicolo da eliminare
     * @throws DatabaseException se l'eliminazione fallisce
     */
    void eliminaVeicolo(String targa) throws DatabaseException;

    /**
     * Recupera TUTTI i veicoli nel catalogo, inclusi quelli già venduti.
     * @return lista di tutti i Veicoli
     * @throws DatabaseException se la query fallisce
     */
    List<Veicolo> getAllVeicoli() throws DatabaseException;
}
