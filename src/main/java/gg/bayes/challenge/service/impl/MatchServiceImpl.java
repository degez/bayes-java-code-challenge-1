package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.*;
import gg.bayes.challenge.service.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MatchServiceImpl implements MatchService {

    CombatLogService combatLogService;
    CombatLogLineProcessor combatLogLineProcessor;
    MatchPersistenceService matchPersistenceService;
    MatchQueryService matchQueryService;

    @Override
    public Long ingestMatch(String payload) {

        log.trace(payload);

        Map<ActionType, List<String>> actionTypeGroupedLines = combatLogService.groupActionsAsLine(payload);
        log.info("action type distinct count: {}", actionTypeGroupedLines.size());

        List<HeroItemsAction> heroItemsActions = actionTypeGroupedLines
                .getOrDefault(ActionType.BUY, new ArrayList<>())
                .stream()
                .map(combatLogLineProcessor::extractHeroItemAction)
                .collect(Collectors.toList());

        List<HeroSpellsAction> heroSpellsActions = actionTypeGroupedLines
                .getOrDefault(ActionType.CAST, new ArrayList<>())
                .stream()
                .map(combatLogLineProcessor::extractHeroSpellsAction)
                .collect(Collectors.toList());

        List<HeroDamageAction> heroDamageActions = actionTypeGroupedLines
                .getOrDefault(ActionType.HIT, new ArrayList<>())
                .stream()
                .map(combatLogLineProcessor::extractHeroDamageAction)
                .collect(Collectors.toList());

        List<HeroKillsAction> heroKillsActions = actionTypeGroupedLines
                .getOrDefault(ActionType.KILLED_BY, new ArrayList<>())
                .stream()
                .map(combatLogLineProcessor::extractHeroKillsAction)
                .collect(Collectors.toList());

        log.info("prepared all action lists, passing them for persisting...");
        Long matchId = matchPersistenceService.persistMatch(heroItemsActions,
                heroSpellsActions,
                heroDamageActions,
                heroKillsActions,
                payload);
        log.info("all filtered log entries persisted");
        return matchId;
    }

    @Override
    public List<HeroKills> getHeroKills(Long matchId) {
        return matchQueryService.getHeroKills(matchId);
    }

    @Override
    public List<HeroItems> getHeroItemsList(String heroName, Long matchId) {
        return matchQueryService.getHeroItemsList(heroName, matchId);
    }

    @Override
    public List<HeroSpells> getHeroSpellsList(String heroName, Long matchId) {
        return matchQueryService.getHeroSpellsList(heroName, matchId);
    }

    @Override
    public List<HeroDamage> getHeroDamageList(String heroName, Long matchId) {
        return matchQueryService.getHeroDamageList(heroName, matchId);
    }


}
