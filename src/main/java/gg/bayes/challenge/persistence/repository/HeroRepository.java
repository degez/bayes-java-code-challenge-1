package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.HeroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeroRepository extends JpaRepository<HeroEntity, Long> {
    Optional<HeroEntity> findByName(String name);
}
