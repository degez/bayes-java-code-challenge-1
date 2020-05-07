package gg.bayes.challenge.exception;

public class HeroNotFoundException extends RuntimeException{

    public static final String MESSAGE = "Hero not found for provided match";

    public HeroNotFoundException() {
        super(MESSAGE);
    }
}
