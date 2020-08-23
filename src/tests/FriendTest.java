package tests;

import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyUser;

public class FriendTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final String categoryName;

    private final User friend;

    // Assegna dataBoard e password, crea una categoria
    // di default per i test su friend, inizializza
    // categoryName e friendName
    public FriendTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.categoryName = "test_category";
        this.friend = new MyUser("tony", "0000");
    }

    private void beforeAll()
    {
        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.createCategory(this.categoryName, this.password);
        } catch (InvalidPasswordException | CategoryAlreadyExistsException e) {
            throw new TestException(testName);
        }
    }

    private void beforeRemove()
    {
        this.beforeAll();

        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (CategoryNotFoundException | InvalidPasswordException
                | FriendAlreadyAddedException e) {
            throw new TestException(testName);
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

    public void we_can_add_a_friend_to_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (InvalidPasswordException | CategoryNotFoundException
                | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

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

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.addFriend(this.categoryName, "0000", this.friend.getName());
        } catch (InvalidPasswordException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CategoryNotFoundException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given password it's not valid but a friend was added to a category.");
    }

    public void we_can_not_add_a_friend_to_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.addFriend(null, this.password, this.friend.getName());
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (InvalidPasswordException | CategoryNotFoundException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was added to a null category.");
    }

    public void we_can_not_add_a_null_friend_to_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.addFriend(this.categoryName, this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CategoryNotFoundException | InvalidPasswordException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null friend was added to a category.");
    }

    public void we_can_not_add_a_friend_to_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        try {
            this.dataBoard.addFriend("not_exists", this.password, this.friend.getName());
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (InvalidPasswordException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was added to a not existing category.");
    }

    public void we_can_not_add_a_friend_that_was_already_added_to_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (FriendAlreadyAddedException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (InvalidPasswordException | CategoryNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A same friend was added twice to a category.");
    }

    public void we_can_remove_a_friend_from_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.removeFriend(this.categoryName, this.password, this.friend.getName());
        } catch (InvalidPasswordException | CategoryNotFoundException | FriendNotFoundException e) {
            throw new TestException(testName);
        }

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

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.removeFriend(this.categoryName, "0000", this.friend.getName());
        } catch (InvalidPasswordException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CategoryNotFoundException | FriendNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given password it's not valid but a friend was removed.");
    }

    public void we_can_not_remove_a_friend_from_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.removeFriend(null, this.password, this.friend.getName());
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (InvalidPasswordException | CategoryNotFoundException | FriendNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was attempt to be removed from a null category.");
    }

    public void we_can_not_remove_a_null_friend_from_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) ||
                !this.dataBoard.isReadableBy(this.categoryName, this.friend)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.removeFriend(this.categoryName, this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (InvalidPasswordException | CategoryNotFoundException | FriendNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null friend was removed from a category.");
    }

    public void we_can_not_remove_a_friend_from_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.removeFriend("not_exists", this.password, this.friend.getName());
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (InvalidPasswordException | FriendNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was removed from a category that doesn't exist.");
    }

    public void we_can_not_remove_a_friend_that_is_not_present_in_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        try {
            this.dataBoard.removeFriend(this.categoryName, this.password, "test");
        } catch (FriendNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (InvalidPasswordException | CategoryNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A friend was removed from a category although it was not present.");
    }
}
