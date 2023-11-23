package ru.java.fun;

/**
 * Exception on execute command.
 */
public class ExecutionException extends RuntimeException {
    public ExecutionException(String message) {
        super(message);
    }
}
