package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistence.model.*;
import gg.bayes.challenge.persistence.repository.*;
import gg.bayes.challenge.service.EntityMapper;
import gg.bayes.challenge.service.MatchPersistenceService;
import gg.bayes.challenge.service.model.HeroDamageAction;
import gg.bayes.challenge.service.model.HeroItemsAction;
import gg.bayes.challenge.service.model.HeroKillsAction;
import gg.bayes.challenge.service.model.HeroSpellsAction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MatchPersistenceServiceImpl implements MatchPersistenceService {

    MatchRepository matchRepository;
    HeroItemsRepository heroItemsRepository;
    HeroRepository heroRepository;
    HeroKillsRepository heroKillsRepository;
    HeroDamageRepository heroDamageRepository;
    HeroSpellsRepository heroSpellsRepository;
    EntityMapper entityMapper;

    @Override
    @Transactional
    public Long persistMatch(List<HeroItemsAction> heroItemsActions, List<HeroSpellsAction> heroSpellsActions, List<HeroDamageAction> heroDamageActions, List<HeroKillsAction> heroKillsActions, String payload) {

        log.info("starting to persist match payload");
        // check if exists
        int hash = payload.hashCode();
        MatchEntity matchEntity = matchRepository.findByHash(hash);

        if (matchEntity == null) {
            matchEntity = saveMatchEntity(hash, payload);
        }
        final Long matchId = matchEntity.getId();

        // hero items actions
        heroItemsActions.stream()
                .map(heroItemsAction -> {
                    HeroEntity heroEntity = getOrCreateHero(heroItemsAction.getHeroName());
                    return entityMapper.buildHeroItemsEntity(heroItemsAction, matchId, heroEntity.getId());
                })
                .forEach(heroItemsRepository::save);

        log.info("{} hero items actions saved", heroItemsActions.size());

        // hero spells actions
        Map<String, Map<String, Long>> groupedSpellCountsByHero = heroSpellsActions.stream()
                .collect(Collectors.groupingBy(HeroSpellsAction::getHero,
                        Collectors.groupingBy(HeroSpellsAction::getSpell, Collectors.counting()))
                );

        groupedSpellCountsByHero
                .forEach((hero, spellCountMap) -> spellCountMap
                        .forEach((spell, count) -> persistHeroSpells(matchId, hero, spell, count)));


        // hero damage actions
        Map<String, Map<String, LongSummaryStatistics>> groupedDamageCountsByHero = heroDamageActions.stream()
                .collect(Collectors.groupingBy(HeroDamageAction::getHeroName,
                        Collectors.groupingBy(HeroDamageAction::getTarget, Collectors.summarizingLong(HeroDamageAction::getDamage)))
                );

        groupedDamageCountsByHero
                .forEach((hero, damageCountMap) -> damageCountMap
                        .forEach((target, summaryStatistics) ->
                                persistHeroDamage(matchId, hero, target, summaryStatistics)
                        )
                );

        // hero kill counts
        Map<String, Long> heroKillCountMap = heroKillsActions.stream()
                .collect(Collectors
                        .groupingBy(HeroKillsAction::getHero, Collectors.counting()));

        heroKillCountMap.forEach((hero, killCount) -> persistHeroKills(matchId, hero, killCount));

        log.info("finished persisting match payload");

        return matchId;
    }

    private void persistHeroSpells(Long matchId, String hero, String spell, Long count) {
        HeroEntity heroEntity = getOrCreateHero(hero);
        HeroSpellsEntity heroSpellsEntity = entityMapper.buildHeroSpellsEntity(heroEntity.getId(), spell, count, matchId);
        log.debug("going to persist hero spells entity: {}", heroSpellsEntity);
        heroSpellsRepository.save(heroSpellsEntity);
        log.debug("hero spells entity persisted: {}", heroSpellsEntity);
    }

    private void persistHeroKills(Long matchId, String hero, Long killCount) {
        HeroEntity heroEntity = getOrCreateHero(hero);
        HeroKillsEntity heroKillsEntity = entityMapper.buildHeroKillsEntity(heroEntity.getId(), killCount, matchId);

        log.debug("going to persist hero kills entity: {}", heroKillsEntity);
        heroKillsRepository.save(heroKillsEntity);
        log.debug("hero kills entity persisted: {}", heroKillsEntity);
    }

    private void persistHeroDamage(Long matchId, String hero, String target, LongSummaryStatistics summaryStatistics) {
        Long count = summaryStatistics.getCount();
        Long totalDamage = summaryStatistics.getSum();
        HeroEntity heroEntity = getOrCreateHero(hero);

        HeroDamageEntity heroDamageEntity = entityMapper.buildHeroDamageEntity(heroEntity.getId(), target, count, totalDamage, matchId);

        log.debug("going to persist hero damage entity: {}", heroDamageEntity);
        heroDamageRepository.save(heroDamageEntity);
        log.debug("hero damage entity persisted: {}", heroDamageEntity);
    }


    private HeroEntity getOrCreateHero(String heroName) {
        HeroEntity heroEntity;
        Optional<HeroEntity> heroEntityOptional = heroRepository.findByName(heroName);
        if (heroEntityOptional.isPresent()) {
            heroEntity = heroEntityOptional.get();
        } else {
            heroEntity = new HeroEntity();
            heroEntity.setName(heroName);
            log.info("going to create new hero: {}", heroName);
            heroEntity = heroRepository.save(heroEntity);
            log.info("created new hero: {}", heroEntity);
        }
        return heroEntity;
    }

    private MatchEntity saveMatchEntity(int hash, String payload) {
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setHash(hash);
        matchEntity.setPayload(payload);

        return matchRepository.save(matchEntity);
    }
}
