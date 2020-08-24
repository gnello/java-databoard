package interfaces;

public interface User {
    // ritorna true se passwd è uguale alla password
    // dell'utente, false altrimenti
    public boolean authenticate(String passw);

    /*
     * ritorna il nome dello user
     */
    public String getName();

    /*
     * Sovrascrive il metodo clone di Object
     */
    public User clone();
}
