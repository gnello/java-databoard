import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyUser;

public class Main {

    public static void main (String [] args) {
        String password = "1234";
        User owner = new MyUser("Jon Doe", password);

        MyDataBoard1<Data> myDataBoard1 = new MyDataBoard1<>(owner);

        DataBoardTest<DataBoard<Data>> dataBoardTests1 = new DataBoardTest<>(myDataBoard1, password);
        dataBoardTests1.run();
    }

}
