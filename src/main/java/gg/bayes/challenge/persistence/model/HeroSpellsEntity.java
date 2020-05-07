package gg.bayes.challenge.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "hero_spells")
public class HeroSpellsEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long heroId;
    private Long matchId;
    private String spell;
    private Integer casts;
}
