package edu.ncsu.csc326.coffee_maker.exception;

/**
 * Custom exception for handling conflicts, such as duplicate ingredients.
 */
public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * constructor for the exception
     * @param message the message displayed with the error
     */
    public ConflictException(String message) {
        super(message);
    }
}