package tests;

import interfaces.DataBoard;
import org.jetbrains.annotations.NotNull;

public class AbstractTest<E extends DataBoard<?>> {
    protected E dataBoard;
    protected String password;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public AbstractTest(E dataBoard, String password) {
        this.dataBoard = dataBoard;
        this.password = password;
    }

    // Stampa una stringa che indica il passaggio
    // del test passato come parametro
    protected static void printSuccess(String testName) {
        System.out.println("[x] " + testName + " " + ANSI_GREEN + "test passed" + ANSI_RESET + ".");
    }

    // ritorna il nome del metodo
    // che ha invocato getMethodName
    protected static @NotNull
    String getCurrentMethodName()
    {
        return new Throwable()
                .getStackTrace()[1]
                .getMethodName();
    }
}
