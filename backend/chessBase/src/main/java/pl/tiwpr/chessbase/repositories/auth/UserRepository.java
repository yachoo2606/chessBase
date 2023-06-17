package pl.tiwpr.chessbase.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tiwpr.chessbase.model.auth.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findOneById(Long id);
}
