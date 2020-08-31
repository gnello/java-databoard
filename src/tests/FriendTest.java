package tests;

import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyUser;

public class FriendTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final String categoryName;

    private final User friend;

    // Assegna dataBoard e password,
    // inizializza categoryName e friend
    public FriendTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.categoryName = "test_category";
        this.friend = new MyUser("tony");
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
    // crea una categoria di default e abilita
    // un amico alla lettura
    private void beforeRemove()
    {
        this.beforeAll();

        String methodName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            throw new TestException(methodName);
        }
    }

    // questo metodo è chiamato dopo ogni test
    // ed elimina la categoria e rimuove gli amici
    // dai permessi di lettura
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

    public void we_can_add_a_friend_to_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente e non
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // aggiungi friend ai permessi di lettura
        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // verifica se la categoria è leggibile da friend
        if (!this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName, "The category \"" + this.categoryName + "\" is not readable after addFriend.");
        }

        AbstractTest.printSuccess(testName);

        this.afterAll();
    }

    public void we_can_not_add_a_friend_to_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente e non
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // prova ad aggiugnere friend ai permessi
        // di lettura con una psw errata
        try {
            this.dataBoard.addFriend(this.categoryName, "0000", this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The given password it's not valid but a friend was added to a category.");
    }

    public void we_can_not_add_a_friend_to_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente e non
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // prova ad aggiungere friend ai permessi di
        // lettura di una categoria null
        try {
            this.dataBoard.addFriend(null, this.password, this.friend.getName());
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was added to a null category.");
    }

    public void we_can_not_add_a_null_friend_to_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // la categoria deve essere presente e non
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // prova ad aggiungere un friend null ai permessi
        // di lettura di una categoria
        try {
            this.dataBoard.addFriend(this.categoryName, this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null friend was added to a category.");
    }

    public void we_can_not_add_a_friend_to_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // prova ad aggiungere friend ai permessi di lettura
        // di una categoria che non esiste
        try {
            this.dataBoard.addFriend("not_exists", this.password, this.friend.getName());
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was added to a not existing category.");
    }

    public void we_can_not_add_a_friend_that_was_already_added_to_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // prova ad aggiungere 2 volte lo stesso friend
        // ai permessi di lettura di una categoria
        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (FriendAlreadyAddedException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A same friend was added twice to a category.");
    }

    public void we_can_remove_a_friend_from_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        // la categoria deve essere presente e
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // rimuovi friend dai permessi di lettura di una categoria
        try {
            this.dataBoard.removeFriend(this.categoryName, this.password, this.friend.getName());
        } catch (UnauthorizedAccessException | UserNotFoundException e) {
            throw new TestException(testName);
        }

        // verifica che la categoria non sia più leggibile da friend
        if (this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName, "The category \"" + this.categoryName + "\" is readable after removeFriend.");
        }

        AbstractTest.printSuccess(testName);

        this.afterAll();
    }

    public void we_can_not_remove_a_friend_from_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        // la categoria deve essere presente e
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // prova a rimuovere friend dai permessi lettura
        // con una psw errata
        try {
            this.dataBoard.removeFriend(this.categoryName, "0000", this.friend.getName());
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UserNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given password it's not valid but a friend was removed.");
    }

    public void we_can_not_remove_a_friend_from_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        // la categoria deve essere presente e
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // prova a rimuovere friend dai permessi di lettura
        // di una categoria che non esiste
        try {
            this.dataBoard.removeFriend(null, this.password, this.friend.getName());
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | UserNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was attempt to be removed from a null category.");
    }

    public void we_can_not_remove_a_null_friend_from_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        // la categoria deve essere presente e
        // deve essere leggibile da friend
        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        // prova a rimuvoere un friend null dai
        // permessi di lettura di una categoria
        try {
            this.dataBoard.removeFriend(this.categoryName, this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | UserNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null friend was removed from a category.");
    }

    public void we_can_not_remove_a_friend_from_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // prova a rimuovere friend dai permessi di lettura
        // di una categoria che non esiste
        try {
            this.dataBoard.removeFriend("not_exists", this.password, this.friend.getName());
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | UserNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was removed from a category that doesn't exist.");
    }

    public void we_can_not_remove_a_friend_that_is_not_present_in_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        // prova a rimuovere un friend che non è mai stato aggiunto
        // ai permessi di lettura della categoria
        try {
            this.dataBoard.removeFriend(this.categoryName, this.password, "test");
        } catch (UserNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was removed from a category although it was not present.");
    }
}
