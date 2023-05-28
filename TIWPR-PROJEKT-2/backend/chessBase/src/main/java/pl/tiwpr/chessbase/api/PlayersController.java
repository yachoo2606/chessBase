package pl.tiwpr.chessbase.api;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.Game;
import pl.tiwpr.chessbase.model.Gender;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.views.PlayerView;
import pl.tiwpr.chessbase.services.GamesService;
import pl.tiwpr.chessbase.services.PlayersService;
import pl.tiwpr.chessbase.services.tokens.TokenService;

import java.util.Map;
import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/players")
public class PlayersController {


    private final PlayersService playersService;
    private final TokenService tokenService;
    private final GamesService gamesService;

    public PlayersController(PlayersService playersService, TokenService tokenService, GamesService gamesService) {
        this.playersService = playersService;
        this.tokenService = tokenService;
        this.gamesService = gamesService;
    }

    @GetMapping
    public Page<PlayerView> getAllPlayers(@RequestParam(defaultValue ="1") int page,
                                          @RequestParam(defaultValue = "#{playersRepository.count()+1}") int size,
                                          @RequestParam(required = false) Gender gender){
        Pageable pageable = PageRequest.of(page-1, size);

        if(gender!=null){
            log.info("All Players Requested by gender");
            return this.playersService.getAllByGender(pageable,gender);
        }else{
            log.info("All Players Requested");
            return this.playersService.getAllPlayers(pageable);
        }
    }

    @PostMapping
    public ResponseEntity<?> addPlayer(@RequestHeader("Token") String postToken ,@RequestBody Player player){

        if(!tokenService.isTokenValid(postToken)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicated Request");
        }
        tokenService.invalidateToken(postToken);

        if(player.getName() == null || player.getLastName()==null){
            log.warn("Player add failed: Name and last name must not be null");
            throw new MissingDataException("Name and last name must not be null");
        }
        PlayerView addedPlayer = this.playersService.createPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPlayer);
    }

    @GetMapping("/{id}")
    public Optional<PlayerView> welcomePage(@PathVariable @NonNull Long id){
        return this.playersService.getOneByIdOptional(id);
    }

    @GetMapping("/{id}/games")
    public Page<Game> getGamesOfPlayer(@PathVariable Long id,
                                       @RequestParam(defaultValue ="1") int page,
                                       @RequestParam(defaultValue = "#{gamesRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return gamesService.getGamesByPlayer(id,pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWholePlayer(@PathVariable Long id,
                                               @RequestHeader("VERSION") Long requestVersion,
                                               @RequestBody Player updatedPlayer){
        return playersService.updateWholePlayer(id,requestVersion, updatedPlayer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartOfPlayer(@PathVariable Long id,
                                               @RequestHeader("VERSION") Long requestVersion,
                                               @RequestBody Map<String, Object> updates){
        return playersService.updatePartOfPlayer(id,requestVersion, updates);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> updatePartOfPlayer(@PathVariable Long id){
        return playersService.deletePlayer(id);
    }



}
