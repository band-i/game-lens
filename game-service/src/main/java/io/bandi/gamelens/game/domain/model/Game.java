package io.bandi.gamelens.game.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rawg_id")
    private Long rawgId;

    @Column(name = "title")
    private String title;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "average_playtime")
    private Integer averagePlaytime;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "platform")
    private String platform;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
