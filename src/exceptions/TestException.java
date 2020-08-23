package exceptions;

public class TestException extends RuntimeException {
    public TestException(String testName, String exceptionMessage) {
        super ("[ ] " + testName + " test failed: " + exceptionMessage);
    }

    public TestException(String testName) {
        super ("[ ] " + testName + " test failed: Invalid test configuration.");
    }
}
