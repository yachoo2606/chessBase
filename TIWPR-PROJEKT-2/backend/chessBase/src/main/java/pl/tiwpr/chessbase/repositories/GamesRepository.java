package pl.tiwpr.chessbase.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.tiwpr.chessbase.model.Game;
import pl.tiwpr.chessbase.model.Result;

import java.util.Optional;

public interface GamesRepository  extends JpaRepository<Game,Long> {

    Optional<Game> findGameById(Long id);
    @Query("SELECT g FROM Game g WHERE g.whitePlayer.id = :id OR g.blackPlayer.id = :id")
    Page<Game> findByPlayerIsWhiteOrBlack(Long id, Pageable pageable);

    Page<Game> findGamesByResult(Result result,Pageable pageable);

}
