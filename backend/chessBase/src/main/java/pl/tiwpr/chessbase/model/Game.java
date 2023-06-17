package pl.tiwpr.chessbase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "games")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column()
    private String event;

    @Column
    private LocalDate date;

    @Column()
    private Integer round;

    @Version
    @Column(name="OPTCLOCK", nullable = false, columnDefinition = "integer DEFAULT 1")
    private Long version;

    @ManyToOne
    @JoinColumn(name = "whitePlayer_id", referencedColumnName = "id")
    private Player whitePlayer;

    private Integer whiteELO;


    @ManyToOne
    @JoinColumn(name = "blackPlayer_id", referencedColumnName = "id")
    private Player blackPlayer;

    private Integer blackELO;

    private String pgn;

    @Enumerated(EnumType.STRING)
    private Result result;

}

