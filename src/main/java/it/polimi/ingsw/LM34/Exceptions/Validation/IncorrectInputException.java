package it.polimi.ingsw.LM34.Exceptions.Validation;

/**
 * {@link it.polimi.ingsw.LM34.Utils.Validator} have multiple methods that thrown this exception
 * This exception is used both client-side and server-side in order to verify the input from players
 */
public class IncorrectInputException extends Exception {
    private static final long serialVersionUID = 1023807304463660592L;

    public IncorrectInputException() {
        super("Incorrect input");
    }
}
