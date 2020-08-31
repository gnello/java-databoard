package tests;

import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import models.MyData;

public class CategoryTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final String categoryName;

    // Assegna dataBoard e password,
    // inizializza categoryName
    public CategoryTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.categoryName = "test_category";
    }

    // questo metodo è chiamato prima di ogni
    // test e crea una categoria di default
    private void beforeRemove() {
        String methodName = AbstractTest.getCurrentMethodName();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            try {
                this.dataBoard.createCategory(this.categoryName, this.password);
            } catch (UnauthorizedAccessException e) {
                throw new TestException(methodName, "Can't create category \"" + this.categoryName + "\".");
            }
        }
    }

    // questo metodo è chiamato dopo ogni test
    // ed elimina la categoria creata di default
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
    }

    public void we_can_create_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // la categoria non deve essere presente
        if (this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // crea la categoria
        try {
            this.dataBoard.createCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // adesso la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName, "The category \"" + this.categoryName + "\" doesn't exists after create.");
        }

        AbstractTest.printSuccess(testName);

        this.afterAll();
    }

    public void we_can_not_create_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // prova a creare la categoria con una psw errata
        try {
            this.dataBoard.createCategory(this.categoryName, null);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The given password it's not valid but a category was created.");
    }

    public void we_can_not_create_a_category_with_a_null_name()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // prova a creare una categoria con name errato
        try {
            this.dataBoard.createCategory(null, this.password);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A category with null name was created.");
    }

    public void we_can_not_create_a_category_that_already_exists()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // prova ad creare 2 volte la stessa categoria
        try {
            this.dataBoard.createCategory(this.categoryName, this.password);
            this.dataBoard.createCategory(this.categoryName, this.password);
        } catch (CategoryAlreadyExistsException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A category that already exists was created.");
    }

    public void we_can_remove_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        // la categoria deve essere presente
        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        // rimuovi la categoria
        try {
            this.dataBoard.removeCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // adesso la categoria non deve più essere presente
        if (this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName, "The category \"" + this.categoryName + "\" still exists after remove.");
        }

        AbstractTest.printSuccess(testName);

        this.afterAll();
    }

    public void we_can_not_remove_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        // prova a rimuovere una categoria con una psw errata
        try {
            this.dataBoard.removeCategory(this.categoryName, null);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        }

        throw new TestException(testName, "The given password it's not valid but a category was removed.");
    }

    public void we_can_not_remove_a_category_with_a_null_name()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        // prova a rimuovere una categoria con name null
        try {
            this.dataBoard.removeCategory(null, this.password);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A category with null name was attempt to remove.");
    }

    public void we_can_not_remove_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        // prova a rimuovere una categoria che non esiste
        try {
            this.dataBoard.removeCategory(this.categoryName, this.password);
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A category that not exists was attempt to remove.");
    }

    public void we_can_remove_a_category_and_all_its_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        MyData data = new MyData(1, "Lorem ipsum");

        // inserisce un dato nella categoria
        try {
            this.dataBoard.put(this.password, data, this.categoryName);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // rimuovi la categoria
        try {
            this.dataBoard.removeCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        // prova a recuperare il dato inserito prima
        // nella categoria
        try {
            this.dataBoard.get(this.password, data);
        } catch (DataNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A category was removed but not its data.");
    }
}
