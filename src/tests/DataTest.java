package tests;

import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyData;
import models.MyUser;

import java.util.List;

public class DataTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final String categoryName;

    private final Data data;

    private final User friend;

    // Assegna dataBoard e password,
    // inizializza categoryName, data e friend
    public DataTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.categoryName = "test_category";
        this.data = new MyData(1, "Lorem ipsum");

        this.friend = new MyUser("jarvis");
    }

    // questo metodo è chiamato prima di ogni
    // test e crea una categoria di default
    private void beforeAll()
    {
        String methodName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.createCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }
    }

    // questo metodo, chiamato prima di un test,
    // crea una categoria di default e vi inserisce
    // un dato
    private void beforeGetOrRemove()
    {
        this.beforeAll();

        String methodName = AbstractTest.getCurrentMethodName();

        try {
            boolean res = this.dataBoard.put(this.password, this.data, this.categoryName);

            if (!res) {
                throw new TestException(methodName);
            }

        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }
    }

    // questo metodo, chiamato prima di un test,
    // crea una categoria di default , vi inserisce
    // un dato e abilita un amico a leggere la categoria
    private void beforeLike()
    {
        this.beforeGetOrRemove();

        String methodName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }
    }

    // questo metodo è chiamato dopo ogni test
    // ed elimina la categoria e i dati creati
    // di default e rimuove gli amici dai permessi
    // di lettura
    // (resetta lo stato dell'esecuzione)
    private void afterAll() {
        String methodName = AbstractTest.getCurrentMethodName();

        if (this.dataBoard.hasCategory(this.categoryName)) {
            try {
                this.dataBoard.removeCategory(this.categoryName, this.password);
            } catch (UnauthorizedAccessException e) {
                throw new TestException(methodName, "Can't remove category \"" + this.categoryName + "\".");
            }
        }

        if (this.dataBoard.hasData(this.data)) {
            try {
                this.dataBoard.remove(this.password, this.data);
            } catch (UnauthorizedAccessException e) {
                throw new TestException(methodName, "Can't remove data \"" + this.data.getId() + "\".");
            }
        }

        try {
            if (this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
                try {
                    this.dataBoard.removeFriend(this.categoryName, this.password, this.friend.getName());
                } catch (UserNotFoundException | UnauthorizedAccessException e) {
                    throw new TestException(methodName, "Can't remove friend \"" + this.friend.getName() + "\".");
                }
            }
        } catch (CategoryNotFoundException e) {
            // ignore
        }
    }

    public void we_can_put_a_data_into_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // inserisci un dato nella categoria
        try {
            boolean res = this.dataBoard.put(this.password, this.data, this.categoryName);

            if (res) {
                AbstractTest.printSuccess(testName);
            } else {
                throw new TestException(testName, "Put method failed.");
            }

            this.afterAll();

        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }
    }

    public void we_can_not_put_a_data_into_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // prova ad inserire un dato con una psw errata
        try {
            this.dataBoard.put("0000", this.data, this.categoryName);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The given password it's not valid but a data was put.");
    }

    public void we_can_not_put_a_null_data_into_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // prova ad inserire un dato null nella categoria
        try {
            this.dataBoard.put(this.password, null, this.categoryName);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null data was put into a category.");
    }

    public void we_can_not_put_a_data_into_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // prova ad inserire un dato in una categoria null
        try {
            this.dataBoard.put(this.password, this.data, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data was put into a null category.");
    }

    public void we_can_not_put_a_data_into_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // prova ad inserire un dato in una categoria che non esiste
        try {
            this.dataBoard.put(this.password, this.data, "not_exists");
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data was put into a category that doesn't exist.");
    }

    public void we_can_not_put_the_same_data_twice_into_any_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // crea una seconda categoria
        try {
            this.dataBoard.createCategory(this.categoryName + "2", this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // prova ad inserire lo stesso dato in due categorie diverse
        try {
            this.dataBoard.put(this.password, this.data, this.categoryName);
            this.dataBoard.put(this.password, this.data, this.categoryName + "2");
        } catch (DataAlreadyPutException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data was put twice into any category.");
    }

    public void we_can_get_a_data_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prendi il dato dalla databoard
        Data data;
        try {
            data = this.dataBoard.get(this.password, this.data);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // controlla che il dato sia quello atteso
        if (this.data.equals(data)) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "A different data was get.");
    }

    public void we_can_not_get_a_shallow_copy_of_a_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prendi il dato dalla databoard
        Data data;
        try {
            data = this.dataBoard.get(this.password, this.data);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // verifica che il dato non sia una shallow copy
        if (this.data.equals(data) && this.data == data) {
            throw new TestException(testName, "A shallow copy of data was get.");
        }

        AbstractTest.printSuccess(testName);

        this.afterAll();
    }

    public void we_can_not_get_a_data_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a prendere un dato con una psw errata
        try {
            this.dataBoard.get("0000", this.data);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The given password it's not valid but a data was get.");
    }

    public void we_can_not_get_a_null_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a prendere un dato null
        try {
            this.dataBoard.get(this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null data was get.");
    }

    public void we_can_not_get_a_data_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente, il dato non deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName) || this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a prendere un dato che non esiste
        try {
            this.dataBoard.get(this.password, this.data);
        } catch (DataNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data that doesn't exists was get.");
    }

    public void we_can_remove_a_data_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // rimuovi un dato
        try {
            this.dataBoard.remove(this.password, this.data);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // verifica che siano rimossi anche i dati nella categoria
        if (!this.dataBoard.hasData(this.data)) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The data still exists after remove.");
    }

    public void we_can_not_remove_a_data_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a rimuovere un dato con una psw errata
        try {
            this.dataBoard.remove("0000", this.data);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The given password it's not valid but the data was removed.");
    }

    public void we_can_not_remove_a_null_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a rimuovere un dato null
        try {
            this.dataBoard.remove(this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null data was removed.");
    }

    public void we_can_not_remove_a_data_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente, il dato non deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName) || this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a rimuovere un dato che non esiste
        try {
            this.dataBoard.remove(this.password, this.data);
        } catch (DataNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data that doesn't exist was removed.");
    }

    public void we_can_get_the_removed_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // rimuovi il dato
        Data data;
        try {
            data = this.dataBoard.remove(this.password, this.data);

            // verifica che venga restituito il dato atteso
            if (this.data.equals(data)) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
            }
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "Can't get the data removed.");
    }

    public void we_can_get_the_list_of_data_of_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prendi la lista di dati
        List<Data> dataList;
        try {
            dataList = this.dataBoard.getDataCategory(this.password, this.categoryName);

            // verifica che la lista di dati sia quella attesa
            if (dataList.contains(this.data)) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
            }
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "Can't get the list of data of a category.");
    }

    public void we_can_not_get_the_list_of_data_of_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a prendere la lista dei dati con una psw errata
        try {
            this.dataBoard.getDataCategory("0000", this.categoryName);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The given password it's not valid but the data list was got.");
    }

    public void we_can_not_get_the_list_of_data_of_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a prendere la lista dei dati di una categoria null
        try {
            this.dataBoard.getDataCategory(this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The data list of a null category was got.");
    }

    public void we_can_not_get_the_list_of_data_of_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova a prendere la lista dei dati di una categoria che non esiste
        try {
            this.dataBoard.getDataCategory(this.password, "not_exists");
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The data list of a category that doesn't exist was got.");
    }

    public void we_can_insert_a_like_with_a_valid_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // inserisci un like
        try {
            this.dataBoard.insertLike(this.friend.getName(), this.data);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // prendi tutti i like
        List<User> list;
        try {
            list = this.dataBoard.getLikes(this.data);
        } catch (DataNotFoundException e) {
            throw new TestException(testName);
        }

        // verifica che il like inserito sia presente nella lista dei like
        for (User item : list) {
            if (item.equals(this.friend)) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
            }
        }

        throw new TestException(testName, "Can't insert a like.");
    }

    public void we_can_not_insert_a_like_with_a_wrong_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova ad inserire un like con uno user che non ha i permessi
        try {
            this.dataBoard.insertLike("tony", this.data);
        } catch (UnauthorizedAccessException e) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
        }

        throw new TestException(testName, "The given user has not access to data but can insert a like.");
    }

    public void we_can_not_insert_a_like_twice_for_the_same_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova ad inserire un like due volte con lo stesso friend sullo stesso dato
        try {
            this.dataBoard.insertLike(this.friend.getName(), this.data);
            this.dataBoard.insertLike(this.friend.getName(), this.data);
        } catch (FriendAlreadyAddedException e) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
        } catch (DataNotFoundException | UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given user inserted a like twice.");
    }

    public void we_can_not_insert_a_like_into_a_null_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        // la categoria e il dato devono essere presenti
        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        // prova ad inserire un like su un dato null
        try {
            this.dataBoard.insertLike(this.friend.getName(), null);
        } catch (NullPointerException e) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given user inserted a like in a null data.");
    }

    public void we_can_not_insert_a_like_into_a_data_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        // prova ad inserire un like su un dato che non esiste
        try {
            this.dataBoard.insertLike(this.friend.getName(), new MyData(2020, "Lorem Ipsum"));
        } catch (DataNotFoundException e) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given user inserted a like in a data that doesn't exist.");
    }
}
