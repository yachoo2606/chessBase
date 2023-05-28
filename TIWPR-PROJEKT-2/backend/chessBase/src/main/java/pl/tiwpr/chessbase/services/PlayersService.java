package pl.tiwpr.chessbase.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.*;
import pl.tiwpr.chessbase.model.views.PlayerView;
import pl.tiwpr.chessbase.repositories.ClubRepository;
import pl.tiwpr.chessbase.repositories.PlayersRepository;
import pl.tiwpr.chessbase.repositories.TitleRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PlayersService {

    public final ModelMapper modelMapper;
    private final PlayersRepository playersRepository;
    private final ClubRepository clubRepository;
    private final TitleRepository titleRepository;

    public PlayersService(
            ModelMapper modelMapper,
            PlayersRepository playersRepository, ClubRepository clubRepository, TitleRepository titleRepository){
        this.modelMapper = modelMapper;
        this.playersRepository = playersRepository;
        this.clubRepository = clubRepository;
        this.titleRepository = titleRepository;
    }

    public Page<PlayerView> getAllPlayers(Pageable pageable){
        return playersRepository.findAll(pageable)
                .map(player -> modelMapper.map(player,PlayerView.class));
    }
    public Page<PlayerView> getAllByGender(Pageable pageable, Gender gender){
        return playersRepository.findAllByGender(pageable, gender)
                .map(player -> modelMapper.map(player,PlayerView.class));
    }

    public PlayerView createPlayer(Player player){
        Player addedPlayer = playersRepository.save(player);
        log.info("Player Added: "+addedPlayer.toString());
        return this.modelMapper.map(addedPlayer, PlayerView.class);
    }

    public Optional<PlayerView> getOneByIdOptional(Long id){
        Optional<Player> requestedPlayer = playersRepository.findOneById(id);

        if(requestedPlayer.isEmpty()) throw new MissingDataException("There is no user with given ID");

        log.info("Details of Player: "+ requestedPlayer.toString());
        return  requestedPlayer.map(player -> modelMapper.map(player, PlayerView.class));

    }

    public Player getOneById(Long id){
        Optional<Player> requestedPlayer = playersRepository.findOneById(id);

        if(requestedPlayer.isEmpty()) throw new MissingDataException("There is no user with given ID");
        return  requestedPlayer.get();

    }

    public ResponseEntity<?> updateWholePlayer(Long id, Long requestVersion, Player updatedPlayer) {
        Optional<Player> exisingPlayer = playersRepository.findOneById(id);
        if(exisingPlayer.isPresent()){
            if(requestVersion.equals(exisingPlayer.get().getVersion())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update out of date object get new player and try again.");
            }

            Player playerToUpdate = exisingPlayer.get();

            playerToUpdate.setName(updatedPlayer.getName());
            playerToUpdate.setLastName(updatedPlayer.getLastName());
            playerToUpdate.setBirthDate(updatedPlayer.getBirthDate());
            playerToUpdate.setGender(updatedPlayer.getGender());
            playerToUpdate.setELO(updatedPlayer.getELO());
            playerToUpdate.setTitle(updatedPlayer.getTitle());
            playerToUpdate.setClub(updatedPlayer.getClub());

            playersRepository.save(playerToUpdate);
            log.info("Player "+playerToUpdate.getId()+ " updated");
            return ResponseEntity.ok().body("Player "+playerToUpdate.getId()+ " updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
    }

    public ResponseEntity<?> updatePartOfPlayer(Long id, Long requestVersion, Map<String, Object> updates) {
        Optional<Player> existingPlayer = playersRepository.findOneById(id);

        if(existingPlayer.isPresent()){

            if(!requestVersion.equals(existingPlayer.get().getVersion())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update out of date object get new game and try again.");
            }

            Player playerToUpdate = existingPlayer.get();
            for(Map.Entry<String, Object> entry: updates.entrySet()){
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field){
                    case "name"-> playerToUpdate.setName((String) value);
                    case "lastName"->playerToUpdate.setLastName((String) value);
                    case "birthDate"->playerToUpdate.setBirthDate(LocalDate.parse(value.toString()));
                    case "gender"->{
                        if(value instanceof Gender){
                            playerToUpdate.setGender((Gender) value);
                        }
                    }
                    case "ELO"->{
                        if(value instanceof Integer){
                            playerToUpdate.setELO((Integer) value);
                        }
                    }
                    case "title"->{
                        Title title = titleRepository.getReferenceById(Long.valueOf((String) value));
                        playerToUpdate.setTitle(title);
                    }
                    case "club"->{
                        Club club = clubRepository.getReferenceById(Long.valueOf((String) value));
                        playerToUpdate.setClub(club);
                    }
                }
            }
            playersRepository.save(playerToUpdate);
            return ResponseEntity.ok().body("Player updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested player not found");
        }
    }
}
