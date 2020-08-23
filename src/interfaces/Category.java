package interfaces;

import exceptions.DataNotFoundException;

import java.util.List;

public interface Category<E extends Data> {
    /*
     * Ritorna il nome della categoria
     */
    public String getName();

    /*
     * Abilita uno user alla lettura
     */
    public void allowRead(User user) throws NullPointerException;
    /*
     * REQUIRES: user != null
     * MODIFIES: this
     * EFFECTS: abilita lo user a leggere i dati della
     *          categoria
     * THROWS: se user == null solleva una NullPointerException
     */

    /*
     * Disabilita uno user alla lettura
     */
    public void denyRead(User user) throws NullPointerException;
    /*
     * REQUIRES: user != null
     * MODIFIES: this
     * EFFECTS: disabilita lo user a leggere i dati della
     *          categoria
     * THROWS: se user == null solleva una NullPointerException
     */

    /*
     * Verifica se uno user può leggere i dati della categoria
     */
    public boolean isReadableBy(User user);
    /*
     * RETURNS: restituisce true se l'utente ha i permessi di lettura
     *          per la categoria, false altrimenti
     */

    /*
     * Aggiunge un dato alla categoria
     */
    public boolean addData(E data) throws NullPointerException;
    /*
     * REQUIRES: data != null
     * MODIFIES: this
     * EFFECTS: aggiunge data alla categoria
     * THROWS: se data == null solleva una NullPointerException
     * RETURNS: restituisce true se data è stato aggiunto alla categoria,
     *          false altrimenti
     */

    /*
     * Controlla il dato esiste nella categoria
     */
    public boolean hasData(E data) throws NullPointerException;
    /*
     * REQUIRES: data != null
     * EFFECTS: controlla se data appartiene alla categoria
     * THROWS: se data == null solleva una NullPointerException
     * RETURNS: restituisce true se data appartiene alla categoria,
     *          false altrimenti
     */

    /*
     * Restituisce una shallow copy del dato se esiste nella categoria
     */
    public E getData(E data) throws NullPointerException, DataNotFoundException;
    /*
     * REQUIRES: data != null
     * EFFECTS: restituisce una shallow copy di data se data appartiene alla categoria
     * THROWS: se data == null solleva una NullPointerException
     *         se data non esiste nella categoria solleva una DataNotFoundException
     * RETURNS: restituisce data se data appartiene alla categoria
     */

    /*
     * Restituisce una shallow copy del dato rimosso
     */
    public E removeData(E data) throws NullPointerException, DataNotFoundException;
    /*
     * REQUIRES: data != null
     * EFFECTS: rimuove data dalla categoria e restituisce una shallow copy di data
     * THROWS: se data == null solleva una NullPointerException
     *         se data non esiste nella categoria solleva una DataNotFoundException
     * RETURNS: restituisce una shallow copy di data
     */

    /*
     * Restituisce la lista dei dati presenti nella categoria
     */
    public List<E> getAllData();
    /*
     * EFFECTS: genera e restituisce una lista di shallow copy dei dati presenti nella categoria
     * RETURNS: restituisce la lista dei dati presenti nella categoria
     */
}
