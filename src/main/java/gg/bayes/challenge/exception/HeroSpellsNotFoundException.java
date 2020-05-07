package gg.bayes.challenge.exception;

public class HeroSpellsNotFoundException extends RuntimeException{

    public static final String MESSAGE = "Hero spells not found for provided match";

    public HeroSpellsNotFoundException() {
        super(MESSAGE);
    }
}
