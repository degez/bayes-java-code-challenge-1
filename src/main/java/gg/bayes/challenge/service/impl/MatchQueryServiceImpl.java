package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.exception.*;
import gg.bayes.challenge.persistence.model.HeroEntity;
import gg.bayes.challenge.persistence.model.HeroKillsEntity;
import gg.bayes.challenge.persistence.repository.*;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MatchQueryServiceImpl implements MatchQueryService {
    HeroItemsRepository heroItemsRepository;
    HeroRepository heroRepository;
    HeroKillsRepository heroKillsRepository;
    HeroDamageRepository heroDamageRepository;
    HeroSpellsRepository heroSpellsRepository;

    @Override
    public List<HeroKills> getHeroKills(Long matchId) {
        List<HeroKillsEntity> heroKillsEntities = heroKillsRepository.findByMatchId(matchId)
                .orElseThrow(HeroKillsNotFoundException::new);

        return heroKillsEntities.stream().map(heroKillsEntity -> {
            HeroKills heroKills = new HeroKills();
            BeanUtils.copyProperties(heroKillsEntity, heroKills);

            Optional<HeroEntity> heroEntityOptional = heroRepository.findById(heroKillsEntity.getHeroId());
            heroEntityOptional.ifPresent(heroEntity -> heroKills.setHero(heroEntity.getName()));

            return heroKills;
        }).collect(Collectors.toList());
    }

    @Override
    public List<HeroItems> getHeroItemsList(String heroName, Long matchId) {

        HeroEntity heroEntity = heroRepository.findByName(heroName)
                .orElseThrow(HeroNotFoundException::new);

        return heroItemsRepository.findByHeroIdAndMatchId(heroEntity.getId(), matchId)
                .map(list -> list.stream().map(heroItemsEntity -> {
                    HeroItems heroItems = new HeroItems();
                    BeanUtils.copyProperties(heroItemsEntity, heroItems);

                    return heroItems;
                }).collect(Collectors.toList()))
                .orElseThrow(HeroItemsNotFoundException::new);
    }

    @Override
    public List<HeroSpells> getHeroSpellsList(String heroName, Long matchId) {
        HeroEntity heroEntity = heroRepository.findByName(heroName)
                .orElseThrow(HeroNotFoundException::new);

        return heroSpellsRepository.findByHeroIdAndMatchId(heroEntity.getId(), matchId)
                .map(list -> list.stream().map(heroSpellsEntity -> {
                    HeroSpells heroSpells = new HeroSpells();
                    BeanUtils.copyProperties(heroSpellsEntity, heroSpells);

                    return heroSpells;
                }).collect(Collectors.toList()))
                .orElseThrow(HeroSpellsNotFoundException::new);
    }

    @Override
    public List<HeroDamage> getHeroDamageList(String heroName, Long matchId) {
        HeroEntity heroEntity = heroRepository.findByName(heroName)
                .orElseThrow(HeroNotFoundException::new);

        return heroDamageRepository.findByHeroIdAndMatchId(heroEntity.getId(), matchId)
                .map(list -> list.stream().map(heroDamageEntity -> {
                    HeroDamage heroDamage = new HeroDamage();
                    BeanUtils.copyProperties(heroDamageEntity, heroDamage);

                    return heroDamage;
                }).collect(Collectors.toList()))
                .orElseThrow(HeroDamageNotFoundException::new);
    }
}
