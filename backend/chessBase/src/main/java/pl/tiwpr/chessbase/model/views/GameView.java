package pl.tiwpr.chessbase.model.views;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.tiwpr.chessbase.model.Result;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class GameView {
    private String event;
    private LocalDate date;
    private Integer round;
    private Long version;
    private Long whitePlayer;
    private Integer whiteELO;
    private Long blackPlayer;
    private Integer blackELO;
    private String pgn;
    private Result result;
}
