package gg.bayes.challenge.service;

import gg.bayes.challenge.service.model.HeroDamageAction;
import gg.bayes.challenge.service.model.HeroItemsAction;
import gg.bayes.challenge.service.model.HeroKillsAction;
import gg.bayes.challenge.service.model.HeroSpellsAction;

import java.util.List;

public interface MatchPersistenceService {

    Long persistMatch(List<HeroItemsAction> heroItemsActions, List<HeroSpellsAction> heroSpellsActions, List<HeroDamageAction> heroDamageActions, List<HeroKillsAction> heroKillsActions, String payload);
}
