package pl.tiwpr.chessbase.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.Club;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.views.ClubView;
import pl.tiwpr.chessbase.repositories.ClubRepository;
import pl.tiwpr.chessbase.repositories.PlayersRepository;

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

}
