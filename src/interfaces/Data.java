package interfaces;

import models.MyData;

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
}
