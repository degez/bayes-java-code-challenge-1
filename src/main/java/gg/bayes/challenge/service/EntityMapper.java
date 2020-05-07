package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.HeroDamageEntity;
import gg.bayes.challenge.persistence.model.HeroItemsEntity;
import gg.bayes.challenge.persistence.model.HeroKillsEntity;
import gg.bayes.challenge.persistence.model.HeroSpellsEntity;
import gg.bayes.challenge.service.model.HeroItemsAction;

public interface EntityMapper {
    HeroKillsEntity buildHeroKillsEntity(Long heroId, Long killCount, Long matchId);

    HeroDamageEntity buildHeroDamageEntity(Long heroId, String target, Long killCount, Long totalDamage, Long matchId);

    HeroSpellsEntity buildHeroSpellsEntity(Long heroId, String spellName, Long count, Long matchId);

    HeroItemsEntity buildHeroItemsEntity(HeroItemsAction heroItemsAction, Long matchId, Long heroId);
}
