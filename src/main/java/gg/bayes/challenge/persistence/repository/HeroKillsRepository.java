package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.HeroKillsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeroKillsRepository extends JpaRepository<HeroKillsEntity, Long> {
    Optional<List<HeroKillsEntity>> findByMatchId(Long matchId);
}
