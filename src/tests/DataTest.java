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
        this.data = new MyData(1);

        this.friend = new MyUser("jarvis");
    }

    private void beforeAll()
    {
        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.createCategory(this.categoryName, this.password);
        } catch (UnauthorizedAccessException | CategoryAlreadyExistsException e) {
            throw new TestException(testName);
        }
    }

    private void beforeGetOrRemove()
    {
        this.beforeAll();

        String testName = AbstractTest.getCurrentMethodName();

        try {
            boolean res = this.dataBoard.put(this.password, this.data, this.categoryName);

            if (!res) {
                throw new TestException(testName);
            }

        } catch (UnauthorizedAccessException | CategoryNotFoundException | DataAlreadyPutException e) {
            throw new TestException(testName);
        }
    }

    private void beforeLike()
    {
        this.beforeGetOrRemove();

        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.addFriend(this.categoryName, this.password, this.friend.getName());
        } catch (UnauthorizedAccessException | CategoryNotFoundException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }
    }

    private void afterAll() {
        String methodName = AbstractTest.getCurrentMethodName();

        if (this.dataBoard.hasCategory(this.categoryName)) {
            try {
                this.dataBoard.removeCategory(this.categoryName, this.password);
            } catch (UnauthorizedAccessException | CategoryNotFoundException e) {
                throw new TestException(methodName, "Can't remove category \"" + this.categoryName + "\".");
            }
        }
    }

    public void we_can_put_a_data_into_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        try {
            boolean res = this.dataBoard.put(this.password, this.data, this.categoryName);

            if (res) {
                AbstractTest.printSuccess(testName);
            } else {
                throw new TestException(testName, "Put method failed.");
            }

            this.afterAll();

        } catch (UnauthorizedAccessException | CategoryNotFoundException | DataAlreadyPutException e) {
            throw new TestException(testName);
        }
    }

    public void we_can_not_put_a_data_into_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.put("0000", this.data, this.categoryName);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CategoryNotFoundException | DataAlreadyPutException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given password it's not valid but a data was put.");
    }

    public void we_can_not_put_a_null_data_into_a_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.put(this.password, null, this.categoryName);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | CategoryNotFoundException | DataAlreadyPutException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null data was put into a category.");
    }

    public void we_can_not_put_a_data_into_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.put(this.password, this.data, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | CategoryNotFoundException | DataAlreadyPutException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data was put into a null category.");
    }

    public void we_can_not_put_a_data_into_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.put(this.password, this.data, "not_exists");
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | DataAlreadyPutException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data was put into a category that doesn't exist.");
    }

    public void we_can_not_put_the_same_data_twice_into_any_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.createCategory(this.categoryName + "2", this.password);
        } catch (UnauthorizedAccessException | CategoryAlreadyExistsException e) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.put(this.password, this.data, this.categoryName);
            this.dataBoard.put(this.password, this.data, this.categoryName + "2");
        } catch (DataAlreadyPutException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | CategoryNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data was put twice into any category.");
    }

    public void we_can_get_a_data_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        Data data;
        try {
            data = this.dataBoard.get(this.password, this.data);
        } catch (CloneNotSupportedException | UnauthorizedAccessException | DataNotFoundException e) {
            throw new TestException(testName);
        }

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

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        Data data;
        try {
            data = this.dataBoard.get(this.password, this.data);
        } catch (CloneNotSupportedException | UnauthorizedAccessException | DataNotFoundException e) {
            throw new TestException(testName);
        }

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

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.get("0000", this.data);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CloneNotSupportedException | DataNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given password it's not valid but a data was get.");
    }

    public void we_can_not_get_a_null_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.get(this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CloneNotSupportedException | UnauthorizedAccessException | DataNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null data was get.");
    }

    public void we_can_not_get_a_data_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName) || this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.get(this.password, this.data);
        } catch (DataNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CloneNotSupportedException | UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A data that doesn't exists was get.");
    }

    public void we_can_remove_a_data_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.remove(this.password, this.data);
        } catch (UnauthorizedAccessException | DataNotFoundException e) {
            throw new TestException(testName);
        }

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

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.remove("0000", this.data);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (DataNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given password it's not valid but the data was removed.");
    }

    public void we_can_not_remove_a_null_data()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.remove(this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | DataNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "A null data was removed.");
    }

    public void we_can_not_remove_a_data_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeAll();

        if (!this.dataBoard.hasCategory(this.categoryName)) {
            throw new TestException(testName);
        }

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

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        Data data;
        try {
            data = this.dataBoard.remove(this.password, this.data);

            if (this.data.equals(data)) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
            }
        } catch (UnauthorizedAccessException | DataNotFoundException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "Can't get the data removed.");
    }

    public void we_can_get_the_list_of_data_of_a_category_with_a_valid_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        List<Data> dataList;
        try {
            dataList = this.dataBoard.getDataCategory(this.password, this.categoryName);

            if (dataList.contains(this.data)) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
            }
        } catch (UnauthorizedAccessException | CategoryNotFoundException | CloneNotSupportedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "Can't get the list of data of a category.");
    }

    public void we_can_not_get_the_list_of_data_of_a_category_with_a_wrong_password()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.getDataCategory("0000", this.categoryName);
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (CategoryNotFoundException | CloneNotSupportedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given password it's not valid but the data list was got.");
    }

    public void we_can_not_get_the_list_of_data_of_a_null_category()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.getDataCategory(this.password, null);
        } catch (NullPointerException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | CategoryNotFoundException | CloneNotSupportedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The data list of a null category was got.");
    }

    public void we_can_not_get_the_list_of_data_of_a_category_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeGetOrRemove();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.getDataCategory(this.password, "not_exists");
        } catch (CategoryNotFoundException e) {
            AbstractTest.printSuccess(testName);

            this.afterAll();

            return;
        } catch (UnauthorizedAccessException | CloneNotSupportedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The data list of a category that doesn't exist was got.");
    }

    public void we_can_insert_a_like_with_a_valid_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.insertLike(this.friend.getName(), this.data);
        } catch (DataNotFoundException | UnauthorizedAccessException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        List<User> list;
        try {
            list = this.dataBoard.getLikes(this.data);
        } catch (DataNotFoundException e) {
            throw new TestException(testName);
        }

        for (User tmp : list) {
            if (tmp.getName().equals(this.friend.getName())) {
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

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.insertLike("tony", this.data);
        } catch (UnauthorizedAccessException e) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
        } catch (DataNotFoundException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given user has not access to data but can insert a like.");
    }

    public void we_can_not_insert_a_like_twice_for_the_same_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

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

        if (!this.dataBoard.hasCategory(this.categoryName) || !this.dataBoard.hasData(this.data)) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.insertLike(this.friend.getName(), null);
        } catch (NullPointerException e) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
        } catch (DataNotFoundException | UnauthorizedAccessException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given user inserted a like in a null data.");
    }

    public void we_can_not_insert_a_like_into_a_data_that_doesnt_exist()
    {
        String testName = AbstractTest.getCurrentMethodName();

        this.beforeLike();

        try {
            this.dataBoard.insertLike(this.friend.getName(), new MyData(2020));
        } catch (DataNotFoundException e) {
                AbstractTest.printSuccess(testName);

                this.afterAll();

                return;
        } catch (UnauthorizedAccessException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "The given user inserted a like in a data that doesn't exist.");
    }
}
