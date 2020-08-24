package interfaces;

import exceptions.*;

import java.util.Iterator;
import java.util.List;

public interface DataBoard<E extends Data> {
    /*
     * Crea una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void createCategory(String category, String passw) throws UnauthorizedAccessException,
            CategoryAlreadyExistsException;
    /*
     * REQUIRES: category != null
     * MODIFIES: this
     * EFFECTS: crea la nuova categoria
     * THROWS: se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category == null solleva una NullPointerException
     *         se category è già presente in DataBoard solleva una CategoryAlreadyExistsException
     */

    /*
     * Rimuove una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void removeCategory(String category, String passw) throws UnauthorizedAccessException,
            CategoryNotFoundException;
    /*
     * REQUIRES: category != null
     * MODIFIES: this
     * EFFECTS: rimuove la categoria
     * THROWS: se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category == null solleva una NullPointerException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     */

    /*
     * Aggiunge un amico ad una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void addFriend(String category, String passw, String friend) throws NullPointerException,
            UnauthorizedAccessException, CategoryNotFoundException, FriendAlreadyAddedException;
    /*
     * REQUIRES: category != null && friend != null
     * MODIFIES: this
     * EFFECTS: aggiunge friend a category
     * THROWS: se category == null || friend == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     *         se friend è già presente in category solleva una FriendAlreadyAddedException
     */

    /*
     * rimuove un amico da una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void removeFriend(String category, String passw, String friend) throws NullPointerException,
            UnauthorizedAccessException, CategoryNotFoundException, UserNotFoundException;
    /*
     * REQUIRES: category != null && friend != null
     * MODIFIES: this
     * EFFECTS: rimuove friend da category
     * THROWS: se category == null || friend == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     *         se friend non è presente in category solleva una UserNotFoundException
     */

    /*
     * Inserisce un dato in bacheca
     * se vengono rispettati i controlli di identità
     */
    public boolean put(String passw, E dato, String category) throws NullPointerException, UnauthorizedAccessException,
            CategoryNotFoundException, DataAlreadyPutException, CloneNotSupportedException;
    /*
     * REQUIRES: dato != null && category != null
     * MODIFIES: this
     * EFFECTS: inserisce una deep copy di dato in DataBoard
     * RETURNS: restituisce true se il dato è stato inserito, false altrimenti
     * THROWS: se dato == null || category == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     *         se dato è già stato aggiunto in DataBoard solleva una DataAlreadyPutException TODO: specificare category nella sottoclasse?
     *         se non è possibile inserire una deep copy di dato solleva una CloneNotSupportedException
     */

    /*
     * Restituisce una copia del dato in bacheca
     * se vengono rispettati i controlli di identità
     */
    public E get(String passw, E dato) throws NullPointerException, UnauthorizedAccessException,
            DataNotFoundException, CloneNotSupportedException;
    /*
     * REQUIRES: dato != null
     * RETURNS: restituisce una deep copy di dato
     * THROWS: se dato == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se dato non è presente DataBoard solleva una DataNotFoundException TODO: specificare category nella sottoclasse?
     *         se non è possibile restituire una copia del dato solleva una CloneNotSupportedException
     */

    /*
     * Rimuove il dato dalla bacheca
     * se vengono rispettati i controlli di identità
     */
    public E remove(String passw, E dato) throws NullPointerException, UnauthorizedAccessException,
            DataNotFoundException, CloneNotSupportedException;
    /*
     * REQUIRES: dato != null
     * MODIFIES: this
     * EFFECTS: rimuove il dato da DataBoard
     * RETURNS: restituisce una deep copy del dato rimosso
     * THROWS: se dato == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se dato non è presente in DataBoard solleva una DataNotFoundException TODO: specificare category nella sottoclasse?
     *         se non è possibile restituire una deep copy di dato solleva una CloneNotSupportedException
     */

    /*
     * Crea la lista dei dati in bacheca di una determinata categoria
     * se vengono rispettati i controlli di identità
     */
    public List<E> getDataCategory(String passw, String category) throws NullPointerException,
            UnauthorizedAccessException, CategoryNotFoundException, CloneNotSupportedException;
    /*
     * RETURNS: restituisce la lista dei dati presenti in category
     * THROWS: se category == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una UnauthorizedAccessException
     *         se category non è presente solleva una CategoryNotFoundException
     *         se non è possibile restituire una deep copy della lista dei dati presenti
     *         in category solleva una CloneNotSupportedException
     */

    /*
     * Aggiunge un like a un dato
     * se vengono rispettati i controlli di identità
     */
    public void insertLike(String friend, E data) throws NullPointerException, DataNotFoundException,
            UnauthorizedAccessException, FriendAlreadyAddedException;
    /*
     * REQUIRES: data != null
     * MODIFIES: this TODO: sovrascrivere nell'implementazione se modifica l'oggetto data, verificare sulle slide
     * EFFECTS: aggiunge un like di friend a data
     * THROWS: se data == null solleva una NullPointerException
     *         se data non è presente in DataBoard solleva una DataNotFoundException TODO: specificare category nella sottoclasse?
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
     * RETURNS: restituisce un iteratore (senza remove) che genera tutti i dati in
     *          bacheca ordinati rispetto al numero di like in ordine decrescente
     * THROWS: se passwd non è una password valida solleva una UnauthorizedAccessException
     */

    /*
     * restituisce un iteratore (senza remove) che genera tutti i dati in
     * bacheca condivisi
     */
    public Iterator<E> getFriendIterator(String friend) throws UserNotFoundException;
    /*
     * RETURNS: restituisce un iteratore (senza remove) che genera tutti i dati in
     *          bacheca condivisi con friend
     * THROWS: se a friend non è stato aggiunto a nessuna categoria solleva una UserNotFoundException
     */

    // … altre operazione da definire a scelta

    /*
     * Controlla se la categoria esiste in DataBoard
     */
    public boolean hasCategory(String category) throws NullPointerException;
    /*
     * REQUIRES: category != null
     * RETURNS: ritorna true se category esiste in DataBoard
     *          false altrimenti
     * THROWS: se category == null solleva una NullPointerException
     */

    /*
     * Controlla se una categoria è leggibile da uno user
     */
    public boolean isReadableBy(String category, User user) throws NullPointerException;
    /*
     * REQUIRES: category != null
     * RETURNS: ritorna true se la categoria è accessibile in
     *          lettura dallo user, false altrimenti
     * THROWS: se category == null solleva una NullPointerException
     */

    /*
     * Controlla se il dato è già stato inserito in una categoria
     */
    public boolean hasData(E data) throws NullPointerException;
    /*
     * REQUIRES: data != null
     * RETURNS: ritorna true se data è già stato inserito in DataBoard TODO: specificare category nella sottoclasse?
     *          false altrimenti
     * THROWS: se data == null solleva una NullPointerException
     */

    /*
     * Restituisce gli user che hanno inserito un like al dato
     */
    public List<User> getLikes(E data) throws NullPointerException, DataNotFoundException;
    /*
     * REQUIRES: data != null
     * RETURNS: ritorna la lista degli user che hanno inserito un like al dato
     * THROWS: se data == null solleva una NullPointerException
     *         se data non è presente in DataBoard solleva una DataNotFoundException TODO: specificare category nella sottoclasse?
     */
}