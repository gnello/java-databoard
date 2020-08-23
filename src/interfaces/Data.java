package interfaces;

import exceptions.DataNotFoundException;
import models.MyData;

import java.util.List;

public interface Data {
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
     * RETURNS: restituisce l'id del dato
     */

    /*
     * Sovrascrive il metodo clone di Object
     */
    public Data clone() throws CloneNotSupportedException;

    /*
     * Restituisce gli user che hanno inserito un like al dato
     */
    public List<User> getLikes() throws CloneNotSupportedException;
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
}
