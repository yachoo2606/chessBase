package pl.tiwpr.chessbase.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.model.Game;
import pl.tiwpr.chessbase.model.Result;
import pl.tiwpr.chessbase.repositories.GamesRepository;

import java.util.Optional;

@Service
@Slf4j
public class GamesService {

    private final GamesRepository gamesRepository;

    public GamesService(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    public Game createGame(Game game){
        Game addedGame = gamesRepository.save(game);
        log.info("Game added: "+game);
        return addedGame;
    }

    public Optional<Game> getOneGame(Long id){
        return gamesRepository.findGameById(id);
    }

    public Page<Game> getAllClubs(Pageable pageable) {
        return gamesRepository.findAll(pageable);
    }

    public Page<Game> getByResult(Pageable pageable, Result result){
        return gamesRepository.findGamesByResult(result,pageable);
    }

    public Page<Game> getGamesByPlayer(Long id,Pageable pageable){
        return gamesRepository.findByPlayerIsWhiteOrBlack(id,pageable);
    }

}
