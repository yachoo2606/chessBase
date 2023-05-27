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
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.Result;
import pl.tiwpr.chessbase.services.GamesService;
import pl.tiwpr.chessbase.services.PlayersService;
import pl.tiwpr.chessbase.services.tokens.TokenService;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/games")
public class GamesConstroller {
    private final GamesService gamesService;
    private final PlayersService playersService;

    private final TokenService tokenService;

    public GamesConstroller(GamesService gamesService, PlayersService playersService, TokenService tokenService) {
        this.gamesService = gamesService;
        this.playersService = playersService;
        this.tokenService = tokenService;
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
    public ResponseEntity<?> addGame(@RequestHeader("Token") String postToken,@RequestBody Game game) throws ClassNotFoundException {

        if(!tokenService.isTokenValid(postToken)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicated Request");
        }
        tokenService.invalidateToken(postToken);

        if(playersService.getOneByIdOptional(game.getBlackPlayer().getId()).isEmpty() || playersService.getOneByIdOptional(game.getWhitePlayer().getId()).isEmpty()){
            throw new ClassNotFoundException("There is no player with provided ID");
        }
        return ResponseEntity.ok().body(gamesService.createGame(game));
    }

    @GetMapping("/{id}")
    public Game getSpecificGame(@PathVariable Long id) throws ClassNotFoundException {
        Optional<Game> game = gamesService.getOneGame(id);
        if(game.isEmpty()) throw new ClassNotFoundException("There is no game with given ID");

        return game.get();

    }
    @GetMapping("/{id}/statistics")
    public ResponseEntity<String> getGameStatistics(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Site in Build");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateSpecificGameField(@PathVariable Long id,
                                                     @RequestHeader("VERSION") Long requestVersion,
                                                     @RequestBody Map<String, Object> updates){
        Optional<Game> gameToEdit = gamesService.getOneGame(id);
        if(gameToEdit.isPresent()){
            if(!requestVersion.equals(gameToEdit.get().getVersion())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update out of date object get new game and try again.");
            }

            Game gameToUpdate = gameToEdit.get();

            for(Map.Entry<String, Object> entry: updates.entrySet()){
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field){
                    case "event" -> gameToUpdate.setEvent((String) value);
                    case "date" -> gameToUpdate.setDate(LocalDate.parse(value.toString()));
                    case "round" ->{
                        if(value instanceof Integer){
                            gameToUpdate.setRound((Integer) value);
                        }
                    }
                    case "whitePlayer" -> {
                            Player player = playersService.getOneById(Long.valueOf((String) value));
                            gameToUpdate.setWhitePlayer(player);
                    }
                    case "whiteELO" ->{
                        if(value instanceof Integer){
                            gameToUpdate.setWhiteELO((Integer) value);
                        }
                    }
                    case "blackPlayer" ->{
                            Player player = playersService.getOneById(Long.valueOf((String) value));
                            gameToUpdate.setBlackPlayer(player);
                    }
                    case "blackELO" ->{
                        if(value instanceof Integer){
                            gameToUpdate.setBlackELO((Integer) value);
                        }
                    }
                    case "result" -> {
                        if(value instanceof Result){
                            gameToUpdate.setResult((Result) value);
                        }
                    }
                    case "pgn" -> gameToUpdate.setPgn((String) value);
                }

            }
            return gamesService.updateGame(gameToUpdate);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested game not found");
        }
    }
}
