/**
 * Represents an input error that Lumi can explain to the user and recover from.
 */
public class LumiException extends Exception {
    /**
     * Creates an exception with a user-friendly explanation of the input error.
     *
     * @param message explanation shown to the user
     */
    public LumiException(String message) {
        super(message);
    }
}
