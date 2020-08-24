import interfaces.Data;
import interfaces.DataBoard;
import interfaces.User;
import models.MyUser;

public class Main {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main (String [] args) {
        String password = "1234";
        User owner = new MyUser("Jon Doe", password);

        MyDataBoard1<Data> myDataBoard1 = new MyDataBoard1<>(owner);

        DataBoardTest<DataBoard<Data>> dataBoardTests1 = new DataBoardTest<>(myDataBoard1, password);

        System.out.println(ANSI_CYAN + "\nStarting ArrayList implementation tests..." + ANSI_RESET);
        dataBoardTests1.run();

        System.out.println(ANSI_CYAN + "\nStarting TreeSet implementation tests..." + ANSI_RESET);
    }

}
