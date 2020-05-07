package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        final Long matchId = matchService.ingestMatch(payload);
        return ResponseEntity.ok(matchId);
    }

    @GetMapping("{matchId}")
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") @Positive Long matchId) {

        List<HeroKills> heroKillsList  = matchService.getHeroKills(matchId);
        return ResponseEntity.ok(heroKillsList);
    }

    @GetMapping("{matchId}/{heroName}/items")
    public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") @Positive Long matchId,
                                                    @PathVariable("heroName") String heroName) {

        List<HeroItems> heroItemsList = matchService.getHeroItemsList(heroName, matchId);
        return ResponseEntity.ok(heroItemsList);

    }

    @GetMapping("{matchId}/{heroName}/spells")
    public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") @Positive Long matchId,
                                                      @PathVariable("heroName") String heroName) {

        List<HeroSpells> heroSpellsList = matchService.getHeroSpellsList(heroName, matchId);
        return ResponseEntity.ok(heroSpellsList);
    }

    @GetMapping("{matchId}/{heroName}/damage")
    public ResponseEntity<List<HeroDamage>> getDamage(@PathVariable("matchId") @Positive Long matchId,
                                                      @PathVariable("heroName") String heroName) {

        List<HeroDamage> heroDamageList = matchService.getHeroDamageList(heroName, matchId);
        return ResponseEntity.ok(heroDamageList);
    }
}
