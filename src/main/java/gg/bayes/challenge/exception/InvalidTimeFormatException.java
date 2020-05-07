package gg.bayes.challenge.exception;

public class InvalidTimeFormatException extends RuntimeException{

    public static final String MESSAGE = "Time format must be -> hh:mm:ss.SSS";

    public InvalidTimeFormatException() {
        super(MESSAGE);
    }
}
