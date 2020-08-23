package interfaces;

import exceptions.*;

import java.util.Iterator;
import java.util.List;

public interface DataBoard<E extends Data> {
    /*
     * Crea una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void createCategory(String category, String passw) throws InvalidPasswordException, CategoryAlreadyExistsException;
    /*
     * REQUIRES: category != null
     * MODIFIES: this
     * EFFECTS: crea la nuova categoria se passw è una password valida e se category
     *          non è presente nella DataBoard
     * THROWS: se passwd non è una password valida solleva una InvalidPasswordException
     *         se category == null solleva una NullPointerException
     *         se category è già presente in DataBoard solleva una CategoryAlreadyExistsException
     */

    /*
     * Rimuove una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void removeCategory(String category, String passw) throws InvalidPasswordException, CategoryNotFoundException;
    /*
     * REQUIRES: category != null
     * MODIFIES: this
     * EFFECTS: rimuove la categoria se passw è una password valida e se category
     *          è presente nella DataBoard
     * THROWS: se passwd non è una password valida solleva una InvalidPasswordException
     *         se category == null solleva una NullPointerException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     */

    /*
     * Aggiunge un amico ad una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void addFriend(String category, String passw, String friend) throws NullPointerException,
            InvalidPasswordException, CategoryNotFoundException, FriendAlreadyAddedException;
    /*
     * REQUIRES: category != null && friend != null
     * MODIFIES: this
     * EFFECTS: aggiunge friend a category
     * THROWS: se category == null || friend == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una InvalidPasswordException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     *         se friend è già presente in category solleva una FriendAlreadyAddedException
     */

    /*
     * rimuove un amico da una categoria di dati
     * se vengono rispettati i controlli di identità
     */
    public void removeFriend(String category, String passw, String friend) throws NullPointerException,
            InvalidPasswordException, CategoryNotFoundException, FriendNotFoundException;
    /*
     * REQUIRES: category != null && friend != null
     * MODIFIES: this
     * EFFECTS: rimuove friend da category se passw è una password valida e se category
     *          è presente nella DataBoard
     * THROWS: se category == null || friend == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una InvalidPasswordException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     *         se friend non è presente in category solleva una FriendNotFoundException
     */

    /*
     * Inserisce un dato in bacheca
     * se vengono rispettati i controlli di identità
     */
    public boolean put(String passw, E dato, String category) throws NullPointerException, InvalidPasswordException,
            CategoryNotFoundException, DataAlreadyPutException;
    /*
     * REQUIRES: dato != null && category != null
     * MODIFIES: this
     * EFFECTS: inserisce dato in DataBoard
     * RETURNS: restituisce true se il dato è stato inserito, false altrimenti
     * THROWS: se dato == null || category == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una InvalidPasswordException
     *         se category non esiste in DataBoard solleva una CategoryNotFoundException
     *         se dato è già stato aggiunto a una category qualsiasi solleva una DataAlreadyPutException
     */

    /*
     * Restituisce una copia del dato in bacheca
     * se vengono rispettati i controlli di identità
     */
    public E get(String passw, E dato) throws NullPointerException, InvalidPasswordException,
            DataNotFoundException, CloneNotSupportedException;
    /*
     * REQUIRES: dato != null
     * EFFECTS: ottiene una copia del dato in DataBoard
     * RETURNS: restituisce una deep copy di dato
     * THROWS: se dato == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una InvalidPasswordException
     *         se dato non è presente in nessuna category solleva una DataNotFoundException
     *         se non è possibile restituire una copia del dato solleva una CloneNotSupportedException
     */

    /*
     * Rimuove il dato dalla bacheca
     * se vengono rispettati i controlli di identità
     */
    public E remove(String passw, E dato) throws NullPointerException, InvalidPasswordException,
            DataNotFoundException;
    /*
     * REQUIRES: dato != null
     * MODIFIES: this
     * EFFECTS: rimuove il dato da DataBoard e lo restituisce al chiamante
     * RETURNS: restituisce il dato rimosso
     * THROWS: se dato == null solleva una NullPointerException
     *         se passwd non è una password valida solleva una InvalidPasswordException
     *         se dato non è presente in nessuna category solleva una DataNotFoundException
     */

    //Crea la lista dei dati in bacheca di una determinata categoria
    // se vengono rispettati i controlli di identità
    // Aggiunge un like a un dato
    // se vengono rispettati i controlli di identità
    void insertLike(String friend, E data);

    public List<E> getDataCategory(String passw, String category);

    // restituisce un iteratore (senza remove) che genera tutti i dati in
    // bacheca ordinati rispetto al numero di like
    // se vengono rispettati i controlli di identità
    public Iterator<E> getIterator(String passw);

    // restituisce un iteratore (senza remove) che genera tutti i dati in
    // bacheca condivisi
    public Iterator<E> getFriendIterator(String friend);

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
     * RETURNS: ritorna true se data è già stato inserito in una categoria
     *          false altrimenti
     * THROWS: se data == null solleva una NullPointerException
     */
}