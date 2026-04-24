package it.unina.prog.dao;

import it.unina.prog.exception.DatabaseException;

/**
 * DAO Interface per la gestione delle Vendite.
 * Responsabile di registrare le transazioni di vendita nel database.
 * L'implementazione concreta è in VenditaDAOImpl.
 * 
 * Responsabilità:
 * - Registrazione delle vendite (operazione critica)
 * - Tracciamento di cliente, veicolo, dipendente e prezzo finale
 */
public interface VenditaDAO {
    /**
     * Registra una vendita completata nel database.
     * Questa è un'operazione transazionale critica che lega insieme:
     * - veicolo (identificato da targa)
     * - cliente (chi acquista)
     * - dipendente (chi ha effettuato la vendita)
     * - prezzo finale (può differire da prezzo di catalogo per sconti)
     * 
     * @param targa targa del veicolo venduto
     * @param clienteId id del cliente acquirente
     * @param dipendenteId id del dipendente che ha fatto la vendita
     * @param prezzoFinale prezzo finale della transazione
     * @throws DatabaseException se la registrazione fallisce (es. veicolo già venduto, cliente non esiste, etc.)
     */
    void effettuaVendita(String targa, int clienteId, int dipendenteId, double prezzoFinale) throws DatabaseException;
}
