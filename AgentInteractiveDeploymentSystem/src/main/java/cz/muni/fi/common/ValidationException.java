package cz.muni.fi.common;

/**
 * Created by Matúš on 27.3.2017.
 */
public class ValidationException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>ValidationException</code> without detail message.
     */
    public ValidationException() {
    }

    /**
     * Constructs an instance of
     * <code>ValidationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ValidationException(String msg) {
        super(msg);
    }
}

