import exceptions.*;
import interfaces.Category;
import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyCategory;
import models.MyUser;

import java.util.*;

public class MyDataBoardArrayList<E extends Data> implements DataBoard<E> {
    // lo user proprietario della bacheca
    private final User owner;

    private final ArrayList<Category<E>> categories;

    /*
     * AF: α(c) = { c.categories.get(i).getAllData().get(k) |
     *                  0 <= k < c.categories.get(i).getAllData().size()
     *                  && 0 <= i < c.categories.size() }
     *
     * IR: I(c) = c.categories != null
     *              e per ogni i tale che 0 <= i < c.categories.size(), c.categories.get(i).getAllData() != null
     *              e per ogni i, k tale che 0 <= i < c.categories.size()
     *                  && 0 <= k < c.categories.get(i).getAllData().size(),
     *                  c.categories.get(i).getAllData().get(k) implementa l'interfaccia Data
     *              e per tutti gli i, j, k tali che 0 <= i < c.categories.size()
     *                  && 0 <= j < k < c.categories.get(i).getAllData().size(),
     *                  c.categories.get(i).getAllData().get(j).equals(c.categories.get(i).getAllData().get(k)) == false
     */

    // inizializza l'array categories e
    // assegna l'owner della bacheca
    public MyDataBoardArrayList(User owner) {
        this.owner = owner;
        this.categories = new ArrayList<>();
    }

    @Override
    public void createCategory(String category, String passw) throws UnauthorizedAccessException,
            CategoryAlreadyExistsException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        if (category == null) {
            throw new NullPointerException();
        }

        if (this.hasCategory(category)) {
            throw new CategoryAlreadyExistsException();
        }

