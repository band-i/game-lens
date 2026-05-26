package io.bandi.gamelens.game.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "released")
    private LocalDate released;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
