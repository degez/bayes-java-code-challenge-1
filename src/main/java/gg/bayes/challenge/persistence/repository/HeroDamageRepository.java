package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.HeroDamageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeroDamageRepository extends JpaRepository<HeroDamageEntity, Long> {
    Optional<List<HeroDamageEntity>> findByHeroIdAndMatchId(Long heroId, Long matchId);
}
