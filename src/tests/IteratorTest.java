package tests;

import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyData;
import models.MyUser;

import java.util.Iterator;

public class IteratorTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final User friend;

    // Assegna dataBoard e password,
    // inizializza friend
    public IteratorTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.friend = new MyUser("tony");
    }

    // questo metodo è chiamato prima di ogni
    // test e crea una categoria di default,
    // aggiunge friend ai permessi di lettura
    // ed inserisce un dato nella categoria
    private void before() {
        String methodName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.createCategory("test", this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }

        try {
            this.dataBoard.addFriend("test", this.password, this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }

        try {
            this.dataBoard.put(this.password, new MyData(1, "Lorem ipsum"), "test");
        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }
    }

    // questo metodo è chiamato dopo ogni test
    // ed elimina la categoria, rimuove gli amici
    // dai permessi di lettura e rimuove il dato
    // (resetta lo stato dell'esecuzione)
    private void after() {
        String methodName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.removeCategory("test", this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }

        try {
            if (this.dataBoard.isReadableBy("test", this.friend)) {
                try {
                    this.dataBoard.removeFriend("test", this.password, this.friend.getName());
                } catch (UserNotFoundException | UnauthorizedAccessException e) {
                    throw new TestException(methodName, "Can't remove friend \"" + this.friend.getName() + "\".");
                }
            }
        } catch (CategoryNotFoundException e) {
            // ignore
        }

        Data data = new MyData(1, "Lorem ipsum");
        if (this.dataBoard.hasData(data)) {
            try {
                this.dataBoard.remove(this.password, data);
            } catch (UnauthorizedAccessException e) {
                throw new TestException(methodName, "Can't remove data \"" + this.friend.getName() + "\".");
            }
        }
    }

    public void we_can_get_an_iterator_with_a_valid_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // prendi un iteratore
        Iterator<Data> iterator;
        try {
            iterator = this.dataBoard.getIterator(this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // verifica che l'iteratore non sia null
        if (iterator != null) {
            AbstractTest.printSuccess(testName);

            return;
        }

        throw new TestException(testName, "Can't get an iterator.");
    }

    public void we_can_not_get_an_iterator_with_a_wrong_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // prova a prendere un iteratore con psw errata
        try {
            this.dataBoard.getIterator("0000");
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            return;
        }

        throw new TestException(testName, "The password it's invalid but we get an iterator.");
    }

    public void we_can_get_an_iterator_ordered_by_likes_desc()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // crea due categorie di test
        try {
            this.dataBoard.createCategory("test1", this.password);
            this.dataBoard.createCategory("test2", this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // inserisci 3 amici ai permessi di lettura delle categorie
        try {
            this.dataBoard.addFriend("test1", this.password, "luca");
            this.dataBoard.addFriend("test2", this.password, "luca");
            this.dataBoard.addFriend("test1", this.password, "matteo");
            this.dataBoard.addFriend("test1", this.password, "gabriele");
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // crea 4 dati
        MyData data1 = new MyData(1, "Lorem ipsum");
        MyData data2 = new MyData(2, "dolor sit amet");
        MyData data3 = new MyData(3, "consectetur adipisci elit");
        MyData data4 = new MyData(4, "sed do eiusmod tempor incidunt ut labore et dolore magna aliqua");

        // inserisci i dati casualmente fra le categorie
        try {
            this.dataBoard.put(this.password, data1, "test2");
            this.dataBoard.put(this.password, data2, "test1");
            this.dataBoard.put(this.password, data3, "test1");
            this.dataBoard.put(this.password, data4, "test1");
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        try {
            // inserisci i like ai dati in modo da avere
            // in ordine decrescente (dal dato con più like
            // al dato con meno like): data2, data3, data1, data4
            this.dataBoard.insertLike("luca", data1);

            this.dataBoard.insertLike("luca", data2);
            this.dataBoard.insertLike("matteo", data2);
            this.dataBoard.insertLike("gabriele", data2);

            this.dataBoard.insertLike("matteo", data3);
            this.dataBoard.insertLike("gabriele", data3);

        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // prendi l'iteratore
        try {
            Iterator<Data> iterator = this.dataBoard.getIterator(this.password);

            // verifica che sia ordinato per ordine decrescente in
            // base ai like inseriti
            // ordine atteso: data2, data3, data1, data4
            if (iterator.next().equals(data2)
                    && iterator.next().equals(data3)
                    && iterator.next().equals(data1)
                    && iterator.next().equals(data4)) {
                AbstractTest.printSuccess(testName);

                // resetta lo stato dell'esecuzione rimuovendo le categorie
                // e tutti i relativi dati/permessi
                try {
                    this.dataBoard.removeCategory("test1", this.password);
                    this.dataBoard.removeCategory("test2", this.password);

                    try {
                        this.dataBoard.removeFriend("test1", this.password, this.friend.getName());
                        this.dataBoard.removeFriend("test2", this.password, this.friend.getName());
                    } catch (UserNotFoundException e) {
                        throw new TestException(testName, "Can't remove friend \"" + this.friend.getName() + "\".");
                    } catch (CategoryNotFoundException e) {
                        //ignore
                    }

                    this.dataBoard.remove(this.password, data1);
                    this.dataBoard.remove(this.password, data2);
                    this.dataBoard.remove(this.password, data3);
                    this.dataBoard.remove(this.password, data4);
                } catch (UnauthorizedAccessException e) {
                    throw new TestException(testName);
                } catch (DataNotFoundException e) {
                    //ignore
                }

                return;
            }
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "We can't get an iterator ordered by likes desc.");
    }

    public void we_can_get_an_iterator_that_doesnt_support_remove_method()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.before();

        // prendi l'iteratore
        Iterator<Data> iterator;
        try {
            iterator = this.dataBoard.getIterator(this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName + e);
        }

        // verifica che non sia possibile chiamare il metodo remove
        try {
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            AbstractTest.printSuccess(testName);

            this.after();

            return;
        }

        throw new TestException(testName, "We can remove elements from an iterator.");
    }

    public void we_can_get_a_friend_iterator_with_a_valid_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.before();

        // prendi un friend iterator
        Iterator<Data> iterator;
        try {
            iterator = this.dataBoard.getFriendIterator(this.friend.getName());
        } catch (UserNotFoundException e) {
            throw new TestException(testName);
        }

        // verifica che non sia null
        if (iterator != null) {
            AbstractTest.printSuccess(testName);

            this.after();

            return;
        }

        throw new TestException(testName, "Can't get a friend iterator.");
    }

    public void we_can_not_get_a_friend_iterator_with_a_wrong_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.before();

        // prova a prendere un friend iterator
        // di un friend che non esiste
        try {
            this.dataBoard.getFriendIterator("gino");
        } catch (UserNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.after();

            return;
        }

        throw new TestException(testName, "The user doesn't exist but we can get a friend iterator.");
    }

    public void we_can_get_a_friend_iterator_that_doesnt_support_remove_method()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.before();

        // prendi il friend iterator
        Iterator<Data> iterator;
        try {
            iterator = this.dataBoard.getFriendIterator(this.friend.getName());
        } catch (UserNotFoundException e) {
            throw new TestException(testName + e);
        }

        // verifica che non sia possibile chiamare il metodo remove
        try {
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            AbstractTest.printSuccess(testName);

            this.after();

            return;
        }

        throw new TestException(testName, "We can remove elements from a friend iterator.");
    }

    public void we_can_get_a_friend_iterator_with_all_his_readable_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // crea tre categorie di test
        try {
            this.dataBoard.createCategory("category_a", this.password);
            this.dataBoard.createCategory("category_b", this.password);
            this.dataBoard.createCategory("category_c", this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // aggiungi friend ai permessi di lettura di 2 categorie
        try {
            this.dataBoard.addFriend("category_a", this.password, this.friend.getName());
            this.dataBoard.addFriend("category_b", this.password, this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // crea 8 dati
        MyData data1 = new MyData(1, "Lorem ipsum");
        MyData data2 = new MyData(2, "dolor sit amet");
        MyData data3 = new MyData(3, "consectetur adipisci elit");
        MyData data4 = new MyData(4, "sed do eiusmod tempor incidunt ut labore et dolore magna aliqua");
        MyData data5 = new MyData(5, "Ut enim ad minim veniam");
        MyData data6 = new MyData(6, "quis nostrum exercitationem ullamco laboriosam");
        MyData data7 = new MyData(7, "nisi ut aliquid ex ea commodi consequatur");
        MyData data8 = new MyData(8, "Excepteur sint obcaecat cupiditat non proident");

        // inserisci 7 degli 8 dati sparsi fra
        // le categorie leggibili da friend
        try {
            this.dataBoard.put(this.password, data1, "category_a");
            this.dataBoard.put(this.password, data2, "category_a");
            this.dataBoard.put(this.password, data3, "category_a");
            this.dataBoard.put(this.password, data4, "category_a");
            this.dataBoard.put(this.password, data5, "category_b");
            this.dataBoard.put(this.password, data6, "category_b");
            this.dataBoard.put(this.password, data7, "category_b");
            this.dataBoard.put(this.password, data8, "category_c");
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // prendi il friend iterator
        Iterator<Data> iterator;
        try {
            iterator = this.dataBoard.getFriendIterator(this.friend.getName());
        } catch (UserNotFoundException e) {
            throw new TestException(testName);
        }

        boolean containsAllData = true;

        // verifica che friend iterator contenga solamente
        // i dati delle categorie leggibili da friend
        while (iterator.hasNext()) {
            Data iteratorData = iterator.next();

            if ((!iteratorData.equals(data1)
                    && !iteratorData.equals(data2)
                    && !iteratorData.equals(data3)
                    && !iteratorData.equals(data4)
                    && !iteratorData.equals(data5)
                    && !iteratorData.equals(data6)
                    && !iteratorData.equals(data7)
                ) || iteratorData.equals(data8)) {
                containsAllData = false;
            }
        }

        if (containsAllData) {
            AbstractTest.printSuccess(testName);

            // resetta lo stato dell'esecuzione rimuovendo le categorie
            // e tutti i relativi dati/permessi
            try {
                this.dataBoard.removeCategory("category_a", this.password);
                this.dataBoard.removeCategory("category_b", this.password);
                this.dataBoard.removeCategory("category_c", this.password);

                try {
                    this.dataBoard.removeFriend("category_a", this.password, this.friend.getName());
                    this.dataBoard.removeFriend("category_b", this.password, this.friend.getName());
                } catch (UserNotFoundException e) {
                    throw new TestException(testName, "Can't remove friend \"" + this.friend.getName() + "\".");
                } catch (CategoryNotFoundException e) {
                    //ignore
                }
                
                this.dataBoard.remove(this.password, data1);
                this.dataBoard.remove(this.password, data2);
                this.dataBoard.remove(this.password, data3);
                this.dataBoard.remove(this.password, data4);
                this.dataBoard.remove(this.password, data5);
                this.dataBoard.remove(this.password, data6);
                this.dataBoard.remove(this.password, data7);
                this.dataBoard.remove(this.password, data8);
            } catch (UnauthorizedAccessException e) {
                throw new TestException(testName);
            } catch (DataNotFoundException e) {
                //ignore
            }

            return;
        }

        throw new TestException(testName, "We cant get a friend iterator with all his readable data.");
    }

    public void we_can_get_an_empty_friend_iterator()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // crea due categorie
        try {
            this.dataBoard.createCategory("category_a", this.password);
            this.dataBoard.createCategory("category_b", this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // aggiungi friend ai permessi di lettura delle 2 categorie
        try {
            this.dataBoard.addFriend("category_a", this.password, this.friend.getName());
            this.dataBoard.addFriend("category_b", this.password, this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // prendi il friend iterator
        Iterator<Data> iterator;
        try {
            iterator = this.dataBoard.getFriendIterator(this.friend.getName());
        } catch (UserNotFoundException e) {
            throw new TestException(testName);
        }

        // verifica che il friend iterator sia vuoto
        if (!iterator.hasNext()) {
            AbstractTest.printSuccess(testName);

            // resetta lo stato dell'esecuzione rimuovendo le categorie
            // e tutti i relativi dati/permessi
            try {
                this.dataBoard.removeCategory("category_a", this.password);
                this.dataBoard.removeCategory("category_b", this.password);

                try {
                    this.dataBoard.removeFriend("category_a", this.password, this.friend.getName());
                    this.dataBoard.removeFriend("category_b", this.password, this.friend.getName());
                } catch (UserNotFoundException e) {
                    throw new TestException(testName, "Can't remove friend \"" + this.friend.getName() + "\".");
                } catch (CategoryNotFoundException e) {
                    //ignore
                }
            } catch (UnauthorizedAccessException e) {
                throw new TestException(testName);
            }

            return;
        }

        throw new TestException(testName, "We cant get an empty friend.");
    }
}
