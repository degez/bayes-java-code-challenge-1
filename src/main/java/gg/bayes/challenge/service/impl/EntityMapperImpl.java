package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.HeroDamageEntity;
import gg.bayes.challenge.persistence.model.HeroItemsEntity;
import gg.bayes.challenge.persistence.model.HeroKillsEntity;
import gg.bayes.challenge.persistence.model.HeroSpellsEntity;
import gg.bayes.challenge.service.EntityMapper;
import gg.bayes.challenge.service.model.HeroItemsAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EntityMapperImpl implements EntityMapper {

    @Override
    public HeroKillsEntity buildHeroKillsEntity(Long heroId, Long killCount, Long matchId) {

        HeroKillsEntity heroKillsEntity = new HeroKillsEntity();

        heroKillsEntity.setKills(killCount.intValue());
        heroKillsEntity.setMatchId(matchId);
        heroKillsEntity.setHeroId(heroId);
        log.debug("hero kills entity built: {}", heroKillsEntity);

        return heroKillsEntity;
    }

    @Override
    public HeroDamageEntity buildHeroDamageEntity(Long heroId, String target, Long killCount, Long totalDamage, Long matchId) {
        HeroDamageEntity heroDamageEntity = new HeroDamageEntity();

        heroDamageEntity.setDamageInstances(killCount.intValue());
        heroDamageEntity.setMatchId(matchId);
        heroDamageEntity.setTarget(target);
        heroDamageEntity.setHeroId(heroId);
        heroDamageEntity.setTotalDamage(totalDamage.intValue());
        log.debug("hero damage entity built: {}", heroDamageEntity);

        return heroDamageEntity;
    }

    @Override
    public HeroSpellsEntity buildHeroSpellsEntity(Long heroId, String spellName, Long count, Long matchId) {
        HeroSpellsEntity heroSpellsEntity = new HeroSpellsEntity();

        heroSpellsEntity.setCasts(count.intValue());
        heroSpellsEntity.setHeroId(heroId);
        heroSpellsEntity.setMatchId(matchId);
        heroSpellsEntity.setSpell(spellName);
        log.debug("hero spells entity built: {}", heroSpellsEntity);

        return heroSpellsEntity;
    }

    @Override
    public HeroItemsEntity buildHeroItemsEntity(HeroItemsAction heroItemsAction, Long matchId, Long heroId) {
        HeroItemsEntity heroItemsEntity = new HeroItemsEntity();
        BeanUtils.copyProperties(heroItemsAction, heroItemsEntity);

        heroItemsEntity.setMatchId(matchId);
        heroItemsEntity.setHeroId(heroId);

        log.debug("hero items entity built: {}", heroItemsEntity);
        return heroItemsEntity;
    }

}
