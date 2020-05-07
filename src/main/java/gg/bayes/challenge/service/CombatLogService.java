package gg.bayes.challenge.service;

import gg.bayes.challenge.service.model.ActionType;

import java.util.List;
import java.util.Map;

public interface CombatLogService {

    Map<ActionType, List<String>> groupActionsAsLine(String battleLog);
}
