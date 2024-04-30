package pl.tiwpr.chessbase.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tiwpr.chessbase.model.BasicStatistics;
import pl.tiwpr.chessbase.model.Result;
import pl.tiwpr.chessbase.repositories.GamesRepository;

@Validated
@Slf4j
@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {

    private final GamesRepository gamesRepository;

    public StatisticsController(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    @GetMapping
    public ResponseEntity<?> getBasicStatistics(){
        BasicStatistics basicStatistics = new BasicStatistics();

        basicStatistics.setBlack(gamesRepository.findGamesByResult(Result.BLACK).size());
        basicStatistics.setWhite(gamesRepository.findGamesByResult(Result.WHITE).size());
        basicStatistics.setDraw(gamesRepository.findGamesByResult(Result.DRAW).size());
        basicStatistics.setGames(gamesRepository.findAll().size());

        return ResponseEntity.ok().body(basicStatistics);
    }
}
