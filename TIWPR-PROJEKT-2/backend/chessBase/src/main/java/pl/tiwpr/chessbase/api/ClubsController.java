package pl.tiwpr.chessbase.api;

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
import pl.tiwpr.chessbase.model.views.PlayerView;
import pl.tiwpr.chessbase.services.ClubsService;

import java.util.Optional;

@Validated
@Slf4j
@RestController
@RequestMapping("/clubs")
public class ClubsController {
    private final ClubsService clubsService;

    public ClubsController(ClubsService clubsService) {
        this.clubsService = clubsService;
    }

    @GetMapping
    public Page<ClubView> getAllClubs(@RequestParam(defaultValue ="1") int page,
                                          @RequestParam(defaultValue = "#{clubRepository.count()+1}") int size){
        Pageable pageable = PageRequest.of(page-1, size);
        log.info("All clubs requested");
        return this.clubsService.getAllClubs(pageable);
    }

    @PostMapping
    public ResponseEntity<ClubView> addPlayer(@RequestBody Club club){
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

}
