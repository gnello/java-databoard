package models;

import interfaces.User;

public class MyUser implements User, Cloneable {
    private final String name;
    private final String password;

    /*
     * Assegna nome e password dello user
     */
    public MyUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /*
     * Assegna il nome dello user e inizializza password a null
     */
    public MyUser(String name) {
        this.name = name;
        this.password = null;
    }

    /*
     * Ritorna true se passw == this.password,
     * false altrimenti
     */
    @Override
    public boolean authenticate(String passw) {
        return this.password.equals(passw);
    }

    /*
     * Ritorna il nome dello user
     */
    @Override
    public String getName() {
        return this.name;
    }

    public MyUser clone() throws CloneNotSupportedException {
        return (MyUser) super.clone();
    }
}
