package gg.bayes.challenge.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.bayes.challenge.exception.HeroKillsNotFoundException;
import gg.bayes.challenge.rest.controller.MatchController;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.impl.MatchServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MatchControllerIntegrationTests {
    private static final String PAYLOAD =
            "[00:00:04.999] game state is now 2\n" +
                    "[00:16:49.041] npc_dota_hero_puck hits npc_dota_hero_bane with dota_unknown for 54 damage (775->721)\n" +
                    "[00:16:51.008] npc_dota_hero_pangolier is killed by npc_dota_hero_puck\n" +
                    "[00:16:51.874] npc_dota_hero_pangolier buys item item_ward_observer\n" +
                    "[00:16:48.408] npc_dota_hero_bane casts ability bane_nightmare (lvl 1) on npc_dota_hero_abyssal_underlord";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MatchController matchController;
    @Autowired
    MatchServiceImpl matchService;


    @Test
    @Order(1)
    public void givenMatchId_whenGetHeroKills_ThenHeroKillsNotFound() {
        Long matchId = 1l;

        assertThrows(HeroKillsNotFoundException.class, () -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/match/{matchId}", matchId));
            } catch (NestedServletException e) {
                throw (Exception) e.getCause();
            }
        });

    }

    @Test
    @Order(2)
    public void givenValidMatch_whenSave_thenSuccess() throws Exception {

        // when perform ingest then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/match")
                .content(PAYLOAD)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInvalidMatchId_whenGetHeroKills_ThenBadRequest() throws Exception {
        Long matchId = -1l;

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}", matchId))
        .andExpect(status().isBadRequest());

    }


    @Test
    public void givenMatchIdAndHeroName_whenGetHeroItems_ThenSuccess() throws Exception {
        Long matchId = 1l;
        String heroName = "pangolier";
        String itemName = "ward_observer";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/match")
                .content(PAYLOAD)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        String content = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}/{heroName}/items", matchId, heroName))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<HeroItems> heroItemsResult = objectMapper.readValue(content, new TypeReference<List<HeroItems>>() {
        });

        assertThat(heroItemsResult.get(0).getItem()).isEqualTo(itemName);
    }

    @Test
    public void givenMatchId_whenGetHeroKills_ThenSuccess() throws Exception {
        Long matchId = 1l;
        String hero = "puck";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/match")
                .content(PAYLOAD)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        String content = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}", matchId))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<HeroKills> heroItemsResult = objectMapper.readValue(content, new TypeReference<List<HeroKills>>() {
        });

        assertThat(heroItemsResult.get(0).getHero()).isEqualTo(hero);
    }

    @Test
    public void givenMatchIdAndHeroName_whenGetHeroDamage_ThenGetRightDamage() throws Exception {
        Long matchId = 1l;
        String heroName = "puck";
        Integer totalDamage = 54;

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/match")
                .content(PAYLOAD)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        String content = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/match/{matchId}/{heroName}/damage", matchId, heroName))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<HeroDamage> heroItemsResult = objectMapper.readValue(content, new TypeReference<List<HeroDamage>>() {
        });

        assertThat(heroItemsResult.get(0).getTotalDamage()).isEqualTo(totalDamage);
    }


}
