package tests;

import exceptions.CategoryAlreadyExistsException;
import exceptions.CategoryNotFoundException;
import exceptions.InvalidPasswordException;
import exceptions.TestException;
import interfaces.Data;
import interfaces.DataBoard;

public class CategoryTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final String categoryName;

    public CategoryTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.categoryName = "test_category";
    }

    private void beforeRemove() {
        String methodName = AbstractTest.getCurrentMethodName();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            try {
                this.dataBoard.createCategory(this.categoryName, this.password);
            } catch (InvalidPasswordException | CategoryAlreadyExistsException e) {
                throw new TestException(methodName, "Can't create category \"" + this.categoryName + "\".");
            }
        }
    }

    private void afterAll() {
        String methodName = AbstractTest.getCurrentMethodName();

        if (this.dataBoard.hasCategory(this.categoryName)) {
            try {
                this.dataBoard.removeCategory(this.categoryName, this.password);
            } catch (InvalidPasswordException | CategoryNotFoundException e) {
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
        } catch (InvalidPasswordException | CategoryAlreadyExistsException e) {
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
        } catch (InvalidPasswordException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CategoryAlreadyExistsException e) {
            throw new TestException(testName);
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
        } catch (InvalidPasswordException | CategoryAlreadyExistsException e) {
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
        } catch (InvalidPasswordException e) {
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
        } catch (InvalidPasswordException | CategoryNotFoundException e) {
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
        } catch (InvalidPasswordException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CategoryNotFoundException e) {
            throw new TestException(testName);
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
        } catch (InvalidPasswordException | CategoryNotFoundException e) {
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
        } catch (InvalidPasswordException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A category that not exists was attempt to remove.");
    }
}
