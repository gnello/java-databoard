package tests;

import exceptions.*;
import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyData;
import models.MyUser;

import java.util.Iterator;
import java.util.List;

public class IteratorTest<E extends DataBoard<Data>> extends AbstractTest<E> {
    private final User friend;

    // Assegna dataBoard e password,
    // inizializza friend
    public IteratorTest(E dataBoard, String password) {
        super(dataBoard, password);

        this.friend = new MyUser("tony");
    }

    public void we_can_get_a_iterator_with_a_valid_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        Iterator<Data> iterator;
        try {
            iterator = this.dataBoard.getIterator(this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        if (iterator != null) {
            AbstractTest.printSuccess(testName);

            return;
        }

        throw new TestException(testName, "Can't get an iterator.");
    }

    public void we_can_not_get_a_iterator_with_a_wrong_user()
    {
        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.getIterator("0000");
        } catch (UnauthorizedAccessException e) {
            AbstractTest.printSuccess(testName);

            return;
        }

        throw new TestException(testName, "The password it's invalid but we get an iterator.");
    }

    public void we_can_get_a_iterator_ordered_by_likes_desc()
    {
        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.createCategory("test", this.password);
        } catch (UnauthorizedAccessException | CategoryAlreadyExistsException e) {
            throw new TestException(testName);
        }

        try {
            this.dataBoard.addFriend("test", this.password, "luca");
            this.dataBoard.addFriend("test", this.password, "matteo");
            this.dataBoard.addFriend("test", this.password, "gabriele");
        } catch (UnauthorizedAccessException | CategoryNotFoundException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        MyData dato1 = new MyData(1);
        MyData dato2 = new MyData(2);
        MyData dato3 = new MyData(3);
        MyData dato4 = new MyData(4);

        try {
            this.dataBoard.put(this.password, dato1, "test");
            this.dataBoard.put(this.password, dato2, "test");
            this.dataBoard.put(this.password, dato3, "test");
            this.dataBoard.put(this.password, dato4, "test");
        } catch (UnauthorizedAccessException | CategoryNotFoundException | DataAlreadyPutException
                | CloneNotSupportedException e) {
            throw new TestException(testName);
        }

        try {
            //expected order: dato2, dato3, dato1, dato4
            this.dataBoard.insertLike("luca", dato1);

            this.dataBoard.insertLike("luca", dato2);
            this.dataBoard.insertLike("matteo", dato2);
            this.dataBoard.insertLike("gabriele", dato2);

            this.dataBoard.insertLike("matteo", dato3);
            this.dataBoard.insertLike("gabriele", dato3);

        } catch (UnauthorizedAccessException | DataNotFoundException | FriendAlreadyAddedException e) {
            throw new TestException(testName);
        }

        try {
            Iterator<Data> iterator = this.dataBoard.getIterator(this.password);

            //expected order: dato2, dato3, dato1, dato4
            if (iterator.next().equals(dato2)
                    && iterator.next().equals(dato3)
                    && iterator.next().equals(dato1)
                    && iterator.next().equals(dato4)) {
                AbstractTest.printSuccess(testName);

                try {
                    this.dataBoard.removeCategory("test", this.password);
                } catch (UnauthorizedAccessException | CategoryNotFoundException e) {
                    throw new TestException(testName);
                }

                return;
            }
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName);
        }

        throw new TestException(testName, "We can't get an iterator ordered by likes desc.");
    }

    public void we_can_get_a_iterator_that_doesnt_support_remove_method()
    {
        String testName = AbstractTest.getCurrentMethodName();

        try {
            this.dataBoard.createCategory("test", this.password);
        } catch (UnauthorizedAccessException | CategoryAlreadyExistsException e) {
            throw new TestException(testName + e);
        }

        try {
            this.dataBoard.put(this.password, new MyData(1), "test");
        } catch (UnauthorizedAccessException | CategoryNotFoundException | DataAlreadyPutException
                | CloneNotSupportedException e) {
            throw new TestException(testName + e);
        }

        Iterator<Data> iterator;

        try {
            iterator = this.dataBoard.getIterator(this.password);
        } catch (UnauthorizedAccessException e) {
            throw new TestException(testName + e);
        }

        try {
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            AbstractTest.printSuccess(testName);

            return;
        }

        throw new TestException(testName, "We can remove elements from an iterator.");
    }


}
