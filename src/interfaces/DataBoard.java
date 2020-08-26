package interfaces;

import exceptions.*;

import java.util.Iterator;
import java.util.List;

public interface DataBoard<E extends Data> {
    /*
     * OVERVIEW: una MyDataBoard è un insieme modificabile di oggetti
     * che implementano l'interfaccia Data e ai quali è assegnata una categoria
     *
     * Elemento tipico: {data_1, ..., data_n}
     */

    /*
     * Crea una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void createCategory(String category, String passw) throws UnauthorizedAccessException;
    /*
     * REQUIRES: category != null
     * MODIFIES: this
     * EFFECTS: crea la nuova categoria
     * THROWS: se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category == null solleva una NullPointerException
     *         se category è già presente in this solleva una CategoryAlreadyExistsException
     */

    /*
     * Rimuove una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void removeCategory(String category, String passw) throws UnauthorizedAccessException;
    /*
     * REQUIRES: category != null
     * MODIFIES: this
     * EFFECTS: rimuove la categoria
     * THROWS: se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category == null solleva una NullPointerException
     *         se category non esiste in this solleva una CategoryNotFoundException
     */

    /*
     * Aggiunge un amico ad una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void addFriend(String category, String passw, String friend) throws UnauthorizedAccessException;
    /*
     * REQUIRES: category != null && friend != null
     * MODIFIES: this
     * EFFECTS: aggiunge friend a category
     * THROWS: se category == null || friend == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non esiste in this solleva una CategoryNotFoundException
     *         se friend è già presente in category solleva una FriendAlreadyAddedException
     */

    /*
     * rimuove un amico da una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void removeFriend(String category, String passw, String friend) throws UnauthorizedAccessException,
            UserNotFoundException;
    /*
     * REQUIRES: category != null && friend != null
     * MODIFIES: this
     * EFFECTS: rimuove friend da category
     * THROWS: se category == null || friend == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non esiste in this solleva una CategoryNotFoundException
     *         se friend non è presente in category solleva una UserNotFoundException
     */

    /*
     * Inserisce un dato in bacheca
     * se vengono rispettati i controlli di identità
     */
    public boolean put(String passw, E data, String category) throws UnauthorizedAccessException;
    /*
     * REQUIRES: data != null && category != null
     * MODIFIES: this
     * EFFECTS: inserisce una deep copy di dato in this
     * RETURNS: restituisce true se il dato è stato inserito, false altrimenti
     * THROWS: se data == null || category == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non esiste in this solleva una CategoryNotFoundException
     *         se data è già stato aggiunto in this solleva una DataAlreadyPutException
     */

    /*
     * Restituisce una copia del dato in bacheca
     * se vengono rispettati i controlli di identità
     */
    public E get(String passw, E data) throws UnauthorizedAccessException;
    /*
     * REQUIRES: data != null
     * RETURNS: restituisce una deep copy di dato
     * THROWS: se data == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se data non è presente in this solleva una DataNotFoundException
     */

    /*
     * Rimuove il dato dalla bacheca
     * se vengono rispettati i controlli di identità
     */
    public E remove(String passw, E data) throws UnauthorizedAccessException;
    /*
     * REQUIRES: data != null
     * MODIFIES: this
     * EFFECTS: rimuove il dato da this
     * RETURNS: restituisce una deep copy del dato rimosso
     * THROWS: se data == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se data non è presente in this solleva una DataNotFoundException
     */

    /*
     * Crea la lista dei dati in bacheca di una determinata categoria
     * se vengono rispettati i controlli di identità
     */
    public List<E> getDataCategory(String passw, String category) throws UnauthorizedAccessException;
    /*
     * RETURNS: restituisce la lista dei dati della categoria passata come parametro
     * THROWS: se category == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non è presente solleva una CategoryNotFoundException
     */

    /*
     * Aggiunge un like a un dato
     * se vengono rispettati i controlli di identità
     */
    public void insertLike(String friend, E data) throws UnauthorizedAccessException;
    /*
     * REQUIRES: data != null
     * MODIFIES: this
     * EFFECTS: aggiunge un like di friend a data
     * THROWS: se data == null solleva una NullPointerException
     *         se data non è presente in this solleva una DataNotFoundException
     *         se friend non è autorizzato a leggere data solleva una UnauthorizedAccessException
     *         se friend ha già aggiunto un like solleva una FriendAlreadyAddedException
     */

    /*
     * restituisce un iteratore (senza remove) che genera tutti i dati in
     * bacheca ordinati rispetto al numero di like
     * se vengono rispettati i controlli di identità
     */
    public Iterator<E> getIterator(String passw) throws UnauthorizedAccessException;
    /*
     * RETURNS: restituisce un iteratore (senza remove) che genera tutti i dati presenti
     *          in this ordinati rispetto al numero di like in ordine decrescente
     * THROWS: se passwd non è una password valida solleva una UnauthorizedAccessException
     */

    /*
     * restituisce un iteratore (senza remove) che genera tutti i dati in
     * bacheca condivisi
     */
    public Iterator<E> getFriendIterator(String friend) throws UserNotFoundException;
    /*
     * RETURNS: restituisce un iteratore (senza remove) che genera tutti i dati presenti
     *          in this condivisi con friend
     * THROWS: se friend non è stato aggiunto a nessuna categoria solleva una UserNotFoundException
     */

    // … altre operazione da definire a scelta

    /*
     * Controlla se la categoria esiste nella bacheca
     */
    public boolean hasCategory(String category);
    /*
     * REQUIRES: category != null
     * RETURNS: ritorna true se category esiste in this
     *          false altrimenti
     * THROWS: se category == null solleva una NullPointerException
     */

    /*
     * Controlla se una categoria è leggibile da uno user
     */
    public boolean isReadableBy(String category, User user);
    /*
     * REQUIRES: category != null
     * RETURNS: ritorna true se la categoria è accessibile in
     *          lettura da user, false altrimenti
     * THROWS: se category == null solleva una NullPointerException
     */

    /*
     * Controlla se il dato è già stato inserito in this
     */
    public boolean hasData(E data);
    /*
     * REQUIRES: data != null
     * RETURNS: ritorna true se data è già stato inserito in this
     *          false altrimenti
     * THROWS: se data == null solleva una NullPointerException
     */

    /*
     * Restituisce gli user che hanno inserito un like al dato
     */
    public List<User> getLikes(E data);
    /*
     * REQUIRES: data != null
     * RETURNS: ritorna la lista degli user che hanno inserito un like al dato
     * THROWS: se data == null solleva una NullPointerException
     *         se data non è presente in this solleva una DataNotFoundException
     */
}