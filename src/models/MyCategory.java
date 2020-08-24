package models;

import exceptions.DataNotFoundException;
import interfaces.Category;
import interfaces.Data;
import interfaces.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyCategory<E extends Data> implements Category<E> {
    private final String name;

    private final ArrayList<User> readableByList;
    private final ArrayList<E> dataList;

    // inizializza l'array readableByList e
    // assegna il nome della categoria
    public MyCategory(String name) {
        this.name = name;
        this.readableByList = new ArrayList<>();
        this.dataList = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void allowRead(User user) throws NullPointerException {
        // validazione
        if (user == null) {
            throw new NullPointerException();
        }

        // aggiunge lo user agli user con i permessi di lettura
        this.readableByList.add(user);
    }

    @Override
    public void denyRead(User user) throws NullPointerException {
        // validazione
        if (user == null) {
            throw new NullPointerException();
        }

        // rimuovi lo user dagli user con i permessi di lettura
        this.readableByList.removeIf(el -> el.equals(user));
    }

    @Override
    public boolean isReadableBy(User user) {
        // cerca lo user nella lista dagli user con i permessi di lettura
        for (User item : this.readableByList) {
            // se lo user è presente allora ha i permessi di lettura
            if (item.equals(user)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean addData(E data) {
        // validazione
        if (data == null) {
            throw new NullPointerException();
        }

        // aggiungi data alla lista dei dati della categoria
        return this.dataList.add(data);
    }

    @Override
    public boolean hasData(E data) throws NullPointerException {
        // verifica se il dato è presente nella lista dei dati della categoria
        for (E tmp : this.dataList) {
            if (tmp.equals(data)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public E getData(E data) throws NullPointerException, DataNotFoundException {
        // validazione
        if (data == null) {
            throw new NullPointerException();
        }

        // cerca il dato nella lista dei dati della categoria
        for (E tmp : this.dataList) {

            // se lo trovo lo restituisco
            if (tmp.equals(data)) {
                return tmp;
            }
        }

        // il dato non è stato trovato nella categoria, errore
        throw new DataNotFoundException();
    }

    @Override
    public E removeData(E data) throws NullPointerException, DataNotFoundException {
        // validazione
        if (data == null) {
            throw new NullPointerException();
        }

        // cerca il dato nella lista dei dati della categoria
        for (E item : this.dataList) {

            // se lo trovo lo rimuovo e lo ritorno al chiamante
            if (item.equals(data)) {
                this.dataList.remove(data);

                return item;
            }
        }

        // il dato non è stato trovato nella categoria, errore
        throw new DataNotFoundException();
    }

    @Override
    public List<E> getAllData() {
        return Collections.unmodifiableList(this.dataList); //TODO: va bene così?
    }
}
