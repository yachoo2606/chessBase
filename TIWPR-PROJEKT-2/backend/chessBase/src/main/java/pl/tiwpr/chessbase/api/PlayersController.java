package pl.tiwpr.chessbase.api;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.repositories.PlayersRepository;

import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/players")
public class PlayersController {

    @Autowired
    private PlayersRepository playersRepository;


    @GetMapping
    public Page<Player> getAllPlayers(@RequestParam(defaultValue ="1") int page,
                                      @RequestParam(defaultValue = "#{playersRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        log.info("All Players Requested");
        return playersRepository.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player){
        if(player.getName() == null || player.getLastName()==null){
            log.warn("Player add failed: Name and last name must not be null");
            throw new MissingDataException("Name and last name must not be null");
        }
        Player addedPlayer = playersRepository.save(player);
        log.info("Player Added: "+addedPlayer.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPlayer);
    }

    @GetMapping("/{id}")
    public Optional<Player> welcomePage(@PathVariable @NonNull Long id){
        Optional<Player> requestedPlayer = playersRepository.findOneById(id);
        log.info("Details of Player: "+ requestedPlayer.toString());
        return requestedPlayer;
    }
}
