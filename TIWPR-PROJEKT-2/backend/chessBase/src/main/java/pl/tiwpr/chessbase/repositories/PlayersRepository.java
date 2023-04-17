package pl.tiwpr.chessbase.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.tiwpr.chessbase.model.Player;
import java.util.Optional;

public interface PlayersRepository extends JpaRepository<Player,Long> {
    Optional<Player> findOneById(Long id);
}
