package pl.tiwpr.chessbase.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.exceptions.MissingDataException;
import pl.tiwpr.chessbase.model.Gender;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.views.PlayerView;
import pl.tiwpr.chessbase.repositories.PlayersRepository;

import java.util.Optional;

@Service
@Slf4j
public class PlayersService {

    public final ModelMapper modelMapper;
    private final PlayersRepository playersRepository;

    public PlayersService(
            ModelMapper modelMapper,
            PlayersRepository playersRepository){
        this.modelMapper = modelMapper;
        this.playersRepository = playersRepository;
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

}
