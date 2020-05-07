package gg.bayes.challenge.exception;

public class HeroItemsNotFoundException extends RuntimeException{

    public static final String MESSAGE = "Hero items not found for provided match";

    public HeroItemsNotFoundException() {
        super(MESSAGE);
    }
}
