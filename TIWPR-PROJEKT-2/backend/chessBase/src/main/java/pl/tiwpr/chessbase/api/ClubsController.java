package pl.tiwpr.chessbase.api;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.Club;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.views.ClubView;
import pl.tiwpr.chessbase.services.ClubsService;
import pl.tiwpr.chessbase.services.PlayersService;

import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/clubs")
public class ClubsController {
    private final ClubsService clubsService;
    private final PlayersService playersService;


    public ClubsController(ClubsService clubsService, PlayersService playersService) {
        this.clubsService = clubsService;
        this.playersService = playersService;
    }

    @GetMapping
    public Page<ClubView> getAllClubs(@RequestParam(defaultValue ="1") int page,
                                          @RequestParam(defaultValue = "#{clubRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        log.info("All clubs requested");
        return this.clubsService.getAllClubs(pageable);
    }

    @PostMapping
    public ResponseEntity<ClubView> addClub(@RequestBody Club club){
        Optional<Club> tempClub = clubsService.getClubByCodeName(club.getCodeName());
        if(tempClub.isPresent()){
            throw new DataIntegrityViolationException("Club with provided CodeName already exists");
        }

        if(club.getName() == null || club.getCodeName()==null || club.getAddress() == null){
            log.warn("Club add failed: Name and CodeName and Address must not be null");
            throw new MissingDataException("Name and CodeName and Address must not be null");
        }
        ClubView addedClub = this.clubsService.createClub(club);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedClub);
    }

    @GetMapping("/{codeName}")
    public ResponseEntity<Club> getClubByCodeName(@PathVariable String codeName){
       Club requestedClub = clubsService.getClubByCodeName(codeName)
                .orElseThrow(() -> new MissingDataException("Club not found"));
       return ResponseEntity.status(HttpStatus.OK).body(requestedClub);
    }

    @PostMapping("/{codeName}/players")
    public ResponseEntity<Club> addPlayerToClub(@PathVariable String codeName, @RequestParam("playerId") Long playerId){
        return ResponseEntity.ok().body(clubsService.addPlayerToClub(codeName, playerId));
    }

    @PostMapping("/transfer")
    public String transferPlayers(@NonNull @RequestParam Long p1id, @NonNull @RequestParam Long p2id){
        Player player1 = playersService.getOneById(p1id);
        Player player2 = playersService.getOneById(p2id);

        return clubsService.transferPlayers(player1,player2);
    }

}
