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

        // istanzia le due implementazioni dell'interfaccia DataBoard
        MyDataBoardArrayList<Data> myDataBoardArrayList = new MyDataBoardArrayList<>(owner);
        MyDataBoardTreeSet<Data> myDataBoardTreeSet = new MyDataBoardTreeSet<>(owner);

        // crea un'istanza della suite di test per ogni implementazione di DataBoard
        DataBoardTest<DataBoard<Data>> dataBoardTestsArrayListImplementation = new DataBoardTest<>(myDataBoardArrayList, password);
        DataBoardTest<DataBoard<Data>> dataBoardTestsTreeSetImplementation = new DataBoardTest<>(myDataBoardTreeSet, password);

        // esegui i test
        System.out.println(ANSI_CYAN + "\nStarting ArrayList implementation tests..." + ANSI_RESET);
        dataBoardTestsArrayListImplementation.run();

        System.out.println(ANSI_CYAN + "\nStarting TreeSet implementation tests..." + ANSI_RESET);
        dataBoardTestsTreeSetImplementation.run();
    }

}
