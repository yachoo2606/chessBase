package pl.tiwpr.chessbase.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.tiwpr.chessbase.model.Game;
import pl.tiwpr.chessbase.model.Result;

import java.util.List;
import java.util.Optional;

public interface GamesRepository  extends JpaRepository<Game,Long> {

    Optional<Game> findGameById(Long id);
    @Query("SELECT g FROM Game g WHERE g.whitePlayer.id = :id OR g.blackPlayer.id = :id")
    Page<Game> findByPlayerIsWhiteOrBlack(Long id, Pageable pageable);
    @Query("SELECT g FROM Game g WHERE g.whitePlayer.id = :id OR g.blackPlayer.id = :id")
    List<Game> findByPlayerIsWhiteOrBlackList(Long id);

    Page<Game> findGamesByResult(Result result,Pageable pageable);
    List<Game> findGamesByResult(Result result);

}
