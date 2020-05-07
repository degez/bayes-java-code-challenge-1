package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.HeroSpellsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeroSpellsRepository extends JpaRepository<HeroSpellsEntity, Long> {
    Optional<List<HeroSpellsEntity>> findByHeroIdAndMatchId(Long heroId, Long matchId);
}
