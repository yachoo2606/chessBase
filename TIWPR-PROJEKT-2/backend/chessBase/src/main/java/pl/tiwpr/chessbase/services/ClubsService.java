package pl.tiwpr.chessbase.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.Address;
import pl.tiwpr.chessbase.model.Club;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.views.ClubView;
import pl.tiwpr.chessbase.repositories.ClubRepository;
import pl.tiwpr.chessbase.repositories.PlayersRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ClubsService {
    public final ModelMapper modelMapper;
    private final ClubRepository clubRepository;
    private final PlayersRepository playersRepository;
    public ClubsService(ModelMapper modelMapper, ClubRepository clubRepository, PlayersRepository playersRepository) {
        this.modelMapper = modelMapper;
        this.clubRepository = clubRepository;
        this.playersRepository = playersRepository;
    }

    public ClubView createClub(Club club){
        Club addedClub = clubRepository.save(club);
        log.info("Club added: "+club);
        return this.modelMapper.map(addedClub,ClubView.class);
    }

    public Optional<Club> getClubByCodeName(String codeName) {
        return clubRepository.findOneByCodeName(codeName);
    }

    public Page<ClubView> getAllClubs(Pageable pageable){
        return clubRepository.findAll(pageable)
                .map(club -> modelMapper.map(club, ClubView.class));
    }

    public Club addPlayerToClub(String codeName, Long playerId){
        Player toAddPlayer = playersRepository.findOneById(playerId).orElseThrow(()-> new MissingDataException("No Player found"));
        Club toAddClub = clubRepository.findOneByCodeName(codeName).orElseThrow(()-> new MissingDataException("No Club found"));

        toAddPlayer.setClub(toAddClub);
        toAddClub.getPlayers().add(toAddPlayer);
        toAddClub = clubRepository.save(toAddClub);
        return toAddClub;
    }

    public String transferPlayers(Player player1, Player player2){

        if(player1.getClub()==null) throw new MissingDataException("player: "+player1.getId().toString()+" has no club");
        if(player2.getClub()==null) throw new MissingDataException("player: "+player2.getId().toString()+" has no club");

        Club club1 = clubRepository.findOneByCodeName(player1.getClub().getCodeName()).orElseThrow(() -> new MissingDataException("There is no club with given codename"));
        Club club2 = clubRepository.findOneByCodeName(player2.getClub().getCodeName()).orElseThrow(() -> new MissingDataException("There is no club with given codename"));

        club1.getPlayers().remove(player1);
        club2.getPlayers().remove(player2);

        player1.setClub(club2);
        player2.setClub(club1);
        playersRepository.save(player1);
        playersRepository.save(player2);

        club1.getPlayers().add(player2);
        club2.getPlayers().add(player1);

        clubRepository.save(club1);
        clubRepository.save(club2);

        return "transferred";

    }

    public ResponseEntity<?> updateClub(String codeName, Long requestVersion, ClubView clubView){
        Optional<Club> existingClub = clubRepository.findOneByCodeName(codeName);

        if(existingClub.isPresent()){
            Club clubToUpdate = existingClub.get();
            if(!requestVersion.equals(clubToUpdate.getVersion())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update out of date object get new game and try again.");
            }

            clubToUpdate.setName(clubView.getName());
            clubToUpdate.setAddress(clubView.getAddress());
            clubRepository.save(clubToUpdate);

            return ResponseEntity.ok().body("Club updated");
        }else{
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested club not found");
        }
    }

    public ResponseEntity<?> updatePartClub(String codeName, Map<String,Object> updates) {
        Optional<Club> existingClub = clubRepository.findOneByCodeName(codeName);
        if(existingClub.isPresent()){
            Club clubToUpdate = existingClub.get();

            for(Map.Entry<String, Object> entry:updates.entrySet()){
                String field = entry.getKey();
                Object value = entry.getValue();
                switch (field){
                    case "name" -> clubToUpdate.setName((String) value);
                    case "address" -> {
                        Address address = modelMapper.map(value, Address.class);
                        if(address.getCountry()!=null) clubToUpdate.getAddress().setCountry(address.getCountry());
                        if(address.getCity()!=null) clubToUpdate.getAddress().setCity(address.getCity());
                        if(address.getStreet()!=null) clubToUpdate.getAddress().setStreet(address.getStreet());
                        if(address.getNumber()!=null) clubToUpdate.getAddress().setNumber(address.getNumber());
                        if(address.getPostCode()!=null) clubToUpdate.getAddress().setPostCode(address.getPostCode());
                        if(address.getLatitude()!=null) clubToUpdate.getAddress().setLatitude(address.getLatitude());
                        if(address.getLongitude()!=null) clubToUpdate.getAddress().setLongitude(address.getLongitude());
                    }
                }
            }
            clubRepository.save(clubToUpdate);
            log.info("Club "+codeName+" updated");
            return ResponseEntity.ok().body("Club "+codeName+" updated");
        }else{
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested club not found");
        }
    }

    public ResponseEntity<?> deleteClub(String codeName) {
        Optional<Club> existingClub = clubRepository.findOneByCodeName(codeName);

        if(existingClub.isPresent()){
            if(existingClub.get().getPlayers().isEmpty()){
                clubRepository.delete(existingClub.get());
                return ResponseEntity.ok().body("Club deleted");
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("First delete players from club");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested club not found");
        }

    }
}
