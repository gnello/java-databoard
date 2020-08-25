package interfaces;

import java.util.List;

public interface Data extends Cloneable, Comparable<Data> {
    /*
     * OVERVIEW: un oggetto di tipo Data è una quadrupla di elementi
     * che consistono in un id, un corpo, una categoria ed un elenco
     * di user che hanno inserito un like
     *
     * Elemento tipico: <id, body, category, {user_1, ..., user_n}>
     */

    /*
     * Stampa il dato come stringa
     */
    public void display();
    /*
     * EFFECTS: stampa una rappresentazione del dato
     */

    /*
     * Restituisce l'id del dato
     */
    public Integer getId();
    /*
     * RETURNS: restituisce l'id di this
     */

    /*
     * Sovrascrive il metodo clone di Object
     */
    public Data clone();

    /*
     * Restituisce gli user che hanno inserito un like al dato
     */
    public List<User> getLikes();
    /*
     * RETURNS: ritorna una lista degli user che hanno inserito un like al dato
     */

    /*
     * Aggiunge un like di friend
     */
    public void insertLike(String friend) throws NullPointerException;
    /*
     * REQUIRES: friend != null
     * MODIFIES: this
     * EFFECTS: aggiunge un like di friend
     * THROWS: se data == null solleva una NullPointerException
     */

    /*
     * Assegna una categoria
     */
    public void setCategory(String category) throws NullPointerException;
    /*
     * REQUIRES: category != null
     * MODIFIES: this
     * EFFECTS: assegna una categoria a this
     * THROWS: se category == null solleva una NullPointerException
     */

    /*
     * Restituisce la categoria a cui appartiene il dato
     */
    public String getCategory();
    /*
     * RETURNS: restituisce la categoria di this
     */
}
