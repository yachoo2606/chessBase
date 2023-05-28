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
import pl.tiwpr.chessbase.services.tokens.TokenService;

import java.util.Map;
import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/clubs")
public class ClubsController {
    private final ClubsService clubsService;
    private final PlayersService playersService;
    private final TokenService tokenService;


    public ClubsController(ClubsService clubsService, PlayersService playersService, TokenService tokenService) {
        this.clubsService = clubsService;
        this.playersService = playersService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public Page<ClubView> getAllClubs(@RequestParam(defaultValue ="1") int page,
                                          @RequestParam(defaultValue = "#{clubRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        log.info("All clubs requested");
        return this.clubsService.getAllClubs(pageable);
    }

    @PostMapping
    public ResponseEntity<?> addClub(@RequestHeader("Token") String postToken ,@RequestBody Club club){

        if(!tokenService.isTokenValid(postToken)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicated Request");
        }
        tokenService.invalidateToken(postToken);

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
    public ResponseEntity<?> addPlayerToClub(@RequestHeader("Token") String postToken ,@PathVariable String codeName, @RequestParam("playerId") Long playerId){

        if(!tokenService.isTokenValid(postToken)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicated Request");
        }
        tokenService.invalidateToken(postToken);

        return ResponseEntity.ok().body(clubsService.addPlayerToClub(codeName, playerId));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferPlayers(@RequestHeader("Token") String postToken ,@NonNull @RequestParam Long p1id, @NonNull @RequestParam Long p2id){

        if(!tokenService.isTokenValid(postToken)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicated Request");
        }
        tokenService.invalidateToken(postToken);

        Player player1 = playersService.getOneById(p1id);
        Player player2 = playersService.getOneById(p2id);

        return ResponseEntity.ok().body(clubsService.transferPlayers(player1,player2));
    }

    @PutMapping("/{codeName}")
    public ResponseEntity<?> updateClub(@PathVariable String codeName, @RequestHeader("VERSION") Long requestVersion,@RequestBody @NonNull ClubView clubView) {
        return clubsService.updateClub(codeName, requestVersion, clubView);
    }
    @PatchMapping("/{codeName}")
    public ResponseEntity<?> updatePartClub(@PathVariable String codeName, @RequestHeader("VERSION") Long requestVersion,@RequestBody @NonNull Map<String,Object> clubView) {
        return clubsService.updatePartClub(codeName, requestVersion, clubView);
    }

    @DeleteMapping("/{codeName}")
    public ResponseEntity<?> deleteClub(@PathVariable String codeName){
        return clubsService.deleteClub(codeName);
    }

}
