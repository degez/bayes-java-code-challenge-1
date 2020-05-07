package gg.bayes.challenge.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "hero_damage")
public class HeroDamageEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long heroId;
    private Long matchId;
    private String target;
    private Integer damageInstances;
    private Integer totalDamage;
}
