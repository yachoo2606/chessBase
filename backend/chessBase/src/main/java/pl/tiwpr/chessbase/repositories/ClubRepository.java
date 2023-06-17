package pl.tiwpr.chessbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tiwpr.chessbase.model.Club;

import java.util.Optional;


public interface ClubRepository  extends JpaRepository<Club,Long> {
    Optional<Club> findOneById(Long id);
    Optional<Club> findOneByCodeName(String codeName);
    Optional<Club> findOneByAddress_Country(String country);

}
