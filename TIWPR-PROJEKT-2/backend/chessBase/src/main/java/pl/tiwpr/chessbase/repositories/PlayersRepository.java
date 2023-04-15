package pl.tiwpr.chessbase.repositories;
import org.springframework.data.repository.CrudRepository;
import pl.tiwpr.chessbase.model.Player;
import java.util.Optional;

public interface PlayersRepository extends CrudRepository<Player,Long> {
    Optional<Player> findOneById(Long id);
}
