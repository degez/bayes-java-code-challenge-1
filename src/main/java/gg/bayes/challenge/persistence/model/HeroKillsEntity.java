package gg.bayes.challenge.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "hero_kills")
public class HeroKillsEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long heroId;
    private Long matchId;
    private Integer kills;
}
