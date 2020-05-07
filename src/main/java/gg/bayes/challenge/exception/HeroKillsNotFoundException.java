package gg.bayes.challenge.exception;

public class HeroKillsNotFoundException extends RuntimeException{

    public static final String MESSAGE = "Hero kills not found for provided match";

    public HeroKillsNotFoundException() {
        super(MESSAGE);
    }
}
