package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.service.CombatLogService;
import gg.bayes.challenge.service.model.ActionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CombatLogServiceImpl implements CombatLogService {

    // if we want to count creep etc kills as well:
//    @Value("${prefix.to.kill:killed by npc_dota_hero_}")
//    private String prefixToKill;
    @Value("${prefix.to.remove.hero:npc_dota_hero_}")
    private String prefixToRemoveHero;

    @Override
    public Map<ActionType, List<String>> groupActionsAsLine(String battleLog) {

        return Arrays.stream(battleLog.split(System.lineSeparator()))
                .collect(Collectors.groupingBy(s -> {
                    if (StringUtils.contains(s, ActionType.BUY.getAction())) {
                        return ActionType.BUY;
                    } else if (StringUtils.contains(s, ActionType.CAST.getAction())) {
                        return ActionType.CAST;
                    } else if (StringUtils.contains(s, ActionType.HIT.getAction())) {
                        return ActionType.HIT;
                    } else if (StringUtils.contains(s, ActionType.KILLED_BY.getAction())
                            && StringUtils.countMatches(s, prefixToRemoveHero) == 2) {
                        return ActionType.KILLED_BY;
                    } else {
                        return ActionType.NOT_USED;
                    }
                }));
    }
}
