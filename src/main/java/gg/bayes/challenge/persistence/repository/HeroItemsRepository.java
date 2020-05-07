package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.HeroItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeroItemsRepository extends JpaRepository<HeroItemsEntity, Long> {

    Optional<List<HeroItemsEntity>> findByHeroIdAndMatchId(Long id, Long matchId);

}
