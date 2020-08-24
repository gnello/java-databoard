package interfaces;

import java.util.List;

public interface Data extends Cloneable, Comparable<Data> {
    /*
     * Stampa il dato come stringa
     */
    public void display();
    /*
     * EFFECTS: stampa una rappresentazione del dato
     */

    /*
     * Restituisce l'id del dato //TODO: serve per equals?
     */
    public Integer getId();
    /*
     * RETURNS: restituisce l'id del dato
     */

    /*
     * Sovrascrive il metodo clone di Object
     */
    public Data clone() throws CloneNotSupportedException;

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

    //TODO: fare specifica
    public void setCategory(String category) throws NullPointerException;
    public String getCategory();
}