        // aggiungi la categoria
        this.categories.add(new MyCategory<>(category));
    }

    @Override
    public void removeCategory(String category, String passw) throws UnauthorizedAccessException,
            CategoryNotFoundException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        if (category == null) {
            throw new NullPointerException();
        }

        if (!this.hasCategory(category)) {
            throw new CategoryNotFoundException();
        }

        // rimuovi la categoria
        this.categories.removeIf(el -> el.getName().equals(category));
    }

    @Override
    public void addFriend(String category, String passw, String friend) throws NullPointerException,
            UnauthorizedAccessException, CategoryNotFoundException, FriendAlreadyAddedException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        if (category == null || friend == null) {
            throw new NullPointerException();
        }

        // fail fast, don't wait the end of the for loop
        if (!this.hasCategory(category)) {
            throw new CategoryNotFoundException();
        }

        // trova la categoria
        for (Category<E> tmp : this.categories) {
            if (tmp.getName().equals(category)) {
                User friendUser = new MyUser(friend);

                // verifica che friend non sia già
                // stato aggiunto alla categoria
                if (tmp.isReadableBy(friendUser)) {
                    throw new FriendAlreadyAddedException();
                }

                // aggiungi friend alla categoria
                tmp.allowRead(friendUser);

                return;
            }
        }
    }

    @Override
    public void removeFriend(String category, String passw, String friend) throws NullPointerException,
            UnauthorizedAccessException, CategoryNotFoundException, UserNotFoundException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        if (category == null || friend == null) {
            throw new NullPointerException();
        }

        if (!this.hasCategory(category)) {
            throw new CategoryNotFoundException();
        }

        // trova la categoria
        for (Category<E> tmp : this.categories) {
            if (tmp.getName().equals(category)) { //TODO: ridefinire equals?
                User friendUser = new MyUser(friend);

                // se friend è presente nella
                // categoria rimuovilo
                if (tmp.isReadableBy(friendUser)) {
                    tmp.denyRead(friendUser);

                    return;
                }

                break;
            }
        }

        // friend non è presente nella
        // categoria, errore
        throw new UserNotFoundException();
    }

    @Override
    public boolean put(String passw, E dato, String category) throws NullPointerException, UnauthorizedAccessException,
            CategoryNotFoundException, DataAlreadyPutException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        // fail fast, non aspettare il NullPointerException di tmp.addData(dato)
        if (dato == null || category == null) {
            throw new NullPointerException();
        }

        if (!this.hasCategory(category)) {
            throw new CategoryNotFoundException();
        }

        if (this.hasData(dato)) {
            throw new DataAlreadyPutException();
        }

        // trova la categoria passata come parametro
        for (Category<E> tmp : this.categories) {
            if (tmp.getName().equals(category)) {
                // aggiungi una deep copy di dato alla categoria
                return tmp.addData((E)dato.clone());
            }
        }

        return false;
    }

    @Override
    public E get(String passw, E dato) throws NullPointerException, UnauthorizedAccessException,
            DataNotFoundException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        // fail fast, non aspettare il NullPointerException di dato.clone()
        if (dato == null) {
            throw new NullPointerException();
        }

        // cerca il dato nelle categorie
        for (Category<E> tmp : this.categories) {
            if (tmp.hasData(dato)) {
                // restituisci una deep copy del dato
                return (E)tmp.getData(dato).clone();
            }
        }

        throw new DataNotFoundException();
    }

    @Override
    public E remove(String passw, E dato) throws NullPointerException, UnauthorizedAccessException,
            DataNotFoundException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        // fail fast, non aspettare il NullPointerException di tmp.removeData(dato)
        if (dato == null) {
            throw new NullPointerException();
        }

        // cerca il dato nelle categorie
        for (Category<E> tmp : this.categories) {
            if (tmp.hasData(dato)) {
                // rimuovi il dato e restituisci una deep copy
                return (E)tmp.removeData(dato).clone();
            }
        }

        throw new DataNotFoundException();
    }

    @Override
    public List<E> getDataCategory(String passw, String category) throws NullPointerException,
            UnauthorizedAccessException, CategoryNotFoundException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        if (category == null) {
            throw new NullPointerException();
        }

        // trova la categoria passata come parametro
        for (Category<E> tmp : this.categories) {
            if (tmp.getName().equals(category)) {
                // ritorna una deep copy della lista di dati della categoria
                ArrayList<E> dataList = new ArrayList<>();

                for (E item : tmp.getAllData()) {
                    dataList.add((E)item.clone());
                }

                return dataList;
            }
        }

        throw new CategoryNotFoundException();
    }

    @Override
    public void insertLike(String friend, E data) throws NullPointerException, DataNotFoundException,
            UnauthorizedAccessException, FriendAlreadyAddedException {
        // validazione
        if (data == null) {
            throw new NullPointerException();
        }

        // trova il dato
        for (Category<E> item : this.categories) {
            if (item.hasData(data)) {
                // controlla se friend può leggere data
                if (!item.isReadableBy(new MyUser(friend))) {
                    throw new UnauthorizedAccessException();
                }

                E dataItem = item.getData(data);

                // controlla se friend ha già inserito un like
                List<User> likesList = dataItem.getLikes();

                for (User user : likesList) {
                    if (user.getName().equals(friend)) {
                        throw new FriendAlreadyAddedException();
                    }
                }

                // inserisci il like
                dataItem.insertLike(friend);

                return;
            }
        }

        // il dato non è stato trovato, errore
        throw new DataNotFoundException();
    }

    @Override
    public Iterator<E> getIterator(String passw) throws UnauthorizedAccessException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        // mergia tutti i dati delle category
        List<E> dataList = new ArrayList<>();

        for (Category<E> item : this.categories) {
            dataList.addAll(item.getAllData());
        }

        // ordina decrescente
        dataList.sort(Comparator.comparingInt(o -> -(o.getLikes().size())));

        // disabilita il metodo remove dell'iteratore che verrà generato
        // sfruttando il metodo Collections.unmodifiableList
        dataList = Collections.unmodifiableList(dataList);

        // ritorna l'iteratore
        return dataList.iterator();
    }

    @Override
    public Iterator<E> getFriendIterator(String friend) throws UserNotFoundException {
        List<E> dataList = new ArrayList<>();

        boolean friendFound = false;

        // cerca friend nelle categorie
        User friendUser = new MyUser(friend);

        for (Category<E> item : this.categories) {

            // se lo trovo restituisci un iteratore
            // con i dati della categoria
            if (item.isReadableBy(friendUser)) {
                friendFound = true;

                dataList.addAll(item.getAllData());
            }
        }

        if (friendFound) {
            // disabilita il metodo remove dell'iteratore che verrà generato
            // sfruttando il metodo Collections.unmodifiableList
            dataList = Collections.unmodifiableList(dataList);

            return dataList.iterator();
        }

        // lo user non ha dati leggibili, errore
        throw new UserNotFoundException();
    }

    @Override
    public boolean hasCategory(String category) throws NullPointerException {
        //validazione
        if (category == null) {
            throw new NullPointerException();
        }

        // cerca la categoria, se la trova ritorna true
        for (Category<E> tmp : this.categories) {
            if (tmp.getName().equals(category)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isReadableBy(String category, User user) throws NullPointerException {
        // trova la categoria
        for (Category<E> item : this.categories) {
            if (item.getName().equals(category)) {
                // sfrutta il metodo isReadableBy dell'interfaccia Category
                // per determinare se lo user può leggere i dati della categoria
                return item.isReadableBy(user);
            }
        }

        return false;
    }

    @Override
    public boolean hasData(E data) throws NullPointerException {
        // controlla in ogni categoria se è presente il dato
        // passato come parametro sfruttando il metodo hasData
        // dell'interfaccia Category
        for (Category<E> tmp : this.categories) {
            if (tmp.hasData(data)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<User> getLikes(E data) throws NullPointerException, DataNotFoundException {
        //validazione
        if (data == null) {
            throw new NullPointerException();
        }

        // trova il dato
        for (Category<E> tmp : this.categories) {
            if (tmp.hasData(data)) {
                // ritorna la lista degli user che
                // hanno inserito un like al dato
                return tmp.getData(data).getLikes();
            }
        }

        // il dato non è stato trovato, errore
        throw new DataNotFoundException();
    }
}
