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

    @Override
    public MyUser clone() {
        return new MyUser(this.name, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MyUser user = (MyUser) o;

        return this.getName().equals(user.getName());
    }

    @Override
    /*
     * Sovrascrivi per non violare il general contract di Object.hashCode(),
     */
    public int hashCode() {
        return this.getName().hashCode();
    }
}
