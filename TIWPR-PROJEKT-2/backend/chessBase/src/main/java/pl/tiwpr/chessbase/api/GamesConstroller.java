package pl.tiwpr.chessbase.api;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.model.Game;
import pl.tiwpr.chessbase.model.Result;
import pl.tiwpr.chessbase.services.GamesService;
import pl.tiwpr.chessbase.services.PlayersService;

import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/games")
public class GamesConstroller {
    private final GamesService gamesService;
    private final PlayersService playersService;

    public GamesConstroller(GamesService gamesService, PlayersService playersService) {
        this.gamesService = gamesService;
        this.playersService = playersService;
    }

    @GetMapping
    public Page<Game> getAllGames(@RequestParam(defaultValue ="1") int page,
                                  @RequestParam(defaultValue = "#{gamesRepository.count()+1}") int size,
                                  @Nullable @RequestParam Result result){
        Pageable pageable = PageRequest.of(page-1, size);
        if(result!=null){
            log.info("Requested Games by "+result+" result");
            return gamesService.getByResult(pageable,result);
        }
        log.info("All games Requested");
        return this.gamesService.getAllClubs(pageable);
    }

    @PostMapping
    public Game addGame(@RequestBody Game game) throws ClassNotFoundException {
        if(playersService.getOneByIdOptional(game.getBlackPlayer().getId()).isEmpty() || playersService.getOneByIdOptional(game.getWhitePlayer().getId()).isEmpty()){
            throw new ClassNotFoundException("There is no player with provided ID");
        }
        return gamesService.createGame(game);
    }

    @GetMapping("/{id}")
    public Game getSpecificGame(@PathVariable Long id) throws ClassNotFoundException {
        Optional<Game> game = gamesService.getOneGame(id);
        if(game.isEmpty()) throw new ClassNotFoundException("There is no game with given ID");

        return game.get();

    }
    @GetMapping("/{id}/statistics")
    public ResponseEntity<String> getGameStatistics(@PathVariable String id){
        return new ResponseEntity<String>("Site in Build",HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/players/{id}")
    public Page<Game> getGamesOfPlayer(@PathVariable Long id,
                                       @RequestParam(defaultValue ="1") int page,
                                       @RequestParam(defaultValue = "#{gamesRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return gamesService.getGamesByPlayer(id,pageable);
    }

}
