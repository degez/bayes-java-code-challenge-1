package gg.bayes.challenge.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.bayes.challenge.rest.controller.MatchController;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MatchControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MatchController matchController;
    @MockBean
    MatchService matchService;

    @Test
    public void givenValidMatch_whenSave_thenSuccess() throws Exception {

        String payload = "[00:16:49.041] npc_dota_hero_puck hits npc_dota_hero_bane with dota_unknown for 54 damage (775->721)\n" +
                "[00:16:51.008] npc_dota_badguys_siege is killed by npc_dota_goodguys_tower1_bot\n" +
                "[00:16:51.874] npc_dota_hero_pangolier buys item item_ward_observer";

        when(matchService.ingestMatch(payload))
                .thenReturn(1l);

        // when perform ingest then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/match")
                .content(payload)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidMatch_whenSave_thenBadRequest() throws Exception {

        String payload = "";

        // when perform ingest then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/match")
                .content(payload)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenMatchId_whenGetHeroKills_ThenSuccess() throws Exception {
        Long matchId = 1l;

        List<HeroKills> heroKillsList = new ArrayList<>();


        when(matchService.getHeroKills(1l))
                .thenReturn(heroKillsList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}", matchId))
                .andExpect(status().isOk());
    }


    @Test
    public void givenMatchIdAndHeroName_whenGetHeroSpells_ThenSuccess() throws Exception {
        Long matchId = 1l;
        String heroName = "hero";

        List<HeroSpells> heroSpells = new ArrayList<>();


        when(matchService.getHeroSpellsList(heroName, matchId))
                .thenReturn(heroSpells);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}/{heroName}/spells", matchId, heroName))
                .andExpect(status().isOk());
    }

    @Test
    public void givenMatchIdAndHeroName_whenGetHeroDamage_ThenSuccess() throws Exception {
        Long matchId = 1l;
        String heroName = "hero";

        List<HeroDamage> heroDamages = new ArrayList<>();


        when(matchService.getHeroDamageList(heroName, matchId))
                .thenReturn(heroDamages);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}/{heroName}/damage", matchId, heroName))
                .andExpect(status().isOk());
    }

    @Test
    public void givenMatchIdAndHeroName_whenGetHeroItems_ThenSuccess() throws Exception {
        Long matchId = 1l;
        String heroName = "hero";

        List<HeroItems> heroItemsList = new ArrayList<>();
        HeroItems heroItems = new HeroItems();
        String item = "test";
        heroItems.setItem(item);

        heroItemsList.add(heroItems);

        when(matchService.getHeroItemsList(heroName, matchId))
                .thenReturn(heroItemsList);

        String content = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}/{heroName}/items", matchId, heroName))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<HeroItems> heroItemsResult = objectMapper.readValue(content, new TypeReference<List<HeroItems>>() {
        });

        assertThat(heroItemsResult.get(0).getItem()).isEqualTo(item);
    }


}
