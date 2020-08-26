package tests;

import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import models.MyData;
import models.MyUser;

public class CategoryTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final String categoryName;

    // Assegna dataBoard e password,
    // inizializza categoryName
    public CategoryTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.categoryName = "test_category";
    }

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

        if (this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.createCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName, "The category \"" + this.categoryName + "\" doesn't exists after create.");
        }

        AbstractTest.printSuccess(testName);

        this.afterAll();
    }

    public void we_can_not_create_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

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

        try {
            this.dataBoard.removeCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

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

        try {
            this.dataBoard.put(this.password, data, this.categoryName);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.removeCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

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
