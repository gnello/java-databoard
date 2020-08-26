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
    public void allowRead(User user);
    /*
     * REQUIRES: user != null
     * MODIFIES: this
     * EFFECTS: vengono aggiunti i permessi di lettura di user
     * THROWS: se user == null solleva una NullPointerException
     */

    /*
     * Disabilita uno user alla lettura
     */
    public void denyRead(User user);
    /*
     * REQUIRES: user != null
     * MODIFIES: this
     * EFFECTS: vengono rimossi i permessi di lettura di user
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
    public boolean addData(E data);
    /*
     * REQUIRES: data != null
     * MODIFIES: this
     * EFFECTS: data è aggiunto a this
     * THROWS: se data == null solleva una NullPointerException
     * RETURNS: restituisce true se data è stato aggiunto alla categoria,
     *          false altrimenti
     */

    /*
     * Controlla il dato esiste nella categoria
     */
    public boolean hasData(E data);
    /*
     * REQUIRES: data != null
     * THROWS: se data == null solleva una NullPointerException
     * RETURNS: restituisce true se data appartiene alla categoria,
     *          false altrimenti
     */

    /*
     * Restituisce una shallow copy del dato se esiste nella categoria
     */
    public E getData(E data);
    /*
     * REQUIRES: data != null
     * THROWS: se data == null solleva una NullPointerException
     *         se data non esiste nella categoria solleva una DataNotFoundException
     * RETURNS: restituisce una shallow copy di data
     */

    /*
     * Rimuove il dato dalla categoria
     */
    public E removeData(E data);
    /*
     * REQUIRES: data != null
     * EFFECTS: data viene rimosso da this
     * THROWS: se data == null solleva una NullPointerException
     *         se data non esiste nella categoria solleva una DataNotFoundException
     * RETURNS: restituisce una shallow copy di data
     */

    /*
     * Restituisce la lista dei dati presenti nella categoria
     */
    public List<E> getAllData();
    /*
     * RETURNS: restituisce una lista di shallow copy dei dati presenti nella categoria
     */
}
