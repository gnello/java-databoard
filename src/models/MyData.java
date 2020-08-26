package models;

import interfaces.Data;
import interfaces.User;

import java.util.ArrayList;
import java.util.List;

public class MyData implements Data {
    /*
     * Id del dato
     */
    private final Integer id;

    /*
     * categoria a cui appartiene il dato
     */
    private String category;

    /*
     * Il body del dato
     */
    private final String body;

    /*
     * La lista degli user che hanno inserito
     * un like per questo dato
     */
    private final ArrayList<User> likedByList;

    /*
     * inizializza this
     */
    public MyData(Integer id, String body) {
        this.id = id;

        this.category = null;
        this.body = body;

        this.likedByList = new ArrayList<>();
    }
    /*
     * EFFECTS: inizializza this con id e body passati
     *          al costruttore, category null e
     *          likedByList array vuoto
     */

    @Override
    public void display() {
        System.out.println(this.body);
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    /*
     * restituisce una copia del dato
     */
    public MyData clone() {
        try {
            // clona
            return (MyData) super.clone();
        } catch (CloneNotSupportedException e) {
            // se il clone non ha successo,
            // effettua una copia "a mano"
            MyData cloneData = new MyData(this.getId(), this.body);

            cloneData.setCategory(this.getCategory());

            for (User item : this.getLikes()) {
                cloneData.insertLike(item.getName());
            }

            return cloneData;
        }
    }
    /*
     * RETURNS: restituisce una deep copy di this
     */

    @Override
    public List<User> getLikes() {
        // crea una nuova lista che conterrà
        // le copie degli elementi user
        ArrayList<User> likesList = new ArrayList<>();

        // esegui una deep copy degli elementi
        for (User item : this.likedByList) {
            likesList.add(item.clone());
        }

        return likesList;
    }

    @Override
    public void insertLike(String friend) {
        // validazione
        if (friend == null) {
            throw new NullPointerException();
        }

        // inserisci friend nella lista degli utenti
        // che hanno inserito un like per questo dato
        this.likedByList.add(new MyUser(friend));
    }

    @Override
    public void setCategory(String category) {
        // validazione
        if (category == null) {
            throw new NullPointerException();
        }

        this.category = category;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    /*
     * Verifica se il dato è uguale all'oggetto
     * passato come argomento
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MyData data = (MyData) o;

        // effettua il controllo su id e body
        return this.getId().equals(data.getId())
                && this.body.equals(data.body);
    }
    /*
     * RETURNS: restituisce true se o è una copia valida di this,
     *          ovvero se id e body combaciano
     */

    /*
     * Sovrascrive hashCode() per non violare il general contract
     * di Object.hashCode(), ad esempio se due oggetti sono uguali
     * secondo il metodo equals, anche i loro hashCode devono esserlo
     */
    public int hashCode() {
        // il campo id è anche usato per
        // la comparazione in equals()
        // quindi è perfetto per non violare
        // il general contract di Object.hashCode()
        return this.id.hashCode();
    }
    /*
     * RETURNS: restituisce un hashCode di this
     */

    /*
     * Utilizza l'id per determinare se un dato
     * è più grande di un altro
     */
    public int compareTo(Data data) {
        return data.getId() - this.getId();
    }
    /*
     * RETURNS: ritorna un numero positivo se this è più piccolo di data,
     *          un numero negativo se this è più grande di data,
     *          zero se this e data sono uguali
     */
}
