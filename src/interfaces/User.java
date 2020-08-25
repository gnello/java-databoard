package interfaces;

public interface User {
    /*
     * controlla se passw è la password
     * di User
     */
    public boolean authenticate(String passw);
    /*
     * RETURNS: restituisce true se passwd è uguale alla password
     *          di user, false altrimenti
     */

    /*
     * ritorna il nome dello user
     */
    public String getName();

    /*
     * Sovrascrive il metodo clone di Object
     */
    public User clone();
}
