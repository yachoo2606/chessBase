package pl.tiwpr.chessbase.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.views.PlayerView;
import pl.tiwpr.chessbase.repositories.PlayersRepository;

import java.util.Optional;

@Service
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

    public PlayerView createPlayer(Player player){
        Player addedPlayer = playersRepository.save(player);
        return this.modelMapper.map(addedPlayer, PlayerView.class);
    }

    public Optional<PlayerView> getOneById(Long id){
        Optional<Player> requestedPlayer = playersRepository.findOneById(id);

        return  requestedPlayer.map(player -> modelMapper.map(player, PlayerView.class));

    }

}
