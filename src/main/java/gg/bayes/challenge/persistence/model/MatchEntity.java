package gg.bayes.challenge.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "match")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private int hash;
    @Lob
    private String payload;
}
