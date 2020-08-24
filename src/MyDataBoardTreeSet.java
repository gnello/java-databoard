import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyUser;

import java.util.*;

public class MyDataBoardTreeSet<E extends Data> implements DataBoard<E> {
    // lo user proprietario della bacheca
    private final User owner;

    private final TreeSet<E> dataList;
    private final HashMap<String, ArrayList<User>> categories;

    /*
     * inizializza this e
     * assegna l'owner della bacheca
     */
    public MyDataBoardTreeSet(User owner) {
        this.owner = owner;
        this.dataList = new TreeSet<>();
        this.categories = new HashMap<>();
    }
    /*
     * EFFECTS: inizializza this a tree set vuoto
     * e inizializza l'owner con lo user passato come argomento
     */

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

        this.categories.put(category, new ArrayList<>());
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

        // rimuovi prima i dati della categoria
        this.emptyData(category);

        // rimuovi la categoria
        this.categories.remove(category);
    }

    private void emptyData(String category) {
        this.dataList.removeIf(item -> item.getCategory().equals(category));
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

        if (!this.hasCategory(category)) {
            throw new CategoryNotFoundException();
        }

        if (this.isReadableBy(category, new MyUser(friend))) {
            throw new FriendAlreadyAddedException();
        }

        this.categories.get(category).add(new MyUser(friend));
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

        if (!this.isReadableBy(category, new MyUser(friend))) {
            throw new UserNotFoundException();
        }

        this.categories.get(category).removeIf(el -> el.equals(new MyUser(friend)));
    }

    @Override
    public boolean put(String passw, E dato, String category) throws NullPointerException,
            UnauthorizedAccessException, CategoryNotFoundException, DataAlreadyPutException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        if (dato == null || category == null) {
            throw new NullPointerException();
        }

        if (!this.hasCategory(category)) {
            throw new CategoryNotFoundException();
        }

        if (this.hasData(dato)) {
            throw new DataAlreadyPutException();
        }

        // imposta la categoria al dato
        dato.setCategory(category);

        return this.dataList.add((E)dato.clone());
    }

    @Override
    public E get(String passw, E dato) throws NullPointerException, UnauthorizedAccessException,
            DataNotFoundException {
        // validazione
        if (!this.owner.authenticate(passw)) {
            throw new UnauthorizedAccessException();
        }

        if (dato == null) {
            throw new NullPointerException();
        }

        for (E item : this.dataList) {
            if (item.equals(dato)) {
                return (E)item.clone();
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

        if (dato == null) {
            throw new NullPointerException();
        }

        for (E item : this.dataList) {
            if (item.equals(dato)) {
                this.dataList.removeIf(el -> el.equals(dato));

                return item;
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

        // ritorna una deep copy della lista
        // di dati della categoria
        ArrayList<E> dataList = new ArrayList<>();

        // cicla tutti i dati
        for (E item : this.dataList) {
            if (item.getCategory().equals(category)) {
                    dataList.add((E)item.clone());
            }
        }

        if (dataList.size() > 0) {
            return dataList;
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

        // cerco il dato
        for (E item : this.dataList) {
            if (item.equals(data)) {
                //controlla che friend abbia
                // i permessi di lettura
                if (!this.isReadableBy(item.getCategory(), new MyUser(friend))) {
                    throw new UnauthorizedAccessException();
                }

                // controlla se friend ha già inserito un like
                List<User> likesList = item.getLikes();

                for (User user : likesList) {
                    if (user.equals(new MyUser(friend))) {
                        throw new FriendAlreadyAddedException();
                    }
                }

                //inserisci il like
                item.insertLike(friend);

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

        // converti il TreeSet in ArrayList
        List<E> dataList = new ArrayList<>(this.dataList);

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

        for (E item : this.dataList) {
            if (this.isReadableBy(item.getCategory(), new MyUser(friend))) {
                dataList.add(item);
            }
        }

        if (dataList.size() == 0) {
            boolean friendFound = false;

            for (String item : this.categories.keySet()) {
                if (this.isReadableBy(item, new MyUser(friend))) {
                    friendFound = true;
                }
            }

            if (!friendFound) {
                throw new UserNotFoundException();
            }
        }

        // disabilita il metodo remove dell'iteratore che verrà generato
        // sfruttando il metodo Collections.unmodifiableList
        dataList = Collections.unmodifiableList(dataList);

        // ritorna l'iteratore
        return dataList.iterator();
    }

    @Override
    public boolean hasCategory(String category) throws NullPointerException {
        //validazione
        if (category == null) {
            throw new NullPointerException();
        }

        return this.categories.containsKey(category);
    }

    @Override
    public boolean isReadableBy(String category, User user) throws NullPointerException {
        for (User item : this.categories.get(category)) {
            if (item.equals(user)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasData(E data) throws NullPointerException {
        //validazione
        if (data == null) {
            throw new NullPointerException();
        }

        for (E item : this.dataList) {
            if (item.equals(data)) {
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

        // cerco il dato
        for (E item : this.dataList) {
            if (item.equals(data)) {
                //se lo trovo ritorna i like
                return item.getLikes();
            }
        }

        // il dato non è stato trovato, errore
        throw new DataNotFoundException();
    }
}
