package gg.bayes.challenge.exception;

public class HeroDamageNotFoundException extends RuntimeException{

    public static final String MESSAGE = "Hero damage not found for provided match";

    public HeroDamageNotFoundException() {
        super(MESSAGE);
    }
}
