package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    MatchEntity findByHash(int hash);
}
