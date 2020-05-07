package gg.bayes.challenge.service;

import gg.bayes.challenge.service.model.HeroDamageAction;
import gg.bayes.challenge.service.model.HeroItemsAction;
import gg.bayes.challenge.service.model.HeroKillsAction;
import gg.bayes.challenge.service.model.HeroSpellsAction;

public interface CombatLogLineProcessor {

    HeroItemsAction extractHeroItemAction(String line);
    HeroDamageAction extractHeroDamageAction(String line);
    HeroKillsAction extractHeroKillsAction(String line);
    HeroSpellsAction extractHeroSpellsAction(String line);

}
