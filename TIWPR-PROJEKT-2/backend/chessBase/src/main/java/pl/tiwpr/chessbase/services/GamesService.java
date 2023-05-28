package pl.tiwpr.chessbase.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.tiwpr.chessbase.model.Game;
import pl.tiwpr.chessbase.model.Player;
import pl.tiwpr.chessbase.model.Result;
import pl.tiwpr.chessbase.model.views.GameView;
import pl.tiwpr.chessbase.repositories.GamesRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class GamesService {

    private final GamesRepository gamesRepository;
    private final PlayersService playersService;

    public GamesService(GamesRepository gamesRepository, PlayersService playersService) {
        this.gamesRepository = gamesRepository;
        this.playersService = playersService;
    }

    public Game createGame(GameView gameView){

        Game game = new Game();
        game.setEvent(gameView.getEvent());
        game.setDate(gameView.getDate());
        game.setRound(gameView.getRound());
        game.setWhitePlayer(playersService.getOneById(gameView.getWhitePlayer()));
        game.setBlackPlayer(playersService.getOneById(gameView.getBlackPlayer()));
        game.setWhiteELO(gameView.getWhiteELO());
        game.setBlackELO(gameView.getBlackELO());
        game.setPgn(gameView.getPgn());
        game.setResult(gameView.getResult());

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


    public ResponseEntity<?> updateGame(Long id, Long requestVersion,Map<String, Object> updates) {
        Optional<Game> gameToEdit = gamesRepository.findGameById(id);
        if(gameToEdit.isPresent()){
            if(!requestVersion.equals(gameToEdit.get().getVersion())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update out of date object get new game and try again.");
            }

            Game gameToUpdate = gameToEdit.get();

            for(Map.Entry<String, Object> entry: updates.entrySet()){
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field){
                    case "event" -> gameToUpdate.setEvent((String) value);
                    case "date" -> gameToUpdate.setDate(LocalDate.parse(value.toString()));
                    case "round" ->{
                        if(value instanceof Integer){
                            gameToUpdate.setRound((Integer) value);
                        }
                    }
                    case "whitePlayer" -> {
                            Player player = playersService.getOneById(Long.valueOf((String) value));
                            gameToUpdate.setWhitePlayer(player);
                    }
                    case "whiteELO" ->{
                        if(value instanceof Integer){
                            gameToUpdate.setWhiteELO((Integer) value);
                        }
                    }
                    case "blackPlayer" ->{
                            Player player = playersService.getOneById(Long.valueOf((String) value));
                            gameToUpdate.setBlackPlayer(player);
                    }
                    case "blackELO" ->{
                        if(value instanceof Integer){
                            gameToUpdate.setBlackELO((Integer) value);
                        }
                    }
                    case "result" -> {
                        if(value instanceof Result){
                            gameToUpdate.setResult((Result) value);
                        }
                    }
                    case "pgn" -> gameToUpdate.setPgn((String) value);
                }

            }
            gamesRepository.save(gameToUpdate);
            return ResponseEntity.ok().body("Game updated");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested game not found");
        }
    }

    public ResponseEntity<?> deleteGame(Long id) {
        Optional<Game> gameToDelete = gamesRepository.findGameById(id);
        if(gameToDelete.isPresent()){
            log.warn("Game "+id+" deleted;");
            gamesRepository.delete(gameToDelete.get());
            return ResponseEntity.ok().body("Game deleted");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }
    }
}
