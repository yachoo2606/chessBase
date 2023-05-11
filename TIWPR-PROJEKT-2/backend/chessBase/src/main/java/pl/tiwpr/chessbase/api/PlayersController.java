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
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.views.PlayerView;
import pl.tiwpr.chessbase.services.PlayersService;

import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/players")
public class PlayersController {


    private final PlayersService playersService;

    public PlayersController(PlayersService playersService) {
        this.playersService = playersService;
    }

    @GetMapping
    public Page<PlayerView> getAllPlayers(@RequestParam(defaultValue ="1") int page,
                                          @RequestParam(defaultValue = "#{playersRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        log.info("All Players Requested");
        return this.playersService.getAllPlayers(pageable);
    }

    @PostMapping
    public ResponseEntity<PlayerView> addPlayer(@RequestBody Player player){
        if(player.getName() == null || player.getLastName()==null){
            log.warn("Player add failed: Name and last name must not be null");
            throw new MissingDataException("Name and last name must not be null");
        }
        PlayerView addedPlayer = this.playersService.createPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPlayer);
    }

    @GetMapping("/{id}")
    public Optional<PlayerView> welcomePage(@PathVariable @NonNull Long id){
        return this.playersService.getOneById(id);
    }
}
