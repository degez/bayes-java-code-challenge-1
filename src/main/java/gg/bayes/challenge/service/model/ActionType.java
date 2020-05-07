package gg.bayes.challenge.service.model;

import lombok.Getter;

@Getter
public enum ActionType {
    BUY("buys"),
    KILLED_BY("killed"),
    CAST("casts"),
    HIT("hits"),
    NOT_USED("not_used");

    private final String action;

    ActionType(String action) {
        this.action = action;
    }
}
