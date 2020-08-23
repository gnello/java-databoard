import interfaces.Data;
import interfaces.DataBoard;
import tests.CategoryTest;
import tests.DataTest;
import tests.FriendTest;


public class DataBoardTest<E extends DataBoard<Data>> {
    E dataBoard;
    String password;

    public DataBoardTest(E dataBoard, String password) {
        this.dataBoard = dataBoard;
        this.password = password;
    }

    public void run()
    {
        System.out.println("\nStarting interfaces.DataBoard test suite...\n");

        CategoryTest<E> categoryTest = new CategoryTest<>(this.dataBoard, this.password);
        FriendTest<E> friendTest = new FriendTest<>(this.dataBoard, this.password);
        DataTest<E> dataTest = new DataTest<>(this.dataBoard, this.password);

        // test that...

        // create category
        categoryTest.we_can_create_a_category_with_a_valid_password();
        categoryTest.we_can_not_create_a_category_with_a_wrong_password();
        categoryTest.we_can_not_create_a_category_with_a_null_name();
        categoryTest.we_can_not_create_a_category_that_already_exists();

        // remove category
        categoryTest.we_can_remove_a_category_with_a_valid_password();
        categoryTest.we_can_not_remove_a_category_with_a_wrong_password();
        categoryTest.we_can_not_remove_a_category_with_a_null_name();
        categoryTest.we_can_not_remove_a_category_that_doesnt_exist();

        // add friend
        friendTest.we_can_add_a_friend_to_a_category_with_a_valid_password();
        friendTest.we_can_not_add_a_friend_to_a_category_with_a_wrong_password();
        friendTest.we_can_not_add_a_friend_to_a_null_category();
        friendTest.we_can_not_add_a_null_friend_to_a_category();
        friendTest.we_can_not_add_a_friend_to_a_category_that_doesnt_exist();
        friendTest.we_can_not_add_a_friend_that_was_already_added_to_a_category();

        //remove friend
        friendTest.we_can_remove_a_friend_from_a_category_with_a_valid_password();
        friendTest.we_can_not_remove_a_friend_from_a_category_with_a_wrong_password();
        friendTest.we_can_not_remove_a_friend_from_a_null_category();
        friendTest.we_can_not_remove_a_null_friend_from_a_category();
        friendTest.we_can_not_remove_a_friend_from_a_category_that_doesnt_exist();
        friendTest.we_can_not_remove_a_friend_that_is_not_present_in_category();

        //put data
        dataTest.we_can_put_a_data_into_a_category_with_a_valid_password();
        dataTest.we_can_not_put_a_data_into_a_category_with_a_wrong_password();
        dataTest.we_can_not_put_a_null_data_into_a_category();
        dataTest.we_can_not_put_a_data_into_a_null_category();
        dataTest.we_can_not_put_a_data_into_a_category_that_doesnt_exist();
        dataTest.we_can_not_put_the_same_data_twice_into_any_category();

        //get data
        dataTest.we_can_get_a_data_with_a_valid_password();
        dataTest.we_can_not_get_a_shallow_copy_of_a_data();
        dataTest.we_can_not_get_a_data_with_a_wrong_password();
        dataTest.we_can_not_get_a_null_data();
        dataTest.we_can_not_get_a_data_that_doesnt_exist();

        //remove data
        dataTest.we_can_remove_a_data_with_a_valid_password();
        dataTest.we_can_not_remove_a_data_with_a_wrong_password();
        dataTest.we_can_not_remove_a_null_data();
        dataTest.we_can_not_remove_a_data_that_doesnt_exist();
        dataTest.we_can_get_the_removed_data();

        //get data category
        dataTest.we_can_get_the_list_of_data_of_a_category_with_a_valid_password();
        dataTest.we_can_not_get_the_list_of_data_of_a_category_with_a_wrong_password();
        dataTest.we_can_not_get_the_list_of_data_of_a_null_category();
        dataTest.we_can_not_get_the_list_of_data_of_a_category_that_doesnt_exist();
    }


}
