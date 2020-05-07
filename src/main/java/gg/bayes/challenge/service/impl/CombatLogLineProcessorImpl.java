package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.exception.InvalidTimeFormatException;
import gg.bayes.challenge.service.CombatLogLineProcessor;
import gg.bayes.challenge.service.model.HeroDamageAction;
import gg.bayes.challenge.service.model.HeroItemsAction;
import gg.bayes.challenge.service.model.HeroKillsAction;
import gg.bayes.challenge.service.model.HeroSpellsAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class CombatLogLineProcessorImpl implements CombatLogLineProcessor {

    @Value("${prefix.to.remove.hero:npc_dota_hero_}")
    private String prefixToRemoveHero;
    @Value("${prefix.to.remove.item:item_}")
    private String prefixToRemoveItem;
    @Value("${input.bulk.index.time:0}")
    private Integer timeIndex;
    @Value("${input.bulk.index.hero:1}")
    private Integer heroNameIndex;
    @Value("${input.bulk.index.item:4}")
    private Integer itemIndex;
    @Value("${input.bulk.index.target:3}")
    private Integer targetIndex;
    @Value("${input.bulk.index.damage:7}")
    private Integer damageIndex;
    @Value("${input.bulk.index.kill:5}")
    private Integer killIndex;
    @Value("${input.bulk.index.spell:4}")
    private Integer spellIndex;

    @Autowired
    SimpleDateFormat simpleDateFormat;

    @Override
    public HeroItemsAction extractHeroItemAction(String line) {

        HeroItemsAction heroItemsAction = new HeroItemsAction();

        String[] lineParts = StringUtils.split(line);

        String timeRaw = lineParts[timeIndex];
        String heroNameRaw = lineParts[heroNameIndex];
        String itemRaw = lineParts[itemIndex];

        Long time = convertTimeValue(timeRaw);
        String heroName = StringUtils.removeStart(heroNameRaw, prefixToRemoveHero);
        String item = StringUtils.removeStart(itemRaw, prefixToRemoveItem);

        heroItemsAction.setTimestamp(time);
        heroItemsAction.setHeroName(heroName);
        heroItemsAction.setItem(item);

        log.info("extracted hero item: {}", heroItemsAction);
        return heroItemsAction;
    }

    @Override
    public HeroDamageAction extractHeroDamageAction(String line) {
        HeroDamageAction heroDamageAction = new HeroDamageAction();

        String[] lineParts = StringUtils.split(line);

        String heroNameRaw = lineParts[heroNameIndex];
        String targetRaw = lineParts[targetIndex];
        String damageRaw = lineParts[damageIndex];

        String heroName = StringUtils.removeStart(heroNameRaw, prefixToRemoveHero);
        String target = StringUtils.removeStart(targetRaw, prefixToRemoveHero);
        Integer damage = Integer.valueOf(damageRaw);

        heroDamageAction.setDamage(damage);
        heroDamageAction.setHeroName(heroName);
        heroDamageAction.setTarget(target);

        log.info("extracted hero damage action: {}", heroDamageAction);

        return heroDamageAction;
    }

    @Override
    public HeroKillsAction extractHeroKillsAction(String line) {
        HeroKillsAction heroKillsAction = new HeroKillsAction();

        String[] lineParts = StringUtils.split(line);

        String killerHeroNameRaw = lineParts[killIndex];
        String heroName = StringUtils.removeStart(killerHeroNameRaw, prefixToRemoveHero);
        heroKillsAction.setHero(heroName);

        log.info("extracted hero kills action: {}", heroKillsAction);

        return heroKillsAction;
    }

    @Override
    public HeroSpellsAction extractHeroSpellsAction(String line) {
        HeroSpellsAction heroSpellsAction = new HeroSpellsAction();
        String[] lineParts = StringUtils.split(line);

        String heroNameRaw = lineParts[heroNameIndex];
        String spellNameRaw = lineParts[spellIndex];

        String heroName = StringUtils.removeStart(heroNameRaw, prefixToRemoveHero);
        String spellName = StringUtils.removeStart(spellNameRaw, heroName + "_");

        heroSpellsAction.setHero(heroName);
        heroSpellsAction.setSpell(spellName);

        log.info("extracted hero spells action: {}", heroSpellsAction);
        return heroSpellsAction;
    }

    private Long convertTimeValue(String timeRaw) {
        Timestamp time = null;

        timeRaw = StringUtils.removeEnd(timeRaw, "]");
        timeRaw = StringUtils.removeStart(timeRaw, "[");

        try {
            time = new Timestamp(simpleDateFormat.parse(timeRaw).getTime());
        } catch (ParseException e) {
            throw new InvalidTimeFormatException();
        }

        return time.getTime();
    }
}
